package com.tybest.base;

/**
 * @author tb
 * @date 2018/12/3 14:53
 */
public class EnumDemo {


    public static void main(String[] args) {
        System.out.println(Clazz.insuranceProducts+"hell");
    }

    static enum Clazz {
        insuranceProducts,
        bankArea,
        sex,
        maritalStatus,
    }
}
