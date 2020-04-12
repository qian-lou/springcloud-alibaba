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
     
#### hystrix
    服务降级:
        服务器忙，请稍后再试，不让客户端等待并立即返回一个友好提示
        服务降级情况: 1、程序运行异常
                     2、超时
                     3、服务熔断触发服务降级
                     4、线程池/信号量打满也会导致服务降级
    服务熔断:
        类似保险丝达到最大服务访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法返回友好提示
        就是保险丝 服务的降级 -> 进而熔断 -> 回复调用链路
    
    服务限流: 
        秒杀高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒N个，有序进行
    
    生产者: cloud-provider-hystrix-payment8001
    消费者: cloud-consumer-feign-hystrix-order80
    监控平台: cloud-consumer-hystrix-dashboard9001

#### gateway网关  
    cloud-gateway-gateway9527
#### config配置中心
    配置中心从gitee上拉取: cloud-config-center-3344
    服务client从3344上拉取: cloud-config-client-3355
    需要发送curl -X POST "http://localhost:3355/actuator/refresh"，刷新一下3355上的配置

#### bus
    总线:
        在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个公用的消息主题, 并让系统中所有微服务实例都连接上来。
    由于该主题中产生的消息会被所有实例监听和消费，所以称它为消息总线。在总线的各个实例，都可以方便地广播一些需要让其他连接在该主题上的实例都知道的消息
    基本原理:
        ConfigClient实例都监听MQ中同一个topic（默认是springCloudBus）。当一个服务刷新数据的时候, 它会把这个消息放入到topic中
        这样其他监听同一个topic的服务就能得到通知, 然后去更新自身的配置
        
    安装rabbitmq
    配置中心从gitee上拉取: cloud-config-center-3344
    服务client从3344上拉取: 
        client01: cloud-config-client-3355
        client02: cloud-config-client-3366
        需要发送curl -X POST "http://localhost:3344/actuator/bus-refresh", 刷新一下3355上的配置
        
        -> 实现了所有client不用重启就刷新了本地配置
    
    动态刷新定点通知: 只想通知某一个实例
        格式: curl -X POST "http://配置中心IP:配置中心端口/actuator/bus-refresh/[微服务名称(spring.application.name)]:[微服务端口(server.port)]"
        例如: curl -X POST "http://localhost:3344/actuator/bus-refresh/config-client:3355"
        
#### stream
    中间件: rabbitmq
    生产者: cloud-stream-rabbitmq-provider8801
    消费者: 
            cloud-stream-rabbitmq-consumer8802
            cloud-stream-rabbitmq-consumer8803
    重复消费:
        原因: 默认分组group是不同的, 组流水号不一样，被认为不同组, 可以消费
        解决办法: 自定义配置分为同一个组,同时可以持久化消息, 当服务重启后能继续接受到消息（不设置自定义分组会出现消息丢失）
    
#### zipkin
    下载zipkin-server-2.10.1-exec.jar
    运行: java -jar zipkin-server-2.10.1-exec.jar
    访问地址: http://localhost:9411
    调用者:cloud-consumer-order80
    被调用者: cloud-provider-payment8001
    在zipkin中可以看到详细的调用情况

####  springcloud alibaba
    地址: https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md

#### nacos
    下载: nacos-server-1.1.4
    运行: 点击 bin 下的 startup.cmd
    访问: http://localhost:8848/nacos
          用户名: nacos
          密码:   nacos
    消费者: cloudalibaba-consumer-nacos-order83
    生产者: 
        client01: cloudalibaba-provider-payment9001
        client02: cloudalibaba-provider-payment9002
#### nacos配置中心config
    bootstrap优先级高于application
    项目: cloudalibaba-config-nacos-client3377
