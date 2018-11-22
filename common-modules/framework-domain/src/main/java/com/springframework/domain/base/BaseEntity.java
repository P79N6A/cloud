package com.springframework.domain.base;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author summer
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseEntity<T extends Model> extends Model<T> {

    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;
    /**
     * 逻辑删除 1删除 0未删除
     */
    @TableField("is_deleted")
    @TableLogic("0")
    protected Integer isDeleted = 0;

    @TableField(value = "created_by", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.INSERT)
    protected String createdBy;
    @TableField(value = "created_time", update = "now()", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.INSERT)
    protected Date createdTime;
    @TableField(value = "last_modified_by", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.UPDATE)
    protected String lastModifiedBy;
    @TableField(value = "last_modified_time", update = "now()", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.UPDATE)
    protected Date lastModifiedTime;


    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("created_by", createdBy)
                .add("created_time", createdTime)
                .add("last_modified_by", lastModifiedBy)
                .add("last_modified_time", lastModifiedTime);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    public static final String ID = "id";
    public static final String IS_DELETED = "is_deleted";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_TIME = "created_time";
    public static final String LAST_MODIFIED_BY = "last_modified_by";
    public static final String LAST_MODIFIED_TIME = "last_modified_time";
}
