package com.tybest.tcc.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tb
 * @date 2018/12/10 17:19
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InvocationContext implements Serializable {

    private Class targetClass;

    private String methodName;

    private Class[] parameterTypes;

    private Object[] args;
}
