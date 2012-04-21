package com.headius.dcpu;

import com.headius.dcpu.instrs.SET;
import me.qmx.jitescript.CodeBlock;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public abstract class Instruction {
//        NON {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//
//            }
//        },
//        ADD {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, a);
//                load(cb, b);
//                cb.invokestatic(p(Machine.class), "addOverflow", sig(int.class, int.class));
//                cb.dup();
//                cb.pushInt(16);
//                cb.ishr();
//                store(cb, Register.O.lvar());
//                cb.pushInt(0x0000FFFF);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        SUB {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, a);
//                load(cb, b);
//                cb.invokestatic(p(Machine.class), "addOverflow", sig(int.class, int.class));
//                cb.dup();
//                cb.pushInt(16);
//                cb.ishr();
//                store(cb, Register.O.lvar());
//                cb.pushInt(0x0000FFFF);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        MUL {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.imul();
//                setOverflow(cb);
//                cb.pushInt(0x0000FFFF);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        DIV {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.idiv();
//                setOverflow(cb);
//                cb.pushInt(0x0000FFFF);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        MOD {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.irem();
//                setOverflow(cb);
//                cb.pushInt(0x0000FFFF);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        SHL {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.ishl();
//                setOverflow(cb);
//                cb.pushInt(0x0000FFFF);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        SHR {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.ishl();
//                setOverflow(cb);
//                cb.pushInt(0x0000FFFF);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        AND {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.iand();
//                store(cb, a);
//            }
//        },
//        BOR {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.ior();
//                store(cb, a);
//            }
//        },
//        XOR {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//                load(cb, b);
//                cb.ixor();
//                store(cb, a);
//            }
//        },
//        IFE {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//
//            }
//        },
//        IFG {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//
//            }
//        },
//        IFB {
//            public void translate(CodeBlock cb, int op, int a, int b) {
//
//            }
//        };

    public static Instruction decode(int[] dcpuWords, int[] i) {
        int instrWord = dcpuWords[i[0]];

        Constants.Location target = Constants.Location.values()[instrWord >>> 10];
        int tvalue = 0;
        Constants.Location source = Constants.Location.values()[(instrWord >>> 4) & 0x3F];
        int svalue = 0;
        Constants.Opcode opcode = Constants.Opcode.values()[instrWord & 0xF];

        if (source.base() == Constants.Base.NEXT) {
            svalue = dcpuWords[++i[0]];
        }

        if (target.base() == Constants.Base.NEXT) {
            tvalue = dcpuWords[++i[0]];
        }

        switch (opcode) {
            case SET:
                return new SET(source, svalue, target, tvalue);
        }
        return null;
    }

    private static void setOverflow(CodeBlock cb) {
        cb.dup();
        cb.invokestatic(p(Machine.class), "overflow", sig(boolean.class, int.class));
        cb.istore(Constants.Base.O.ordinal());
    }

    protected void load(CodeBlock cb, int value) {
        switch (source.modifier()) {
            case NONE:
                switch (source.base()) {
                    case NEXT:
                        cb.pushInt(value);
                        break;
                    default:
                        cb.iload(source.base().lvar());
                }
                break;
            case MEM:
                cb.aload(Constants.Base.MEM.lvar());
                switch (source.base()) {
                    case NEXT:
                        cb.pushInt(value);
                        break;
                    default:
                        cb.iload(source.base().lvar());
                }
                cb.iaload();
                break;
            case NEXT:
                // todo
        }
    }

    protected void store(CodeBlock cb, int value) {
        switch (target.modifier()) {
            case NONE:
                switch (target.base()) {
                    case NEXT:
                        throw new ClassFormatError(); // writing to literal
                    default:
                        cb.istore(target.base().lvar());
                }
                break;
            case MEM:
                cb.aload(Constants.Base.MEM.lvar());
                switch (target.base()) {
                    case NEXT:
                        cb.pushInt(value);
                        break;
                    default:
                        cb.iload(target.base().lvar());
                }
                cb.dup2_x1();
                cb.pop2();
                cb.iastore();
                break;
            case NEXT:
                // todo
        }
    }
    public Instruction(Constants.Opcode opcode, Constants.Location target, int tvalue, Constants.Location source, int svalue) {
        this.opcode = opcode;
        this.target = target;
        this.tvalue = tvalue;
        this.source = source;
        this.svalue = svalue;
    }

    public abstract void translate(CodeBlock cb);

    public Constants.Opcode opcode() { return opcode; }
    public Constants.Location target() { return target; }
    public int tvalue() { return tvalue; }
    public Constants.Location source() { return source; }
    public int svalue() { return svalue; }

    public String toString() {
        return opcode + " " + target.toString(tvalue) + " " + source.toString(svalue);
    }

    public int[] encode() {
        int instr = (source.ordinal() << 10) | (target.ordinal() << 4) | opcode.ordinal();

        boolean tnext = target.base() == Constants.Base.NEXT || target.modifier() == Constants.Modifier.NEXT;
        boolean snext = source.base() == Constants.Base.NEXT || source.modifier() == Constants.Modifier.NEXT;

        if (tnext) {
            if (snext) {
                return new int[]{instr, tvalue, svalue};
            } else {
                return new int[]{instr, tvalue};
            }
        } else {
            if (snext) {
                return new int[]{instr, svalue};
            } else {
                return new int[]{instr};
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof Instruction)) return false;

        Instruction otherInstruction = (Instruction)other;

        return opcode == otherInstruction.opcode &&
                target == otherInstruction.target &&
                tvalue == otherInstruction.tvalue &&
                source == otherInstruction.source &&
                svalue == otherInstruction.svalue;
    }

    protected final Constants.Opcode opcode;
    protected final Constants.Location target;
    protected final int tvalue;
    protected final Constants.Location source;
    protected final int svalue;
}
