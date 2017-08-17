package com.tianbao.buy.service;

import com.google.common.base.Predicate;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.vo.TagVO;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Set;

public class PredicateWrapper {
    public static Predicate<TagVO> getPredicate4Tag(final Set<Long> idSet) {
        return new Predicate<TagVO> () {
            @Override
            public boolean apply(TagVO tagVO) {
                if (tagVO == null) {
                    return false;
                }

                if (idSet.contains(tagVO.getId())) {
                    return true;
                }

                return false;
            }

            @Override
            public boolean test(@Nullable TagVO input) {
                return apply(input);
            }
        };
    }

    public static Predicate<CouponUser> getPredicate4CouponUser(final Set<Byte> statuSet) {
        return new Predicate<CouponUser> () {
            @Override
            public boolean apply(CouponUser couponUser) {
                if (couponUser == null) {
                    return false;
                }

                if (statuSet.contains(couponUser.getStatus())) {
                    return true;
                }

                return false;
            }

            @Override
            public boolean test(@Nullable CouponUser input) {
                return apply(input);
            }
        };
    }

    public static Predicate<CouponTemplate> getPredicate4Template(final Set<Byte> statusSet,
                                                                  final Set<Byte> payTypeSet,
                                                                  final Integer rulePrice,
                                                                  final Date currentTime) {
        return new Predicate<CouponTemplate> () {
            @Override
            public boolean apply(CouponTemplate couponTemplate) {
                boolean result = true;

                if (couponTemplate == null) {
                    return false;
                }

                if (statusSet != null && !statusSet.contains(couponTemplate.getStatus())) {
                    result = false;
                }

                if (payTypeSet != null && !payTypeSet.contains(couponTemplate.getPayType())) {
                    result = false;
                }

                if (rulePrice != null && couponTemplate.getRulePrice() > rulePrice) {
                    return false;
                }

                if (currentTime.getTime() < couponTemplate.getStartTime().getTime()
                        || currentTime.getTime() > couponTemplate.getEndTime().getTime()) {
                    return false;
                }

                return result;
            }

            @Override
            public boolean test(@Nullable CouponTemplate input) {
                return apply(input);
            }
        };
    }
}
