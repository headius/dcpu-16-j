package com.headius.dcpu;

import org.junit.Test;

public class MachineTest {
    @Test
    public void testSet() throws Exception {
        int[] words = {
                0x7c01, 0x0030,
                0x7de1, 0x1000, 0x0020
        };
        Machine m = (Machine)new Translator().translate("testSet", words).newInstance();
        m.run();
    }
}
