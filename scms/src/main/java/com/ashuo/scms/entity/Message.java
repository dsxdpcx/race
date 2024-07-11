package com.ashuo.scms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Message implements Serializable {

        private static final long serialVersionUID = 1L;

        private Integer messageId;

        private Integer userId;

        private String message;

        @JsonIgnore
        @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "GMT+8")
        private LocalDateTime createTime;

        private Integer isRead;
}
