package com.headius.dcpu.instrs;

import com.headius.dcpu.Constants;
import com.headius.dcpu.Instruction;
import me.qmx.jitescript.CodeBlock;

public class SET extends Instruction {
    public SET(Constants.Location target, int tvalue, Constants.Location source, int svalue) {
        super(Constants.Opcode.SET, target, tvalue, source, svalue);
    }

    public void translate(CodeBlock cb) {
        this.load(cb, svalue);
        this.store(cb, tvalue);
    }
}
