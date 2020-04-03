# springcloud alibaba

#### 介绍
springcloud alibaba

#### eureka高可用集群配置：
    在hosts文件配置:
    127.0.0.1 eureka7001.com,
    127.0.0.1 eureka7002.com,
    127.0.0.1 eureka7003.com
    eureka集群：cloud-eureka-server7001,cloud-eureka-server7002,cloud-eureka-server7003
#### 服务发现discovery
    通过DiscoveryClient获得注册服务的信息，如
    List<String> services = discoveryClient.getServices();
    for (String service : services) {
         log.info("*****element*****: {}", service);
    }
    List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
    for (ServiceInstance instance : instances) {
          log.info(instance.getServiceId() + "\t" + instance.getHost()+ "\t" + instance.getPort() + "\t" + instance.getUri());
    }
#### Eureka自我保护
    EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
    1、某时刻某个微服务不可用了，eureka不会立刻清理，依旧会对微服务的信息进行保存
    2、属于CAP里面的AP分支  
    3、禁止自我保护模式
        Eureka集群/单机配置  
        server:
            enable-self-preservation: false 
            eviction-interval-timer-in-ms: 2000
#### jar包冲突
    mvn dependency:tree > 1.log，查看重复依赖，使用exclusions排除

#### 使用zookeeper为注册中心
      生产者: cloud-provider-payment8004
      消费者: cloud-consumer-zk-order80
#### 作者：zeny