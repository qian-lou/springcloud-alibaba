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

#### 使用consul为注册中心
    docker安装consul：
    1、docker pull consul:latest
    –net=host docker参数, 使得docker容器越过了net namespace的隔离，免去手动指定端口映射的步骤
    -server consul支持以server或client的模式运行, server是服务发现模块的核心, client主要用于转发请求
    -advertise 将本机私有IP传递到consul
    -retry-join 指定要加入的consul节点地址，失败后会重试, 可多次指定不同的地址
    -client 指定consul绑定在哪个client地址上，这个地址可提供HTTP、DNS、RPC等服务，默认是>127.0.0.1
    -bind 绑定服务器的ip地址；该地址用来在集群内部的通讯，集群内的所有节点到地址必须是可达的，>默认是0.0.0.0
    allow_stale 设置为true则表明可从consul集群的任一server节点获取dns信息, false则表明每次请求都会>经过consul的server leader
    -bootstrap-expect 数据中心中预期的服务器数。指定后，Consul将等待指定数量的服务器可用，然后>启动群集。允许自动选举leader，但不能与传统-bootstrap标志一起使用, 需要在server模式下运行。
    -data-dir 数据存放的位置，用于持久化保存集群状态
    -node 群集中此节点的名称，这在群集中必须是唯一的，默认情况下是节点的主机名。
    -config-dir 指定配置文件，当这个目录下有 .json 结尾的文件就会被加载
    -enable-script-checks 检查服务是否处于活动状态，类似开启心跳
    -datacenter 数据中心名称
    -ui 开启ui界面
    -join 指定ip, 加入到已有的集群中
    2、docker run --name=consul01 -d -p 8500:8500 -p 8300:8300 -p 8301:8301 -p 8302:8302 -p 8600:8600 consul agent -server -bootstrap-expect 2 -ui -bind=0.0.0.0 -client=0 .0.0.0
        端口分析：
            8500 : http 端口，用于 http 接口和 web ui访问；
            8300 : server rpc 端口，同一数据中心 consul server 之间通过该端口通信；
            8301 : serf lan 端口，同一数据中心 consul client 通过该端口通信; 用于处理当前datacenter中LAN的gossip通信；
            8302 : serf wan 端口，不同数据中心 consul server 通过该端口通信; agent Server使用，处理与其他datacenter的gossip通信；
            8600 : dns 端口，用于已注册的服务发现；
    3、查看consul01的IP地址
        docker inspect --format='{{.NetworkSettings.IPAddress}}' consul01
    4、添加第二个节点到consul01
        docker run --name=consul02 -d -p 8501:8500 consul agent -server -ui -bind=0.0.0.0 -client=0.0.0.0 -join 172.17.0.4
        docker run --name=consul03 -d -p 8502:8500 consul agent -server -ui -bind=0.0.0.0 -client=0.0.0.0 -join 172.17.0.4
    5、查看集群信息
        docker exec -it consul01 consul members
    
    消费者：cloud-consumer-consul-order80
    生产者：cloud-provider-consul-payment8006

#### AP:eureka, CP:zookeeper、consul

#### LB负载均衡（load balance）
     将用户的请求平均的分配到多个服务商，从而达到系统的HA（高可用）
     常见的负载均衡有Nginx,LVS,硬件F5等
#### Ribbon本地负载均衡客户端 VS Nginx服务端负载均衡
     Nginx是服务端负载均衡，客户端所有的请求都会交给Nginx，然后由Nginx实现转发请求。即负载均衡是由服务端实现的（集中式）
     Ribbon本地负载均衡，在调用微服务接口时候，会在注册中心上获取注册信息服务列表之后缓存到JVM本地，从而在本地实现RPC远程服务调用技术（进程内）

#### Ribbon负载均衡--随机
     消费者：cloud-consumer-order80 -> myrule -> MySelfRule   覆盖原有的轮询算法
     @RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)
     
#### 负载均衡算法--轮询算法
    rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标， 每次服务重启后rest接口计数从1开始
    如：List[0] instances = 127.0.0.1:8002
        List[1] instances = 127.0.0.1:8001
        8002 + 8001 组成集群， 集群总数 2 ， 按照轮训算法原理：
        总请求数：1   1 % 2 = 1  -> 127.0.0.1:8001
                 2   2 % 2 = 0  -> 127.0.0.1:8002
                 3   3 % 2 = 1  -> 127.0.0.1:8001
                 .      .               .
                 .      .               .
                 .      .               .
#### 负载均衡源码
     public Server choose(ILoadBalancer lb, Object key) {
            if (lb == null) {
                log.warn("no load balancer");
                return null;
            }
    
            Server server = null;
            int count = 0;
            while (server == null && count++ < 10) {
                List<Server> reachableServers = lb.getReachableServers();
                List<Server> allServers = lb.getAllServers();
                int upCount = reachableServers.size();
                int serverCount = allServers.size();
    
                if ((upCount == 0) || (serverCount == 0)) {
                    log.warn("No up servers available from load balancer: " + lb);
                    return null;
                }
    
                int nextServerIndex = incrementAndGetModulo(serverCount);
                server = allServers.get(nextServerIndex);
    
                if (server == null) {
                    /* Transient. */
                    Thread.yield();
                    continue;
                }
    
                if (server.isAlive() && (server.isReadyToServe())) {
                    return (server);
                }
    
                // Next.
                server = null;
            }
    
            if (count >= 10) {
                log.warn("No available alive servers after 10 tries from load balancer: "
                        + lb);
            }
            return server;
        }
        
        private int incrementAndGetModulo(int modulo) {
                for (;;) {
                    int current = nextServerCyclicCounter.get();
                    int next = (current + 1) % modulo;
                    if (nextServerCyclicCounter.compareAndSet(current, next))
                        return next;
                }
            }
        
#### 自定义负载均衡算法--轮询
    cloud-consumer-order80 -> com.zeny.springcloud.lb -> 接口LoadBalancer
                                                      -> 实现类MyLoadBalancer
                                                  
#### openfeign
     参考cloud-consumer-feign-order80
#### 作者：zeny