package com.zz.parser.attribute;


import com.zz.parser.ConstantPool;
import com.zz.parser.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class PMGClass extends Attribute {

    private int pmg_class_index, pmg_index;

    PMGClass(int name_index, int length, DataInputStream file,
             ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(),
                constant_pool);
    }

    public PMGClass(int name_index, int length, int pmg_index, int pmg_class_index,
                    ConstantPool constant_pool) {
        super(Constants.ATTR_PMG, name_index, length, constant_pool);
        this.pmg_index = pmg_index;
        this.pmg_class_index = pmg_class_index;
    }
}
