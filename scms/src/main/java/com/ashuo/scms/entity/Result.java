package com.ashuo.scms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal HeatsTime;
    private BigDecimal SemifinalsTime;
    private BigDecimal FinalsTime;
    private Integer heatsGroupNumber;
    private Integer semifinalsGroupNumber;
    private Integer finalsGroupNumber;
    private  Integer AthleteId;
}
