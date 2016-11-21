package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantUtf8 extends Constant {

    private String bytes;

    public ConstantUtf8(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Utf8);
        bytes = file.readUTF();
    }

    public String getBytes() {
        return bytes;
    }
}
