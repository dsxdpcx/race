package com.ashuo.scms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Node implements Serializable {

    private Integer id;
    private Player player1;
    private Player player2;
    private List<Node> children;
}
