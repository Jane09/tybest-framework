package com.tybest.base.test;

public class SortTest {

    public static void main(String[] args) {
        int[] a = {314,298,508,123,486,145};
        two();
        System.out.println(Math.sqrt(4)+1);
        System.out.println(isPrime2(14));
        decrypt();
    }

    //809*??=800*??+9*??+1
    public static void two() {
        for(int i=10;i<=99;i++) {
            if(809*i == (800*i + 9*i + 1)) {
                System.out.println("i="+i+"; 809*?? = "+809*i);
            }
        }
    }

    //一个偶数总能表示为两个素数之和。
    public void sum(int i) {

    }



    //判断是否为素数（质数） 大于1的自然数中，除了1和它本身以外不再有其他因数
    public static boolean isPrime(int m) {
        boolean flag = true;
        for(int i=2;i<Math.sqrt(m)+1;i++){
            if(m%i==0){
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isPrime2(int m) {
        if(m == 2) {
            return true;
        }
        for(int i=2; i<m; i++) {
            if(m % i == 0) {
                return false;
            }
        }
        return true;
    }


    public static void decrypt() {
        int n = 1234;
        int[] a = new int[4];
        for(int i=3;i>=0;i--){
            a[i] = n%10;
            n /= 10;
        }
        for(int i=0;i<4;i++)
            System.out.print(a[i]);
        System.out.println();
        for(int i=0;i<a.length;i++){
            a[i] += 5;
            a[i] %= 10;
        }
        int temp1 = a[0];
        a[0] = a[3];
        a[3] = temp1;
        int temp2 = a[1];
        a[1] = a[2];
        a[2] = temp2;
        for(int i=0;i<a.length;i++)
            System.out.print(a[i]);
    }
}
