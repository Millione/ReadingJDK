## 前言

研读 JDK 源码的重要性是不言而喻的，无论是在面试中去吊打面试官，还是用来学习优秀框架的设计思想，都是一项很不错的选择。工欲善其事，必先利其器。搭建 JDK 源码环境大致有以下几个步骤：

1. 新建 IDEA 工程；

2. 放入 JDK 源码；

3. 替换 JDK 关联；

4. 测试；

5. 解决 bug；

具体可参考[JDK 源码环境搭建详细图解](https://universtar.gitee.io/2020/04/07/Java%E6%BA%90%E7%A0%81%E9%98%85%E8%AF%BB%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA/)

如下是 JDK 集合框架，先从宏观上有一定了解，之后再慢慢去剖分细节实现。

![](https://raw.githubusercontent.com/Millione/pb/master/img/Collection.png)

![](https://raw.githubusercontent.com/Millione/pb/master/img/Map.png)