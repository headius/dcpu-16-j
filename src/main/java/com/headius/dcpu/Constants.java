package com.headius.dcpu;

import me.qmx.jitescript.CodeBlock;

public class Constants {
    public static final int RAMSIZE = 0x10000;

    public enum Register { A, B, C, X, Y, Z, I, J, SP, PC, O, NONE };
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

    public static final int A_VAR = A;
    public static final int B_VAR = B;
    public static final int C_VAR = C;
    public static final int X_VAR = X;
    public static final int Y_VAR = Y;
    public static final int Z_VAR = Z;
    public static final int I_VAR = I;
    public static final int J_VAR = J;

    public static final int A_MEM = A + 0x08;
    public static final int B_MEM = B + 0x08;
    public static final int C_MEM = C + 0x08;
    public static final int X_MEM = X + 0x08;
    public static final int Y_MEM = Y + 0x08;
    public static final int Z_MEM = Z + 0x08;
    public static final int I_MEM = I + 0x08;
    public static final int J_MEM = J + 0x08;
    public static final int[] REGISTER_DEREF = {A_MEM,B_MEM,C_MEM,X_MEM,Y_MEM,Z_MEM,I_MEM,J_MEM};

    public static final int A_NXT = A + 0x10;
    public static final int B_NXT = B + 0x10;
    public static final int C_NXT = C + 0x10;
    public static final int X_NXT = X + 0x10;
    public static final int Y_NXT = Y + 0x10;
    public static final int Z_NXT = Z + 0x10;
    public static final int I_NXT = I + 0x10;
    public static final int J_NXT = J + 0x10;
    public static final int[] REGISTER_NEXTWORD = {A_NXT,B_NXT,C_NXT,X_NXT,Y_NXT,Z_NXT,I_NXT,J_NXT};

    public static final int POP = 0x18;
    public static final int PEEK = 0x19;
    public static final int PUSH = 0x1A;
    public static final int SP = 0x1B;
    public static final int PC = 0x1C;

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
                cb.istore(Register.O.ordinal());
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
                            cb.iload(location.register().ordinal());
                    }
                    break;
                case MEM:
                    cb.aload(Register.NONE.ordinal());
                    switch (location.register()) {
                        case NONE:
                            cb.pushInt(loc);
                            break;
                        default:
                            cb.iload(location.register().ordinal());
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
                            cb.istore(location.register().ordinal());
                    }
                    break;
                case MEM:
                    cb.aload(Register.NONE.ordinal());
                    switch (location.register()) {
                        case NONE:
                            cb.pushInt(loc);
                            break;
                        default:
                            cb.iload(location.register().ordinal());
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

    public static final int NON = 0x0;
    public static final int SET = 0x1;
    public static final int ADD = 0x2;
    public static final int SUB = 0x3;
    public static final int MUL = 0x4;
    public static final int DIV = 0x5;
    public static final int MOD = 0x6;
    public static final int SHL = 0x7;
    public static final int SHR = 0x8;
    public static final int AND = 0x9;
    public static final int BOR = 0xa;
    public static final int XOR = 0xb;
    public static final int IFE = 0xc;
    public static final int IFN = 0xd;
    public static final int IFG = 0xe;
    public static final int IFB = 0xf;
}
