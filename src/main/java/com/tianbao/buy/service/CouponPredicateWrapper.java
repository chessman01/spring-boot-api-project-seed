package com.tianbao.buy.service;

import com.google.common.base.Predicate;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Set;

public class CouponPredicateWrapper {
    public static Predicate<CouponUser> getPredicate4CouponUserStatus(final Set<Byte> set) {
        return new Predicate<CouponUser> () {
            @Override
            public boolean apply(CouponUser couponUser) {
                if (couponUser == null) {
                    return false;
                }

                if (set.contains(couponUser.getStatus())) {
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

    public static Predicate<CouponTemplate> getPredicate4TemplateStatus(final Set<Byte> set) {
        return new Predicate<CouponTemplate> () {
            @Override
            public boolean apply(CouponTemplate couponTemplate) {
                if (couponTemplate == null) {
                    return false;
                }

                if (set.contains(couponTemplate.getStatus())) {
                    return true;
                }

                return false;
            }

            @Override
            public boolean test(@Nullable CouponTemplate input) {
                return apply(input);
            }
        };
    }

    public static Predicate<CouponTemplate> getPredicate4TemplatePayType(final Set<Byte> set) {
        return new Predicate<CouponTemplate> () {
            @Override
            public boolean apply(CouponTemplate couponTemplate) {
                if (couponTemplate == null) {
                    return false;
                }

                if (set.contains(couponTemplate.getPayType())) {
                    return true;
                }

                return false;
            }

            @Override
            public boolean test(@Nullable CouponTemplate input) {
                return apply(input);
            }
        };
    }

    public static Predicate<CouponTemplate> getPredicate4TemplatePrice(final int rulePrice) {
        return new Predicate<CouponTemplate> () {
            @Override
            public boolean apply(CouponTemplate couponTemplate) {
                if (couponTemplate == null) {
                    return false;
                }

                if (couponTemplate.getRulePrice() <= rulePrice) {
                    return true;
                }

                return false;
            }

            @Override
            public boolean test(@Nullable CouponTemplate input) {
                return apply(input);
            }
        };
    }

    public static Predicate<CouponTemplate> getPredicate4TemplateTime(final Date date) {
        return new Predicate<CouponTemplate> () {
            @Override
            public boolean apply(CouponTemplate couponTemplate) {
                if (couponTemplate == null) {
                    return false;
                }

                if (date.before(couponTemplate.getStartTime()) || date.after(couponTemplate.getEndTime())) {
                    return false;
                }

                return true;
            }

            @Override
            public boolean test(@Nullable CouponTemplate input) {
                return apply(input);
            }
        };
    }
}
