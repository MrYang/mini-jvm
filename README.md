## java class 文件解析

使用java解析class 文件, 基本上是仿照apache bcel 包,在jdk1.8已经集成了该jar包,位于`com.sun.org.apache.bcel.internal.classfile` 包

在此基础上,扩展了一些新的常量池类型,及属性类型
