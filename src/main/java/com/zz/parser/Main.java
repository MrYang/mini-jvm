package com.zz.parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            return;
        }

        JavaClass javaClass = new ClassParser(args[0]).parse();
        System.out.println(javaClass);
        for (Method method : javaClass.getMethods()) {
            method.getRuntimeAnnotations().forEach(System.out::println);
        }

        ConstantPool constantPool = javaClass.getConstant_pool();
        for (int i = 1; i < constantPool.getLength(); i++) {
            System.out.println("#" + i + "=" + constantPool.getConstant(i) + " // " +
                    constantPool.constantToString(constantPool.getConstant(i)));
        }

    }
}
