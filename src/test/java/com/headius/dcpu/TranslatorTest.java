package com.headius.dcpu;

import static junit.framework.Assert.*;

import com.headius.dcpu.instrs.SET;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TranslatorTest {
    private Translator t = new Translator();

    @Test
    public void testDecodeSet() {
        assertInstructions(
                new int[]{0x7c01, 0x0030},
                new SET(Constants.Location.A_REG, 0, Constants.Location.NEXT, 0x30));

        assertInstructions(
                new int[]{0x7de1, 0x1000, 0x0020},
                new SET(Constants.Location.NEXT_MEM, 0x1000, Constants.Location.NEXT, 0x20));
    }

    @Test
    public void testEncodeSet() {
        assertWords(
                new SET(Constants.Location.A_REG, 0, Constants.Location.NEXT, 0x30),
                new int[]{0x7c01, 0x0030});

        assertWords(
                new SET(Constants.Location.NEXT_MEM, 0x1000, Constants.Location.NEXT, 0x20),
                new int[]{0x7de1, 0x1000, 0x0020});
    }

    private void assertInstructions(int[] words, Instruction... expected) {
        List<Instruction> instructions = t.decode(words);
        for (int i = 0; i < expected.length; i++) {
            Instruction actual = instructions.get(i);
            assertEquals("\"" + actual + "\" != \"" + expected[i] + "\" at offset " + i + " in:\n" + instructions,
                    expected[i], actual);
        }
    }

    private void assertWords(Instruction given, int[] instrs) {
        int[] givenWords = given.encode();
        assertTrue("\"" + given + "\" decoded to " + wordsToString(givenWords) + " not " + wordsToString(instrs),
                Arrays.equals(givenWords, instrs));
    }

    private String wordsToString(int[] words) {
        StringBuilder sb = new StringBuilder();

        sb.append('[');

        boolean comma = false;
        for (int word : words) {
            if (comma) sb.append(',');
            comma = true;
            sb.append(Integer.toHexString(word));
        }

        sb.append(']');

        return sb.toString();
    }
}
