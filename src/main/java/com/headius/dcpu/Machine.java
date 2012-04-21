package com.headius.dcpu;

public abstract class Machine implements Runnable {
    private byte[] ram = new byte[Constants.RAMSIZE];
}
