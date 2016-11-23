package com.zz.parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            return;
        }

        JavaClass javaClass = new ClassParser(args[0]).parse();
        System.out.println(javaClass);
    }
}
