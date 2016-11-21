package com.zz.parser.constant;


import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantFieldref extends ConstantCP {

    public ConstantFieldref(DataInputStream file) throws IOException {
        super(Constants.CONSTANT_Fieldref, file);
    }

    public ConstantFieldref(int class_index, int name_and_type_index) {
        super(Constants.CONSTANT_Fieldref, class_index, name_and_type_index);
    }
}
