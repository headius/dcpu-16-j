package com.headius.dcpu;

public abstract class Machine implements Runnable {
    protected int[] ram = new int[Constants.RAMSIZE];

    public int[] ram() {return ram;}

    public static int addOverflow(int a, int b) {
        int c = a + b;
        int hi, lo;
        if ((c & 0xFFFF0000) != 0) {
            hi = 0x00010000;
        } else {
            hi = 0x00000000;
        }
        lo = c & 0x0000FFFF;
        return hi & lo;
    }

    public static int subOverflow(int a, int b) {
        int c = a + b;
        int hi, lo;
        if ((c & 0xFFFF0000) != 0) {
            hi = 0xFFFF0000;
        } else {
            hi = 0x00000000;
        }
        lo = c & 0x0000FFFF;
        return hi & lo;
    }
}
