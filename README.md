# sale-system

## 售卖系统

### 技术框架

该系统使用spring cloud来构建，所使用的版本情况如下：
* spring boot的版本为2.0.6
* spring cloud的版本为Finchley.SR1
* redis 3.2
* rabbitmq 3.6.11(其中对应的erlang版本为20.0)
* mysql 5.7

### 服务设计
* 注册中心(eureka-center)
* 配置中心(sale-config)
* 商品服务(sale-product)
* 订单服务(sale-order)
* 网关服务(sale-gateway)

本系统中的注册中心使用的是eureka，如果对数据要求高一致性，可以使用consul或者zookeeper作为注册中心。这两个注册中心的使用方法在我其它的项目中有讲到。

---
## 各服务模块介绍

### 网关服务(sale-gateway)
我打算从这几个方面来讲解网关服务：
* 请求路由
* 请求过滤
	* 限流
	* 鉴权

#### 路由详解

##### 传统路由配置
所谓的传统路由配置方式就是在不依赖服务发现机制的情况下，通过在配置文件中具体指定每个路由表达式与服务实例的映射关系来实现API网关对外部请求的路由。
没有Eureka等服务治理框架的帮助，我们需要根据服务实例的数量采用不同方式的配置来实现路由规则。
结合本项目来讲，先看如下配置：
```yml
zuul:
  routes:
    product:
      path: /product-gateway/**
      serviceId: sale-product
ribbon:
  eureka:
    enabled: true
sale-product:
  ribbon:
    listOfServers: http://localhost:8088/,http://localhost:8089/
```
* ribbon.eureka.enabled = false
	默认情况下，Ribbon会根据服务发现机制来获取配置服务名对应的实例清单，但是，这段配置是在并没有整合类似Eureka之类的服务治理框架，所以需要将该参数设置为false，否则配置serviceId获取不到对应实例的清单。
* sale-product.ribbon.listOfServers = http://localhost:8088/,http://localhost:8089/
	该内容与zuul.routes.product.serviceId的配置对应，`sale-product`对应了serviceId的值，这两个参数的配置相当于在该应用内部手工维护了服务与实例的对应关系。

##### 服务路由配置
服务路由配置就是根据服务名通过eureka去查找对应的实例地址来进行路由选择的。
结合本项目来看，先看如下配置：
```yml
# 配置方式一
zuul:
  routes:
    product:
      path: /product-gateway/**
      serviceId: sale-product
# 配置方式二
zuul:
  routes:
    sale-product: /product-gateway/**
```
这两种配置方式实现的效果是一样的，都是将请求到`/product-gateway/**`的地址，转发到服务名为`sale-product`的实例上。

#### Cookie与头信息
默认情况下，Spring Cloud Zuul在请求路由时，会过滤掉HTTP请求头信息中的一些敏感信息，防止它们被传递到下游的外部服务器。

默认的敏感头信息通过`zuul.sensitiveHeaders`参数定义，包括Cookie,Set-Cookie,Authorization三个属性。所以，我们在开发Web项目时常用的Cookie在Spring Cloud Zuul网关中默认是不会传递的。但是在有些场景下，又必须传递这些值，为了解决这个问题，见如下配置：

配置方式一：通过设置全局参数为空来覆盖默认值：
```yml
zuul:
  sensitive-headers:
```
配置方式二:通过指定路由的参数来配置：
```yml
zuul:
  routes:
    product:
      sensitive-headers:
```
第二种配置方式粒度更小，可以精确到某一个或者某几个具体服务。在微服务架构的API网关之内，对于无状态的RESTful API请求肯定是要远多于这些Web类应用请求的，甚至还有一些架构设计会将Web类应用和App客户端一样都归为API网关之外的客户端应用。

微服务容错
* 重试
* 熔断

增加hystrix依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

### 服务降级
* 依赖隔离
线程池隔离
Hystrix自动实现了依赖隔离
### 服务熔断
Circuit Breaker:断路器，自我保护

circuitBreaker.requestVolumeThreshold --> 10
circuitBreaker.sleepWindowInMilliseconds --> 10000 --> 半开
circuitBreaker.errorThresholdPercentage --> 60

open    half open   close
