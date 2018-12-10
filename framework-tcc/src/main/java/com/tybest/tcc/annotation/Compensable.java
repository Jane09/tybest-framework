package com.tybest.tcc.annotation;

import com.tybest.tcc.transaction.DefaultTransactionContextEditor;
import com.tybest.tcc.transaction.Propagation;
import com.tybest.tcc.transaction.TransactionContextEditor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可补偿的
 * @author tb
 * @date 2018/12/7 15:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Compensable {

    /**
     * 事务传播机制
     * @return 事务传播
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * 确认方法
     * @return 方法名
     */
    String confirmMethod() default "";

    /**
     * 撤销方法
     * @return 方法名
     */
    String cancelMethod() default "";

    /**
     * 是否异步确认
     * @return true/false
     */
    boolean asyncConfirm() default false;

    /**
     * 是否异步撤销
     * @return true/false
     */
    boolean asyncCancel() default false;

    /**
     * 事务上下文编辑器
     * @return TransactionContextEditor
     */
    Class<? extends TransactionContextEditor> transactionContextEditor() default DefaultTransactionContextEditor.class;

}
