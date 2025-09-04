package ru.yandex.practicum.commerce.interactionapi;

import java.math.BigDecimal;

public class Util {
    public static final String X_REQUEST_ID_HEADER = "x-request-id";
    public static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    public static final int ADDRESS_COEFFICIENT1 = 1;
    public static final int ADDRESS_COEFFICIENT2 = 2;
    public static final BigDecimal FRAGILE_COEFFICIENT = BigDecimal.valueOf(0.2);
    public static final BigDecimal WEIGHT_COEFFICIENT = BigDecimal.valueOf(0.3);
    public static final BigDecimal VOLUME_COEFFICIENT = BigDecimal.valueOf(0.2);
    public static final BigDecimal ADDRESS_TO_COEFFICIENT = BigDecimal.valueOf(0.2);
    public static final BigDecimal baseCostDelivery = BigDecimal.valueOf(5.0);
    public static final BigDecimal PERCENT_FEE = BigDecimal.valueOf(0.1);

}
