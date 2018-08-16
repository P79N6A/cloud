package com.springframework.enums.demo;

import com.springframework.enums.IntValueDescription;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author summer
 * 2018/8/16
 */
public enum ShopDeliveryEnum implements IntValueDescription {
    /**
     * 当日达自提
     */
    PICKSELF_TODAY(1, "当日达自提"),
    /**
     * 当日达配送
     */
    DELIVERY_TODAY(2, "当日达配送"),
    /**
     * 次日达自提
     */
    PICKSELF_NEXTDAY(4, "次日达自提"),
    /**
     * 次日达B2C
     */
    DELIVERY_NEXTDAY_B2C(8, "次日达B2C"),
    /**
     * 次日达B2B2C
     */
    DELIVERY_NEXTDAY_BBC(16, "次日达B2B2C"),
    /**
     * 点餐堂吃
     */
    PICKSELF_FOOD(32, "点餐堂吃"),
    /**
     * 点餐外卖
     */
    DELIVERY_FOOD(64, "点餐外卖");

    private static List<ShopDeliveryEnum> VALUES = EnumUtils.getEnumList(ShopDeliveryEnum.class);
    private final int value;
    private final String desc;

    ShopDeliveryEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ShopDeliveryEnum getShopDeliveryEnum(int value) {
        return Arrays
                .stream(ShopDeliveryEnum.values())
                .filter(o -> o.getValue() == value)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public int getValue() {
        return value;
    }

    /**
     * 在api-rest中使用，如果为true,设置门店为 次日达不支持自提.
     *
     * @param shopDeliveryType
     * @return 不支持次日达自提
     */
    public static boolean notSupportNextDayPickself(int shopDeliveryType) {
        return (shopDeliveryType & ShopDeliveryEnum.PICKSELF_NEXTDAY.getValue()) <= 0
                && (shopDeliveryType & ShopDeliveryEnum.PICKSELF_TODAY.getValue()) > 0
                && ((shopDeliveryType & ShopDeliveryEnum.DELIVERY_NEXTDAY_BBC.getValue()) > 0 || (shopDeliveryType & ShopDeliveryEnum.DELIVERY_NEXTDAY_B2C.getValue()) > 0);
    }

    /**
     * 在api-rest中使用，如果为true, 设置门店不支持自提
     *
     * @param shopDeliveryType
     * @return 不支持自提
     */
    public static boolean notSupportPickself(int shopDeliveryType) {
        return (shopDeliveryType & ShopDeliveryEnum.PICKSELF_NEXTDAY.getValue()) <= 0 && (shopDeliveryType & ShopDeliveryEnum.PICKSELF_TODAY.getValue()) <= 0;
    }

    /**
     * 在product-center中使用，如果为true, 支持堂食自提
     *
     * @param shopDeliveryType
     * @return 是否支持堂食自提
     */
    public static boolean supportPickselfFood(int shopDeliveryType) {
        return (shopDeliveryType & ShopDeliveryEnum.PICKSELF_FOOD.getValue()) > 0;
    }

    /**
     * 在product-center中使用，如果为true, 支持堂食配送
     *
     * @param shopDeliveryType
     * @return 是否支持堂食配送
     */
    public static boolean supportDeliveryFood(int shopDeliveryType) {
        return (shopDeliveryType & ShopDeliveryEnum.DELIVERY_FOOD.getValue()) > 0;
    }

}