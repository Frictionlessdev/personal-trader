package com.sb.projects.trader.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalPercentage {

    public static BigDecimal toPercentageOf(BigDecimal value, BigDecimal total) {
        return value.divide(total, 4, RoundingMode.HALF_UP).scaleByPowerOfTen(2);
    }

    public static BigDecimal percentOf(BigDecimal percentage, BigDecimal total) {
        return percentage.multiply(total).scaleByPowerOfTen(-2);
    }
}