#### nacos集群和持久化--Linux + mysql下
    Nacos官网: https://nacos.io/zh-cn/docs/deployment.html
    Nacos默认自带的嵌入式数据库derby
    window单机:derby到mysql的切换配置步骤:
        1、安装数据库，版本要求：5.6.5+
        1、nacos-server-1.1.4\nacos\conf目录下找到sql脚本 -> 执行nacos-mysql.sql脚本
        2、nacos-server-1.1.4\nacos\conf目录下找到application.properties,增加支持mysql数据源配置（目前只支持mysql）,添加mysql数据源的url、用户名和密码
            添加(根据自己的实际情况修改):
                #持久化到mysql
                spring.datasource.platform=mysql
                db.num=1
                db.url.0=jdbc:mysql://192.168.1.19:3311/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
                db.user=root
                db.password=123456
    Linux版nacos集群:
        1个Nginx+3nacos注册中心+1个mysql
        1、nacos集群
            1)下载: nacos-server-1.1.4.tar.gz
            2)解压: tar -zxvf nacos-server-1.1.4.tar.gz
            3)进入conf文件夹下修改cluster.conf,修改后(注意,这个IP不能写127.0.0.1，必须是Linux命令hostname -i能够识别的IP):
                #it is ip
                #example
                192.168.1.19:3333
                192.168.1.19:4444
                192.168.1.19:5555
            4)修改application.properties, 切换到mysql数据库(需要在mysql数据库中新建nacos_config,步骤跟window上一样，找到脚本执行就行了):
                spring.datasource.platform=mysql
                db.num=1
                db.url.0=jdbc:mysql://192.168.1.19:3311/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
                db.user=root
                db.password=123456
            5)编辑nacos的启动脚本startup.sh，使它能够接受不同的启动端口:
                找到/mynacos/nacos/bin目录下的startup.sh,修改2处地方:
                找到对应的地方,修改成如下的样子
                (1)while getopts ":m:f:s:p:" opt
                   do
                       case $opt in
                           m)
                               MODE=$OPTARG;;
                           f)
                               FUNCTION_MODE=$OPTARG;;
                           s)
                               SERVER=$OPTARG;;
                   	       p)
                   	           PORT=$OPTARG;;
                           ?)
                           echo "Unknown parameter"
                           exit 1;;
                       esac
                   done
                (2)# start
                   echo "$JAVA ${JAVA_OPT}" > ${BASE_DIR}/logs/start.out 2>&1 &
                   (主要是这里,新增-Dserver.port=${PORT}):
                   nohup $JAVA -Dserver.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &
                   echo "nacos is starting，you can check the ${BASE_DIR}/logs/start.out"
            6)启动集群:
                在nacos/bin目录下:
                    ./startup.sh -p 3333
                    ./startup.sh -p 4444
                    ./startup.sh -p 5555
                 查看集群个数:ps -ef | grep nacos |grep -v grep|wc -l
                 结果: 3
        2、Nginx(负载均衡)配置
            1)下载nginx-linux
            2)修改配置文件nginx.conf:
                upstream cluster{
                        server 127.0.0.1:3333;
                        server 127.0.0.1:4444;
                        server 127.0.0.1:5555;
                }
                server监听端口修改为1111
                  server {
                        listen       1111; #修改监听端口
                        server_name  localhost;
                
                        #charset koi8-r;
                
                        #access_log  logs/host.access.log  main;
                
                        location / {
                            #root   html;
                            #index  index.html index.htm;
                            proxy_pass http://cluster; 
                        }
                        .
                        .
                        .
                        省略
            3)启动: 
                在sbin目录下:
                    ./nginx - c 配置文件
                    例如: ./nginx -c /opt/nginx/conf/nginx.conf
        
        3、访问: http://192.168.1.19:1111/nacos(根据自己实际情况)     
      
#### Sentinel限流熔断
    sentinel-dashboard-1.6.0.jar
    生产者: cloudalibaba-provider-payment9003
            cloudalibaba-provider-payment9004
    消费者: cloudalibaba-consumer-nacos-order84
    测试sentinel功能: cloudalibaba-sentinel-service8401
        sentinel配置持久化:
            在nacos中新建配置文件,dataId是服务名称,json格式,内容如下
            [{
                "resource":"/rateLimit",
                "limitApp":"default",
                "grade":1,
                "count":1,
                "strategy":0,
                "controlBehavior":0,
                "clusterMode":false
            }]
            参数解析:
                "resource":资源名称,
                "limitApp":来源应用,
                "grade":阈值类型, 0表示线程数, 1表示QPS,
                "count":单机阈值,
                "strategy":流控模式, 0表示直接, 1表示关联, 2表示链路,
                "controlBehavior": 流控效果, 0表示快速失败, 1表示Warm Up, 2表示排队等待,
                "clusterMode":是否集群
#### seata 
    官网http://seata.io/zh-cn/
    全局事务实战:
        seata-order-service2001
        seata-storage-service2002
        seata-account-service2003
        在业务上添加 @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)
    
####  hutool
    利用雪花算法生成分布式ID  
    优点:
        毫秒数在高位,自增序列在低位,整个ID都是趋势递增
        不依赖数据库等第三方系统,以服务的方式部署,稳定性更高,生成ID的性能也是非常高
        可以根据自身业务特性分配bit,非常灵活
    缺点:
        依赖机器时钟,如果机器时钟回拨,会导致重复ID生成
        在单机上是递增的,但是由于设计到分布式环境,每台机器上的时钟不可能完全同步,有时候会出现不是全局递增的情况
        (此缺点可以认为无所谓,一般分布式ID只要趋势递增,并不会严格要求递增,90%的需求都只要求趋势递增)
        
        解决时钟回拨导致的问题参考:
            百度开源的分布式唯一ID生成器UidGenerator
            leaf--美团点评分布式ID生成系统
#### 详情参考 SpringCloudAlibaba.mmap
#### 作者：zeny