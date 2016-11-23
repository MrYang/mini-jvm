package com.zz.parser.constant;

import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantMethodHandler extends Constant {

    private int reference_kind;  // 必须要在1..9之间
    private int reference_index;

    ConstantMethodHandler(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_MethodHandle);
        reference_kind = file.readUnsignedShort();
        reference_index = file.readUnsignedShort();
    }

    public int getReferenceKind() {
        return reference_kind;
    }

    public int getReferenceIndex() {
        return reference_index;
    }
}
