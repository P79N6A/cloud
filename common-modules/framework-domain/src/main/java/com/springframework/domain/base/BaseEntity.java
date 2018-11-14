package com.springframework.domain.base;

import com.baomidou.mybatisplus.annotation.*;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import lombok.Data;

import java.util.Date;

/**
 * @author summer
 */
@Data
public abstract class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;
    /**
     * 逻辑删除 1删除 0未删除
     */
    @TableField("is_deleted")
    @TableLogic("0")
    protected Integer isDeleted = 0;

    @TableField(value = "created_by", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.INSERT)
    private String createdBy;
    @TableField(value = "created_time", update = "now()", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.INSERT)
    private Date createdTime;
    @TableField(value = "last_modified_by", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.UPDATE)
    private String lastModifiedBy;
    @TableField(value = "last_modified_time", update = "now()", strategy = FieldStrategy.NOT_NULL, fill = FieldFill.UPDATE)
    private Date lastModifiedTime;


    protected ToStringHelper toStringHelper() {
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
}
