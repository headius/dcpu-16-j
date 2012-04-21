package com.headius.dcpu;

public abstract class Machine implements Runnable {
    private int[] ram = new int[Constants.RAMSIZE];

    public int[] ram() {return ram;}
}
