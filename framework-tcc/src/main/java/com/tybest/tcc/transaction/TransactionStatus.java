package com.tybest.tcc.transaction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionStatus {

    TRYING(1),
    CONFIRMING(2),
    CANCELLING(3);

    private int value;
    
    public static TransactionStatus valueOf(int value) {
        switch (value) {
            case 1:
                return TRYING;
            case 2:
                return CONFIRMING;
            default:
                return CANCELLING;
        }
    }
}
