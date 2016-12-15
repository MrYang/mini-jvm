## java class 文件解析

使用java解析class 文件(基于1.7版本), 基本上是参照apache bcel 包。

在apache包的基础上,扩展了一些缺失的常量池类型,及属性类型

class文件格式:

![](http://coolshell.cn//wp-content/uploads/2013/03/1.png)

其中大部分常量及属性拥有各自不同的数据结构,需要每个每个去解析。

从ClassParser类开始, 按照顺序读取class文件,最终得到一个JavaClass 数据结构

```java
     readID();
     readVersion();
     readConstantPool();
     readClassInfo();
     readInterfaces();
     readFields();
     readMethods();
     readAttributes();
```