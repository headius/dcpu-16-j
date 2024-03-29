package com.headius.dcpu;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import org.objectweb.asm.tree.LabelNode;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.*;

public class Translator {
    public Translator() {}

    public List<Instruction> decode(int[] dcpuWords) {
        ArrayList<Instruction> instructions = new ArrayList<Instruction>();

        // translate all instructions
        for (int i[] = new int[]{0}; i[0] < dcpuWords.length; i[0]++) {
            instructions.add(Instruction.decode(dcpuWords, i));
        }

        return instructions;
    }

    public int[] encode(List<Instruction> instructions) {
        int[] allWords = new int[instructions.size() * 3];
        int offset = 0;
        for (Instruction instruction : instructions) {
            int[] words = instruction.encode();
            for (int i: words) allWords[offset++] = i;
        }
        return Arrays.copyOfRange(allWords, 0, offset);
    }

    public Class translate(String name, final int[] dcpuWords) {
        JiteClass jc = new JiteClass(name, p(Machine.class), new String[0]) {{
            defineDefaultConstructor();

            defineMethod("run", ACC_PUBLIC, sig(void.class), new CodeBlock() {{
                // load ram
                aload(0);
                getfield(p(Machine.class), "ram", ci(int[].class));
                astore(Constants.Base.MEM.lvar());

                // initialize registers
                for (int i = Constants.LVAR_OFFSET; i < Constants.Base.NEXT.lvar(); i++) {
                    pushInt(0);
                    istore(i);
                }

                int a = 0, b = 0;
                boolean mem = false;

                // decode instructions
                List<Instruction> instructions = decode(dcpuWords);

                // labels for jumps
                LabelNode[] labels = new LabelNode[instructions.size()];
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = new LabelNode();
                }

                // translate instructions
                int pc = 0;
                for (Instruction instruction : instructions) {
                    label(labels[pc]);
                    pushInt(pc);
                    istore(Constants.Base.PC.lvar());
                    instruction.translate(this);
                }
                voidreturn();
            }});
        }};

        return new ClassLoader(Translator.class.getClassLoader()) {
            public Class defineClass(String name, byte[] bytes) {
                return super.defineClass(name, bytes, 0, bytes.length);
            }
        }.defineClass(name, jc.toBytes());
    }

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(args[0]);
            byte[] buf = new byte[1024];
            int len = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = fis.read(buf)) != 0) {
                baos.write(buf, 0, len);
            }
            int[] words = new int[baos.size() / 2];
            byte[] bytes = baos.toByteArray();
            for (int i = 0; i < bytes.length;) {
                int hi = (bytes[i++] & 0xFF) << 8;
                int lo = (bytes[i++] & 0xFF);
                words[i / 2 - 1] = hi & lo;
            }

            Class cls = new Translator().translate("blah", words);
            Runnable runnable = (Runnable)cls.newInstance();
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
