package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.domain.YenCare;
import com.tianbao.buy.manager.CouponTemplateManager;
import com.tianbao.buy.manager.CouponUserManager;
import com.tianbao.buy.manager.YenCareManager;
import com.tianbao.buy.service.BaseService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.YenCareService;
import com.tianbao.buy.vo.CouponVO;
import com.tianbao.buy.vo.YenCareVO;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

@Service
public class YenCareServiceImpl extends BaseService implements YenCareService{
    @Resource
    private YenCareManager yenCareManager;

    @Resource
    private UserService userService;

    @Override
    public List<YenCareVO> getAllByUser() {
        User user = userService.getUserByWxUnionId();
        List<YenCare> yenCares = getCareByUser(user.getId());

        return convert2CareVO(yenCares);
    }

    @Override
    public YenCareVO build(long cardId) {
//        // 1. 找到用户的瘾卡
//        User user = userService.getUserByWxUnionId();
//
//        YenCareVO yenCareVO = getYenCare(user.getId(), cardId);
//
//        // 2. 找用户的礼券
//        Condition condition = new Condition(CouponUser.class);
//        condition.createCriteria().andCondition("user_id=", userId).andCondition("status=", 1);
//
//        List<CouponUser> couponUsers = couponUserManager.findByCondition(condition);
//
//        // 3. 找礼券模版
//        condition = new Condition(CouponTemplate.class);
//        condition.orderBy("rulePrice");
//        condition.createCriteria().andCondition("status=", 1)
//                .andCondition("pay_type=", 1).andCondition("rule=", 1);
//
//        List<CouponTemplate> couponTemplates = couponTemplateManager.findByCondition(condition);
//
//        List<CouponTemplate> effectCouponTemplates = Lists.newArrayList();
//        Date current = new Date();
//
//        for (CouponTemplate couponTemplate : couponTemplates) {
//            if (couponTemplate.getStartTime().after(current) || couponTemplate.getEndTime().before(current)) continue;
//
//            for (CouponUser couponUser : couponUsers) {
//                if (couponTemplate.getId().equals(couponUser.getCouponTemplateId())) {
//                    effectCouponTemplates.add(couponTemplate);
//                }
//            }
//        }
//
//        List<CouponVO> couponVOs = convert2CouponVO(effectCouponTemplates);
//        yenCareVO.setCouponVOs(couponVOs);
//
//        return yenCareVO;
        return null;
    }

    @Override
    public String create(long cardId, long rechargeId, long couponId) {
        // 1. 找到用户的瘾卡
//        Long userId = getUserByWxUnionId().getId();
//
//        YenCareVO yenCareVO = getYenCare(userId, cardId);

        // 2. 找礼券

        return null;
    }

    @Override
    public YenCareVO adjust(long cardId, long rechargeId, long couponId) {
        return null;
    }

    private YenCareVO getYenCare(long userId, long cardId){
        List<YenCare> yenCares = getCareByUser(userId);
        YenCareVO yenCareVO = null;

        for (YenCare yenCare : yenCares) {
            if (yenCare.getId().equals(cardId)) {
                yenCareVO = convert2CareVO(yenCare);
            }
        }

        if (yenCareVO == null) throw new BizException("您名下未找到指定的瘾卡");

        return yenCareVO;
    }

    public List<YenCare> getCareByUser(long userId) {
        Condition condition = new Condition(YenCare.class);
        condition.orderBy("type"); // 统一按卡类型排序

        condition.createCriteria().andCondition("user_id=", userId)
                .andCondition("status=", YenCareVO.Status.NORMAL.getCode());

        return yenCareManager.findByCondition(condition);
    }

    private YenCareVO convert2CareVO(YenCare yenCare) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(2);

        String gift = numberFormat.format(yenCare.getGiftAccount() / 100f);
        String cash = numberFormat.format(yenCare.getCashAccount() / 100f);
        String total = numberFormat.format((yenCare.getGiftAccount() + yenCare.getCashAccount()) / 100f);

        numberFormat.setMaximumFractionDigits(1);
        String discount = String.format("消费立打%s折", numberFormat.format(yenCare.getDiscountRate() / 10f));

        return new YenCareVO(yenCare.getId(), "http://gw.alicdn.com/tps/TB1LNMxPXXXXXbhaXXXXXXXXXXX-183-129.png",
                gift, cash, total,
                discount, "http://xxx.xxx.com/recharge.xx", null);
    }

    private List<YenCareVO> convert2CareVO(List<YenCare> yenCares) {
        List<YenCareVO> voList = Lists.newArrayList();

        for (YenCare yenCare : yenCares) {
            voList.add(convert2CareVO(yenCare));
        }

        return voList;
    }
}
