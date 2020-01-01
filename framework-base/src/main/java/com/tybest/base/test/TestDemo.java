package com.tybest.base.test;

public class TestDemo {

    public static void main(String[] args) {
        System.out.println("12"+null);
        int i = 0xFFFFFFF1;
        System.out.println(i);
        int j = ~i;
        System.out.println(j);
        System.out.println(~5);

        int a=10,b=4,c=20,d=6;
        System.out.println(a++*b+c*--d);
        //10*4+20*5
    }


    public String myOverLoad(int i, String str) {
        return null;
    }

    public String myOverLoad(String str, int i) {
        return null;
    }

//    public void myOverLoad(int i, String str) {
//        return null;
//    }
}
