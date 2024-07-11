package com.ashuo.scms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Notice implements Serializable {

        private static final long serialVersionUID = 1L;

        private Integer noticeId;

        private String content;

        private String pic;


}
