package com.tianbao.buy.utils;

import java.text.NumberFormat;

public class MoneyUtils {
    public static String format(int maximumFraction, double data) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(maximumFraction);

        return numberFormat.format(data);
    }

    public static String minusUnitFormat(int maximumFraction, double data) {
        return "-¥" + format(maximumFraction, data);
    }

    public static String unitFormat(int maximumFraction, double data) {
        return "¥" + format(maximumFraction, data);
    }
}
