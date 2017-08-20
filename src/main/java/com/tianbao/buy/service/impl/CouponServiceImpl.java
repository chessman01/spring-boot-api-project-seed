package com.tianbao.buy.service.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.Context;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.manager.CouponTemplateManager;
import com.tianbao.buy.manager.CouponUserManager;
import com.tianbao.buy.service.PredicateWrapper;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.utils.DateUtils;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.utils.enums.EnumUtil;
import com.tianbao.buy.vo.CouponVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CouponServiceImpl implements CouponService {
    @Resource
    private CouponUserManager couponUserManager;

    @Resource
    private CouponTemplateManager couponTemplateManager;

    @Resource
    private UserService userService;

    @Override
    public void updateCouponUserStatus(long recordId, byte newStatus, byte originStatus) {
        CouponUser couponUser = new CouponUser();

        couponUser.setId(recordId);
        couponUser.setStatus(newStatus);

        Condition condition = new Condition(CouponUser.class);
        condition.createCriteria().andCondition("id=", recordId)
                .andCondition("status=", originStatus);

        couponUserManager.update(couponUser, condition);
    }

    @Override
    public List<CouponVO> getCoupon(byte status) {
        User user = userService.getUserByWxUnionId();

        Set payTypeSet = Sets.newHashSet(CouponVO.PayType.RECHARGE.getCode(),
                CouponVO.PayType.ALL.getCode(), CouponVO.PayType.PAY_PER_VIEW.getCode());
        Set couponUserStatusSet = Sets.newHashSet(status);
        Set templateStatusSet = Sets.newHashSet(CouponVO.Status.NORMAL.getCode(),
                CouponVO.Status.EXPIRED.getCode(), CouponVO.Status.USED.getCode());

        return getCoupon(new Context(), user.getId(), Integer.MAX_VALUE, payTypeSet, templateStatusSet, couponUserStatusSet, null);
    }

    @Override
    public List<CouponVO> getCoupon4Recharge(long userId, int price, Long selectId, Context context) {
        Set payTypeSet = Sets.newHashSet(CouponVO.PayType.RECHARGE.getCode(),CouponVO.PayType.ALL.getCode());
        Set couponUserStatusSet = Sets.newHashSet(CouponVO.Status.NORMAL.getCode());
        Set templateStatusSet = Sets.newHashSet(CouponVO.Status.NORMAL.getCode(),
                CouponVO.Status.EXPIRED.getCode(), CouponVO.Status.USED.getCode());

        return getCoupon(context, userId, price, payTypeSet, templateStatusSet, couponUserStatusSet, selectId);
    }

    @Override
    public List<CouponVO> getCoupon4PayPerView(long userId, int price, Long selectId, Context context) {
        Set payTypeSet = Sets.newHashSet(CouponVO.PayType.ALL.getCode(), CouponVO.PayType.PAY_PER_VIEW.getCode());

        Set couponUserStatusSet = Sets.newHashSet(CouponVO.Status.NORMAL.getCode());
        Set templateStatusSet = Sets.newHashSet(CouponVO.Status.NORMAL.getCode(),
                CouponVO.Status.EXPIRED.getCode(), CouponVO.Status.USED.getCode());

        return getCoupon(context, userId, price, payTypeSet, templateStatusSet, couponUserStatusSet, selectId);
    }

    @Override
    public void obtain(Long templateId) {
        // 获取用户信息
        User user = userService.getUserByWxUnionId();

        // 领券，只有这两源的券能被领
        Set<Byte> sourceSet = Sets.newHashSet(CouponVO.Source.OFFLINE.getCode(), CouponVO.Source.WEIXIN.getCode());
        obtain(templateId, sourceSet, user.getId());
    }

    @Override
    public void obtain(Long templateId, Set<Byte> sourceSet, long userId) {
        // 1. 获取到礼券模版
        CouponTemplate couponTemplate = getTemplate(templateId);

        if (!sourceSet.contains(couponTemplate.getSource()) ||
                !couponTemplate.getStatus().equals(CouponVO.Status.NORMAL.getCode())) {
            throw new BizException("此券不能领用");
        }

        // 2. 判断此礼券用户领用是否超限额
        if (!couponTemplate.getUseNum().equals(NumberUtils.BYTE_ZERO)) {
            // 3. 获取用户礼券领用记录
            List<CouponUser> couponUsers = getRelation(userId);

            int useNum = NumberUtils.INTEGER_ZERO;

            for (CouponUser couponUser :couponUsers) {
                if (couponUser.getCouponTemplateId().equals(templateId)) {
                    useNum++;
                }
            }

            if (useNum >= couponTemplate.getUseNum()) {
                throw new BizException("已达到您此礼券领用上限");
            }
        }

        // 4. 保存领用记录
        CouponUser couponUser = new CouponUser();
        DateTime start = DateUtils.getStart();
        DateTime end = DateUtils.getEnd(start, couponTemplate.getValidityUnit(), couponTemplate.getValidityValue());

        couponUser.setStatus(CouponVO.Status.NORMAL.getCode());
        couponUser.setCouponTemplateId(templateId);
        couponUser.setUserId(userId);
        couponUser.setStartTime(start.toDate());
        couponUser.setEndTime(end.toDate());

        couponUserManager.save(couponUser);
    }

    @Override
    public List<CouponVO> getCardRechargeTemplate(Context context, Long selectId) {
        // 1. 得到所有的模版
        List<CouponTemplate> couponTemplates = getAllTemplate();

        // 2. 过滤出充值满送部分的模版
        Predicate<CouponTemplate> predicate = PredicateWrapper.getPredicate4Template(
                Sets.newHashSet(CouponVO.Status.RECHARGE.getCode()), null, null, new Date());

        Predicate<CouponTemplate> unionPredicate = Predicates.and(predicate);
        List<CouponTemplate> filterResult = Lists.newArrayList(Iterators.filter(couponTemplates.iterator(), unionPredicate));

        if (CollectionUtils.isEmpty(filterResult)) throw new BizException("没找到的充值模版");

        // 3. 按赠送金额排序
        Comparator<CouponTemplate> userComparator = Ordering.from(new priceTemplateComparator());
        Collections.sort(filterResult, userComparator);

        // 4. 转化为前端要的vo
        List<CouponVO> couponVOs = convert2CouponVO(filterResult);

        // 5. 置为已选择
        if (couponVOs == null) return Lists.newArrayList();

        boolean isSelect = false;
        if (selectId != null) {
            for (CouponVO couponVO : couponVOs) {
                if (couponVO.getId().equals(selectId)) {
                    couponVO.setSelected(true);
                    isSelect = true;
                    break;
                }
            }

            filterResult.forEach(item -> {
                if (item.getId().equals(selectId)) {
                    context.setTemplate(item);
                }
            });
        }

        if (isSelect == false) {
            if (couponVOs.size() > 1) {
                couponVOs.get(1).setSelected(true);
                context.setTemplate(filterResult.get(1));
            }

            if (couponVOs.size() == 1) {
                couponVOs.get(0).setSelected(true);
                context.setTemplate(filterResult.get(0));
            }

            return couponVOs;
        }

        return couponVOs;
    }

    private List<CouponVO> getCoupon(Context context, long userId, int rulePrice, Set<Byte> payTypeSet,
                                     Set<Byte> templateStatusSet, Set<Byte> couponUserStatusSet, Long selectId) {
        // 1. 得到所有的模版
        List<CouponTemplate> allCouponTemplates = getAllTemplate();

        if (CollectionUtils.isEmpty(allCouponTemplates)) return Lists.newArrayList();

        // 2. 过滤模版
        Predicate<CouponTemplate> predicate = PredicateWrapper.getPredicate4Template(templateStatusSet, payTypeSet, rulePrice, new Date());

        Predicate<CouponTemplate> unionPredicate = Predicates.and(predicate);
        List<CouponTemplate> couponTemplates = Lists.newArrayList(Iterators.filter(allCouponTemplates.iterator(), unionPredicate));
        if (CollectionUtils.isEmpty(couponTemplates)) return Lists.newArrayList();

        // 3. 获取用户所有礼券
        List<CouponUser> allCouponUsers = getRelation(userId);
        if (CollectionUtils.isEmpty(allCouponUsers)) return Lists.newArrayList();

        // 4. 过滤礼券
        Predicate<CouponUser> predicateUserStatus = PredicateWrapper.getPredicate4CouponUser(couponUserStatusSet);
        Predicate<CouponUser> unionUserPredicate = Predicates.and(predicateUserStatus);
        List<CouponUser> couponUsers = Lists.newArrayList(Iterators.filter(allCouponUsers.iterator(), unionUserPredicate));
        if (CollectionUtils.isEmpty(couponUsers)) return Lists.newArrayList();

        Map<Long, CouponTemplate> couponTemplateMap = getUserTemplateMap(couponTemplates);
        List<CouponVO> couponVOs = Lists.newArrayList();
        boolean selected = false;

        // 5. 转化为前端要的vo
        for (CouponUser couponUser : couponUsers) {
            if (couponTemplateMap.containsKey(couponUser.getCouponTemplateId())) {
                CouponVO couponVO = convert2CouponVO(couponTemplateMap.get(couponUser.getCouponTemplateId()));
                couponVO.setOriginPrice(couponTemplateMap.get(couponUser.getCouponTemplateId()).getPrice());
                couponVO.setCouponUserId(couponUser.getId());

                DateTime start = new DateTime(couponUser.getStartTime());
                DateTime end = new DateTime(couponUser.getEndTime());

                couponVO.setTime("有效期：" + start.toString("yyyy.MM.dd") + "至" + end.toString("yyyy.MM.dd"));

                if (!couponUser.getStatus().equals(CouponVO.Status.NORMAL.getCode())) {
                    couponVO.setRemind(EnumUtil.getEnumObject(couponUser.getStatus(), CouponVO.Status.class).getDesc());
                }

                //计算区间天数
                if (couponUser.getStatus().equals(CouponVO.Status.NORMAL.getCode())) {
                    Period p = new Period(new DateTime(), end, PeriodType.days());
                    int days = p.getDays();

                    if (days <= 7) couponVO.setRemind(days + "天后过期");
                }

                couponVO.setEndTime(couponUser.getEndTime());
                if (couponUser.getId().equals(selectId)) {
                    couponVO.setSelected(true);
                    selected = true;
                }
                context.setCoupon(couponTemplateMap.get(couponUser.getCouponTemplateId()));

                couponVOs.add(couponVO);
            }
        }

        // 6. 按赠送金额排序
        Comparator<CouponVO> userComparator = Ordering.from(new PriceComparator()).compound(new EndTimeComparator());
        Collections.sort(couponVOs, userComparator);
        if (CollectionUtils.isEmpty(couponVOs)) return Lists.newArrayList();

        // 5. 置为已选择
        if (selectId == null || selected == false) {
            couponVOs.get(NumberUtils.INTEGER_ZERO).setSelected(true);
            context.setCoupon(couponTemplateMap.get(couponVOs.get(NumberUtils.INTEGER_ZERO).getId()));
        }

        return couponVOs;
    }

    /* 弄成map */
    private Map<Long, CouponTemplate> getUserTemplateMap(List<CouponTemplate> couponTemplates) {
        Map<Long, CouponTemplate> map = Maps.newConcurrentMap();

        if (couponTemplates == null) return map;

        for (CouponTemplate couponTemplate : couponTemplates) {
            map.put(couponTemplate.getId(), couponTemplate);
        }

        return map;
    }

    public CouponUser getCouponUser(long id) {
        CouponUser couponUser = couponUserManager.findBy("id", id);

        if (couponUser != null) return couponUser;

        throw new BizException("没找到模版.id=" + id);
    }

    public CouponTemplate getTemplate(long id) {
        List<CouponTemplate> couponTemplates = getAllTemplate();

        for (CouponTemplate couponTemplate : couponTemplates) {
            if (couponTemplate.getId().equals(id)) return couponTemplate;
        }

        throw new BizException("没找到模版.id=" + id);
    }

    /* 得到瘾卡充值时的模版 */
    private List<CouponTemplate> getAllTemplate() {
        Condition condition = new Condition(CouponTemplate.class);

        condition.createCriteria().andNotEqualTo("status", CouponVO.Status.DEL.getCode());

        return couponTemplateManager.findByCondition(condition);
    }

    /* 得到用户礼券的关联关系 */
    private List<CouponUser> getRelation(long userId) {
        Condition condition = new Condition(CouponUser.class);

        condition.createCriteria().andCondition("user_id=", userId)
                .andIn("status", Lists.newArrayList(CouponVO.Status.NORMAL.getCode(),
                        CouponVO.Status.EXPIRED.getCode(), CouponVO.Status.USED.getCode()));

        List<CouponUser> couponUsers = couponUserManager.findByCondition(condition);

        return couponUsers;
    }

    private List<CouponVO> convert2CouponVO(List<CouponTemplate> doList) {
        List<CouponVO> couponVOs = Lists.newArrayList();

        for (CouponTemplate couponTemplate : doList) {
            couponVOs.add(convert2CouponVO(couponTemplate));
        }

        return couponVOs;
    }

    private CouponVO convert2CouponVO(CouponTemplate couponTemplate) {
        CouponVO couponVO = new CouponVO();

        BeanUtils.copyProperties(couponTemplate, couponVO);

        String price = MoneyUtils.format(2, couponTemplate.getPrice() / 100f);
        String rulePrice = MoneyUtils.format(2, couponTemplate.getRulePrice() / 100f);
        couponVO.setPrice(price);
        couponVO.setOriginPrice(couponTemplate.getPrice());
        couponVO.setRule("使用条件：订单满" + rulePrice);
        couponVO.setRulePrice(rulePrice);

        if (couponTemplate.getPayType().equals(CouponVO.PayType.PAY_PER_VIEW.getCode())) {
            couponVO.setPayType("仅限单次购买时使用。");
        }

        if (couponTemplate.getPayType().equals(CouponVO.PayType.RECHARGE.getCode())) {
            couponVO.setPayType("仅限瘾卡充值时使用。");
        }

        CouponVO.Source enumSource = EnumUtil.getEnumObject(couponTemplate.getSource(), CouponVO.Source.class);

        if (enumSource != null) couponVO.setSourceDesc("来源：" + enumSource.getDesc());

        return couponVO;
    }
}

class PriceComparator implements Comparator<CouponVO> {
    @Override
    public int compare(CouponVO o1, CouponVO o2) {
        return o1.getOriginPrice() < o2.getOriginPrice() ? 1 : (o1.getOriginPrice() == o2.getOriginPrice() ? 0 : -1);
    }
}

class EndTimeComparator implements Comparator<CouponVO> {
    @Override
    public int compare(CouponVO o1, CouponVO o2) {
        return o1.getEndTime().getTime() < o2.getEndTime().getTime()
                ? 1 : (o1.getEndTime().getTime() == o2.getEndTime().getTime() ? 0 : -1);
    }
}

class priceTemplateComparator implements Comparator<CouponTemplate> {
    @Override
    public int compare(CouponTemplate o1, CouponTemplate o2) {
        return o1.getPrice() > o2.getPrice() ? 1 : (o1.getPrice() == o2.getPrice() ? 0 : -1);
    }
}
