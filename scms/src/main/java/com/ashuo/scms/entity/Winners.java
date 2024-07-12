package com.ashuo.scms.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)

public class Winners implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer winnersId;
    private Integer userId;
    private Integer itemId;
    private String winnersName;
    private String winnersRank;

}
