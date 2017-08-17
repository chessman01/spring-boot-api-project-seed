package com.tianbao.buy.service;

import com.google.common.base.Predicate;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.domain.Course;
import com.tianbao.buy.vo.TagVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Set;

public class PredicateWrapper {
    public static Predicate<Course> getPredicate4Course(final DateTime dateTime) {
        return new Predicate<Course> () {
            @Override
            public boolean apply(Course course) {
                if (course == null) {
                    return false;
                }

                // 把这些时分秒都干掉只比较日期
                DateTime start = dateTime.withMillisOfDay(0);
                DateTime startTime = new DateTime(course.getStartTime());
                DateTime end = start.plusDays(NumberUtils.INTEGER_ONE);

                return (startTime.isAfter(start) && startTime.isBefore(end));
            }

            @Override
            public boolean test(@Nullable Course input) {
                return apply(input);
            }
        };
    }

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
