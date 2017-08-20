package com.tianbao.buy.utils;

import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.vo.CouponVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class DateUtils {
    public static DateTime getStart(DateTime start) {
        if (start == null) start = new DateTime();

        return start.withMillisOfDay(0);
    }

    public static DateTime getStart() {
        return getStart(null);
    }

    public static DateTime getEnd(DateTime start, byte validityUnit, int validityValue) {
        switch(validityUnit) {
            case 3:
                return start.plusMonths(validityValue).minusSeconds(NumberUtils.INTEGER_ONE);
            case 2:
                return start.plusWeeks(validityValue).minusSeconds(NumberUtils.INTEGER_ONE);
            case 1:
                return start.plusDays(validityValue).minusSeconds(NumberUtils.INTEGER_ONE);
            default:
                throw new BizException("system error.");
        }
    }

    public static DateTime plusDays(int num, DateTime current) {
        return current.plusDays(num);
    }

    public static String getDayOfWeek(DateTime dt, DateTime current) {
        Period p = new Period(current, dt, PeriodType.days());

        int days = p.getDays();

        //星期
        switch(days) {
            case 0:
                return "今天";
            case 1:
                return "明天";
            default:
                switch(dt.getDayOfWeek()) {
                    case DateTimeConstants.SUNDAY:
                        return "周日";
                    case DateTimeConstants.MONDAY:
                        return "周一";
                    case DateTimeConstants.TUESDAY:
                        return "周二";
                    case DateTimeConstants.WEDNESDAY:
                        return "周三";
                    case DateTimeConstants.THURSDAY:
                        return "周四";
                    case DateTimeConstants.FRIDAY:
                        return "周五";
                    case DateTimeConstants.SATURDAY:
                        return "周六";
                    default:
                        return "无效";
                }
        }
    }

    public static boolean isEqual(DateTime d1, DateTime d2) {
        Period p = new Period(d1, d2, PeriodType.days());
        return p.getDays() == NumberUtils.INTEGER_ZERO;
    }

    public static DateTime yearMonthDayFormat(String yearMonthDay) {
        return new DateTime(yearMonthDay);
    }

    public static String yearMonthDayFormat(DateTime dt) {
        return dt.toString("yyyy-MM-dd");
    }

    public static String monthDayFormat(DateTime dt) {
        return dt.toString("MM.dd");
    }

    public static String hourMinuteFormat(DateTime dt) {
        return dt.toString("HH.mm");
    }

    public static String getDayOfWeek (DateTime dt) {
        //星期
        switch(dt.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                return "日";
            case DateTimeConstants.MONDAY:
                return "一";
            case DateTimeConstants.TUESDAY:
                return "二";
            case DateTimeConstants.WEDNESDAY:
                return "三";
            case DateTimeConstants.THURSDAY:
                return "四";
            case DateTimeConstants.FRIDAY:
                return "五";
            case DateTimeConstants.SATURDAY:
                return "六";
            default:
                return "无效";
        }
    }
}
