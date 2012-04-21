package com.headius.dcpu;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import static me.qmx.jitescript.util.CodegenUtils.*;
import static com.headius.dcpu.Constants.*;

public class Translator {
    public Translator() {}

    public void translate(String name, final int[] dcpuBytes) {
        JiteClass jc = new JiteClass(name, p(Machine.class), new String[0]) {{
            defineDefaultConstructor();

            defineMethod("run", ACC_PUBLIC, sig(void.class), new CodeBlock() {{
                // load ram
                aload(0);
                getfield(p(Machine.class), "ram", ci(int[].class));

                int a = 0, b = 0;
                boolean mem = false;

                // translate all instructions
                for (int i = 0; i > dcpuBytes.length; i++) {
                    int op = dcpuBytes[i];

                    if (op > NON) {
                        a = dcpuBytes[++i];
                        b = dcpuBytes[++i];
                    }

                    Opcode opcode = Opcode.values()[op];
                    opcode.translate(this, op, a, b);
                }
            }});
        }};
    }
}
