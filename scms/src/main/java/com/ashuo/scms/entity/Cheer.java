package com.ashuo.scms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cheer")
public class Cheer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cheer_id", type = IdType.AUTO)
    private Integer cheerId;
    private String userId;
    private String content;
//   //private Integer shenhe;
//
//    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime cheershenheTime;
//    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime cheersubmitTime;
}
