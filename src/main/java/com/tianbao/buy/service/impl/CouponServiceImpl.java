package com.tianbao.buy.service.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.manager.CouponTemplateManager;
import com.tianbao.buy.manager.CouponUserManager;
import com.tianbao.buy.service.CouponPredicateWrapper;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.vo.CouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.*;

@Service
public class CouponServiceImpl implements CouponService {
    @Resource
    private CouponUserManager couponUserManager;

    @Resource
    private CouponTemplateManager couponTemplateManager;

    @Override
    public List<CouponVO> getCoupon(long userId, byte status) {
        return getCoupon(userId, Integer.MAX_VALUE, Sets.newHashSet(CouponVO.PayType.RECHARGE.getCode(),
                CouponVO.PayType.PAY_PER_VIEW.getCode()), Sets.newHashSet(status));
    }

    @Override
    public List<CouponVO> getCoupon4Recharge(long userId, int price) {
        return getCoupon(userId, price, Sets.newHashSet(CouponVO.PayType.RECHARGE.getCode()),
                Sets.newHashSet(CouponVO.Status.NORMAL.getCode()));
    }

    @Override
    public List<CouponVO> getCoupon4PayPerView(long userId, int price) {
        return getCoupon(userId, price, Sets.newHashSet(CouponVO.PayType.PAY_PER_VIEW.getCode()),
                Sets.newHashSet(CouponVO.Status.NORMAL.getCode()));
    }

    @Override
    public List<CouponVO> getCareRechargeTemplate() {
        // 1. 得到所有的模版
        List<CouponTemplate> couponTemplates = getTemplateList();

        // 2. 过滤出充值满送部分的模版
        Predicate<CouponTemplate> predicate4TemplateStatus = CouponPredicateWrapper.getPredicate4TemplateStatus(Sets.newHashSet(CouponVO.Status.RECHARGE.getCode()));
        Predicate<CouponTemplate> predicate4TemplateTime = CouponPredicateWrapper.getPredicate4TemplateTime(new Date());

        Predicate<CouponTemplate> unionPredicate = Predicates.and(predicate4TemplateStatus, predicate4TemplateTime);
        List<CouponTemplate> filterResult = Lists.newArrayList(Iterators.filter(couponTemplates.iterator(), unionPredicate));

        // 3. 按赠送金额排序
        Comparator<CouponTemplate> userComparator = Ordering.from(new priceComparator());
        Collections.sort(filterResult, userComparator);

        // 4. 转化为前端要的vo
        List<CouponVO> couponVOs = convert2CouponVO(filterResult);

        // 5. 置为已选择
        if (couponVOs == null) return Lists.newArrayList();
        if (couponVOs.size() > 1) couponVOs.get(1).setSelected(true);
        if (couponVOs.size() == 1) couponVOs.get(0).setSelected(true);

        return couponVOs;
    }

    private List<CouponVO> getCoupon(long userId, int price, Set<Byte> payType, Set<Byte> status) {
        // 1. 得到所有的模版
        List<CouponTemplate> couponTemplates = getTemplateList();

        // 2. 过滤模版
        Predicate<CouponTemplate> predicateStatus = CouponPredicateWrapper.getPredicate4TemplateStatus(status);
        Predicate<CouponTemplate> predicatePayType = CouponPredicateWrapper.getPredicate4TemplatePayType(payType);
        Predicate<CouponTemplate> predicateRulePrice = CouponPredicateWrapper.getPredicate4TemplatePrice(price);
        Predicate<CouponTemplate> predicateTime = CouponPredicateWrapper.getPredicate4TemplateTime(new Date());

        Predicate<CouponTemplate> unionPredicate = Predicates.and(predicateStatus, predicatePayType, predicateRulePrice, predicateTime);
        List<CouponTemplate> filterCouponTemplate = Lists.newArrayList(Iterators.filter(couponTemplates.iterator(), unionPredicate));

        // 3. 获取用户所有礼券
        List<CouponUser> couponUsers = getRelation(userId);

        // 4. 过滤礼券
        Predicate<CouponUser> predicateUserStatus = CouponPredicateWrapper.getPredicate4CouponUserStatus(Sets.newHashSet(status));
        Predicate<CouponUser> unionUserPredicate = Predicates.and(predicateUserStatus);
        List<CouponUser> filterCouponUser = Lists.newArrayList(Iterators.filter(couponUsers.iterator(), unionUserPredicate));

        List<CouponTemplate> userCoupon = Lists.newArrayList();
        Map<Long, CouponTemplate> couponTemplateMap = getTemplateMap(filterCouponTemplate);

        for (CouponUser couponUser : filterCouponUser) {
            if (couponTemplateMap.containsKey(couponUser.getCouponTemplateId())) {
                userCoupon.add(couponTemplateMap.get(couponUser.getCouponTemplateId()));
            }
        }

        // 3. 按赠送金额排序
        Comparator<CouponTemplate> userComparator = Ordering.from(new priceComparator()).reversed();
        Collections.sort(userCoupon, userComparator);

        // 4. 转化为前端要的vo
        List<CouponVO> couponVOs = convert2CouponVO(userCoupon);

        // 5. 置为已选择
        if (CollectionUtils.isEmpty(couponVOs)) return Lists.newArrayList();
        couponVOs.get(0).setSelected(true);

        return couponVOs;
    }

    /* 礼券模版弄成map */
    private Map<Long, CouponTemplate> getTemplateMap(List<CouponTemplate> couponTemplates) {
        Map<Long, CouponTemplate> map = Maps.newConcurrentMap();

        if (couponTemplates == null) return map;

        for (CouponTemplate couponTemplate : couponTemplates) {
            map.put(couponTemplate.getId(), couponTemplate);
        }

        return map;
    }

    /* 得到指定状态的所有礼券模版 */
    private List<CouponTemplate> getTemplateList() {
        Condition condition = new Condition(CouponTemplate.class);

        condition.createCriteria().andNotEqualTo("status", CouponVO.Status.DEL.getCode());

        return couponTemplateManager.findByCondition(condition);
    }


//    private Map<Long, CouponUser> getCouponUserMap(long userId) {
//        List<CouponUser> couponUsers = getRelation(userId);
//        Map<Long, CouponUser> map = Maps.newConcurrentMap();
//
//        if (couponUsers == null) return map;
//
//        for (CouponUser couponUser : couponUsers) {
//            map.put(couponUser.getCouponTemplateId(), couponUser);
//        }
//
//        return map;
//    }

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
            CouponVO couponVO = new CouponVO();

            BeanUtils.copyProperties(couponTemplate, couponVO);
            NumberFormat numberFormat = NumberFormat.getNumberInstance();

            numberFormat.setGroupingUsed(false);
            numberFormat.setMaximumFractionDigits(2);

            String price = numberFormat.format(couponTemplate.getPrice() / 100f);
            String rulePrice = numberFormat.format(couponTemplate.getRulePrice() / 100f);
            couponVO.setPrice(price);
            couponVO.setRulePrice(rulePrice);

            couponVOs.add(couponVO);
        }

        return couponVOs;
    }
}

class priceComparator implements Comparator<CouponTemplate> {
    @Override
    public int compare(CouponTemplate o1, CouponTemplate o2) {
        return o1.getPrice() > o2.getPrice() ? 1 : (o1.getPrice() == o2.getPrice() ? 0 : -1);
    }
}

