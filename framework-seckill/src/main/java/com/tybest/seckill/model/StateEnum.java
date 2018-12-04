package com.tybest.seckill.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StateEnum {
    MUCH(2, "人太多，请稍后!"),
    SUCCESS(1,"秒杀成功"),
    FINISH(0,"秒杀结束"),
    REPEAT(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATE_REWRITE(-3,"数据被篡改")
    ;
    private int state;
    private String info;


    public static StateEnum stateOf(int index) {
        for (StateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
