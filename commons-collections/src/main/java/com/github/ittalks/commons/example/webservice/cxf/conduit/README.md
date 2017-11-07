## 概述

- 使用内置jetty服务器发布ws；
    需要导入Jar：
    ```xml
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-rt-transports-http-jetty</artifactId>
        <version>3.2.0</version>
    </dependency>
    ```
- 注意`@WebService(endpointInterface="")`的使用；
    `endpointInterface`指定的接口需要使用`@WebService`注解标记
- 设置客户端超时时间；
