package com.project.simplegw.member.dtos.send;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtosDayoffCnt {
    private double usable;
    private double use;
}
