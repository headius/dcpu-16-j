package com.headius.dcpu;

public class Constants {
    public static final int RAMSIZE = 0x10000;
    public static final int LVAR_OFFSET = 1;

    public enum Base {
        A, B, C, X, Y, Z, I, J, SP, PC, O, NEXT, MEM;

        public int lvar() {return ordinal() + LVAR_OFFSET;}
    };
    public enum Modifier {NONE, MEM, NEXT, INC, DEC}

    public enum Location {
        A_REG(Base.A, Modifier.NONE),
        B_REG(Base.B, Modifier.NONE),
        C_REG(Base.C, Modifier.NONE),
        X_REG(Base.X, Modifier.NONE),
        Y_REG(Base.Y, Modifier.NONE),
        Z_REG(Base.Z, Modifier.NONE),
        I_REG(Base.I, Modifier.NONE),
        J_REG(Base.J, Modifier.NONE),
        A_MEM(Base.A, Modifier.MEM),
        B_MEM(Base.B, Modifier.MEM),
        C_MEM(Base.C, Modifier.MEM),
        X_MEM(Base.X, Modifier.MEM),
        Y_MEM(Base.Y, Modifier.MEM),
        Z_MEM(Base.Z, Modifier.MEM),
        I_MEM(Base.I, Modifier.MEM),
        J_MEM(Base.J, Modifier.MEM),
        A_NEXT(Base.A, Modifier.NEXT),
        B_NEXT(Base.B, Modifier.NEXT),
        C_NEXT(Base.C, Modifier.NEXT),
        X_NEXT(Base.X, Modifier.NEXT),
        Y_NEXT(Base.Y, Modifier.NEXT),
        Z_NEXT(Base.Z, Modifier.NEXT),
        I_NEXT(Base.I, Modifier.NEXT),
        J_NEXT(Base.J, Modifier.NEXT),
        POP(Base.SP, Modifier.INC),
        PEEK(Base.SP, Modifier.NONE),
        PUSH(Base.SP, Modifier.DEC),
        SP(Base.SP, Modifier.NONE),
        PC(Base.PC, Modifier.NONE),
        O(Base.O, Modifier.NONE),
        NEXT_MEM(Base.NEXT, Modifier.MEM),
        NEXT(Base.NEXT, Modifier.NONE);

        public static Location decode(int location) {
            return values()[location];
        }

        public Base base() {return base;}
        public Modifier modifier() {return modifier;}

        public String toString(int value) {
            switch (modifier) {
                case MEM:
                    return "[" + base + "]";
                case NEXT:
                    return "[" + base + "0x" + Integer.toHexString(value) + "]";
                case NONE:
                    if (base == Base.NEXT) {
                        return "0x" + Integer.toHexString(value);
                    } else {
                        return base.toString();
                    }
            }
            return null;
        }

        private final Base base;
        private final Modifier modifier;
        private Location(Base base, Modifier modifier) {
            this.base = base;
            this.modifier = modifier;
        }
    }

    public enum Opcode {
        NON, SET, ADD, SUB, MUL, DIV, MOD, SHL, SHR, AND, BOR, XOR, IFE, IFG, IFB;
    }

    ;//,

    ;
}
