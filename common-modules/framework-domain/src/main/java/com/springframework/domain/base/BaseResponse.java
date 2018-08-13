package com.springframework.domain.base;

import java.io.Serializable;

/**
 * @author summer
 * 2018/8/13
 */
public abstract class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -1291080555056535690L;

    public abstract Serializable realId();

    @Override
    public final int hashCode() {
        return (realId() == null) ? 0 : realId().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseVo other = (BaseVo) obj;
        return !(this.realId() != null ? !(this.realId().equals(other.realId())) : (other.realId() != null));
    }
}
