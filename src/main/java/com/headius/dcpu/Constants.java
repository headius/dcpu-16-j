package com.headius.dcpu;

import me.qmx.jitescript.CodeBlock;

public class Constants {
    public static final int RAMSIZE = 0x10000;
    public static final int LVAR_OFFSET = 1;

    public enum Register {
        A, B, C, X, Y, Z, I, J, SP, PC, O, NONE;

        public int lvar() {return ordinal() + LVAR_OFFSET;}
    };
    public enum Deref {NONE, MEM, NEXT}

    public enum Location {
        A_REG(Register.A, Deref.NONE),
        B_REG(Register.B, Deref.NONE),
        C_REG(Register.C, Deref.NONE),
        X_REG(Register.X, Deref.NONE),
        Y_REG(Register.Y, Deref.NONE),
        Z_REG(Register.Z, Deref.NONE),
        I_REG(Register.I, Deref.NONE),
        J_REG(Register.J, Deref.NONE),
        A_MEM(Register.A, Deref.MEM),
        B_MEM(Register.B, Deref.MEM),
        C_MEM(Register.C, Deref.MEM),
        X_MEM(Register.X, Deref.MEM),
        Y_MEM(Register.Y, Deref.MEM),
        Z_MEM(Register.Z, Deref.MEM),
        I_MEM(Register.I, Deref.MEM),
        J_MEM(Register.J, Deref.MEM),
        A_NEXT(Register.A, Deref.NEXT),
        B_NEXT(Register.B, Deref.NEXT),
        C_NEXT(Register.C, Deref.NEXT),
        X_NEXT(Register.X, Deref.NEXT),
        Y_NEXT(Register.Y, Deref.NEXT),
        Z_NEXT(Register.Z, Deref.NEXT),
        I_NEXT(Register.I, Deref.NEXT),
        J_NEXT(Register.J, Deref.NEXT),
        MEM(Register.NONE, Deref.MEM),
        VAL(Register.NONE, Deref.NONE),
        SP(Register.SP, Deref.NONE),
        PC(Register.PC, Deref.NONE),
        O(Register.O, Deref.NONE);

        public static Location decode(int location) {
            if (location < MEM.ordinal()) {
                if (location == 0x1b) return SP;
                if (location == 0x1c) return PC;
                if (location == 0x1d) return O;
                return values()[location];
            }
            return MEM;
        }

        public Register register() {return register;}
        public Deref deref() {return deref;}

        private final Register register;
        private final Deref deref;
        private Location(Register register, Deref deref) {
            this.register = register;
            this.deref = deref;
        }
    }

    public enum Opcode {
        NON {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        SET {
            public void translate(CodeBlock cb, int op, int a, int b) {
                load(cb, b);
                store(cb, a);
            }
        },
        ADD {
            public void translate(CodeBlock cb, int op, int a, int b) {
                load(cb, b);
                cb.iadd();
                cb.dup();
                cb.pushInt(8);
                cb.ishr();
                cb.istore(Register.O.ordinal());
                store(cb, a);
            }
        },
        SUB {
            public void translate(CodeBlock cb, int op, int a, int b) {
                load(cb, b);
                cb.iadd();
                cb.dup();
                cb.pushInt(8);
                cb.ishr();
                cb.istore(Register.O.lvar());
                store(cb, a);
            }
        },
        MUL {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        DIV {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        MOD {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        SHL {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        SHR {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        AND {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        BOR {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        XOR {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        IFE {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        IFG {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        },
        IFB {
            public void translate(CodeBlock cb, int op, int a, int b) {

            }
        };

        private static void load(CodeBlock cb, int loc) {
            Location location = Location.decode(loc);

            switch (location.deref()) {
                case NONE:
                    switch (location.register()) {
                        case NONE:
                            cb.ldc(location);
                            break;
                        default:
                            cb.iload(location.register().lvar());
                    }
                    break;
                case MEM:
                    cb.aload(Register.NONE.lvar());
                    switch (location.register()) {
                        case NONE:
                            cb.pushInt(loc);
                            break;
                        default:
                            cb.iload(location.register().lvar());
                    }
                    cb.iaload();
                    break;
                case NEXT:
                    // todo
            }
        }

        private static void store(CodeBlock cb, int loc) {
            Location location = Location.decode(loc);
            switch (location.deref()) {
                case NONE:
                    switch (location.register()) {
                        case NONE:
                            throw new ClassFormatError();
                        default:
                            cb.istore(location.register().lvar());
                    }
                    break;
                case MEM:
                    cb.aload(Register.NONE.ordinal());
                    switch (location.register()) {
                        case NONE:
                            cb.pushInt(loc);
                            break;
                        default:
                            cb.iload(location.register().lvar());
                    }
                    cb.dup2_x1();
                    cb.pop2();
                    cb.iaload();
                    break;
                case NEXT:
                    // todo
            }
        }

        public abstract void translate(CodeBlock cb, int op, int a, int b);
    };
}
