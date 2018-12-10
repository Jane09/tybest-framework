package com.tybest.tcc.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tb
 */
@AllArgsConstructor
public enum TransactionType {

    ROOT(1),
    BRANCH(2);

    @Getter
    private int type;

    public static TransactionType valueOf(int type) {
        switch (type) {
            case 1:
                return ROOT;
            case 2:
                return BRANCH;
            default:
                return null;
        }
    }
}
