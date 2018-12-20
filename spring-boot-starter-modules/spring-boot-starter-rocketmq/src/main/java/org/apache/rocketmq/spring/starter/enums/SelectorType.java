package org.apache.rocketmq.spring.starter.enums;

import org.apache.rocketmq.common.filter.ExpressionType;
/**
 * @author summer
 */
public enum SelectorType {

    /**
     * @see ExpressionType#TAG
     */
    TAG,

    /**
     * @see ExpressionType#SQL92
     */
    SQL92
}