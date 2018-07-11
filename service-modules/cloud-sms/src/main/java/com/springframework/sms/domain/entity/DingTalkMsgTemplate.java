package com.springframework.sms.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

/**
 * @author summer
 * 2018/7/10
 */
@Data
@ToString
@TableName("t_ding_talk_msg")
public class DingTalkMsgTemplate {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String context;
}
