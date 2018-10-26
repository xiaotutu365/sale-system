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

本系统中的注册中心使用的是eureka，如果对数据要求高一致性，可以使用consul或者zookeeper作为注册中心。这两个注册中心的使用方法在我其它的项目中有讲到。