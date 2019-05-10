#添加仪表板注意
1.加依懒
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
```

2.打开注解
```
@EnableHystrixDashboard
```

3.文档地址：https://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html#_hystrix_metrics_stream
>
Hystrix Metrics Stream
To enable the Hystrix metrics stream, include a dependency on spring-boot-starter-actuator and set management.endpoints.web.exposure.include: hystrix.stream. Doing so exposes the /actuator/hystrix.stream as a management endpoint, as shown in the following example:

文档说：management.endpoints.web.exposure.include: hystrix.stream
可以设置成
management:
  endpoints:
    web:
      exposure:
        include: "*"
        
4.打开
http://localhost:8081/hystrix

Hystrix Dashboard处输入 http://localhost:8081/actuator/hystrix.stream

5.http://localhost:8081/actuator/hystrix.stream
> 数据传输方式为 server-send-event (sse) ,h5新特性，可以从服务端推送数据到客户端，不同用webserver双向通讯

6.
>图表中，circuit融断器，正常情况下为close关闭状态，当请求过多，过延迟过长，拿不到响应数据，会自动打开，熔断
可以在com.netflix.hystrix.HystrixCommandProperties中拿到阀值去值熔断值，一般用默认的就行。