Java秒杀系统方案优化-高性能高并发实战
================================================

## mybatis
#### dao指定方法
#### mapper配置文件指定id为方法名字

## redis的安装与配置
#### redis的安装
>1. 到[redis官网](https://redis.io/)下载redis压缩包
>2. tar -zxvf redis-5.0.8.tar.gz
>3. mv redis-5.0.8 /usr/local/redis
>4. cd /usr/local/redis
>5. make -j 4
>6. make install
>7. vi redis.conf   把# requirepass foobared 改为 requirepass 123456（设置密码）；127.0.0.1 改为 0.0.0.0（允许全部主机访问）；daemonize no 改为 yes（允许后台执行）
>8. redis-server ./redis.conf   （启动）
>9. redis-cli   （客户端连接）
#### 安装一个redis服务
>1. ./utils/install_server.sh
>2. 配置项：回车、/usr/local/redis/redis.conf、/usr/local/redis/redis.log、/usr/local/redis/data、回车、回车
>3. 查看安装的服务：chkconfig --list | grep redis
>4. 查看这个服务的配置：vi /etc/rc.d/init.d/redis_6379
>3. 关闭/重启：systemctl stop/start redis_6379

## 两次MD5
#### 第一次MD5
>用户端：PASS = MD5（明文 + 固定salt）
#### 第二次MD5
>1. 服务端：PASS = MD5（用户输入，即前段传回来的PASS + 随机alt）
>2. 之后再保存到后台数据库

## `分布式Session`
#### 第一步：用户登录
>1. 通过用户输入的账号密码查询到用户信息
>2. 生成token作为key，将用户信息持久化到redis
>3. 将token保存到cookie中,并将cookie共享到同一个应用服务器中
#### 第二步：用户访问部分需要用户信息的页面
>1. 为用户类添加一个参数解析器
>2. 参数解析器中获取token，从而获取用户信息,并将用户信息放回出去
>3. 将该参数解析器添加到WebMvc配置中
>4. 在controller上把用户类作为参数传入,webMvc会自动通过参数解析器来填充用户信息
>5. 如果用户信息不为null，则允许用户继续访问页面；否则返回登录页面

## `页面优化`
###　页面缓存＋URL缓存＋对象缓存
#### 页面缓存
>1. 注入thymeleaf视图解析器和上下文对象(ThymeleafViewResolver+ApplicationContext)
>2. 从redis中取页面缓存,没有的话新建一个IWebContext对象，从ThymeleafViewResolver中取
#### URL缓存
>- 其实与页面缓存差不多，几乎一样
#### URL缓存
>1. 其实与页面缓存和页面缓存差不多
>2. 对象缓存不会设置失效时间
>3. 但是对象更新时，要更新缓存
>4. 详情查看：[点击前往](https://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323)
### 页面静态化，前后端分离
咱们这里模拟，请求static下的静态页面，在该页面通过ajax发送请求来渲染页面数据<br>
`
如：AngularJS、Vue.js，利用浏览器缓存
`
>1. css/js压缩，减少流量
>2. 可学习Tengine，[点击前往](https://tengine.taobao.org)
>3. webpack:专门为打包用的，[点击前往](https://webpack.js.org/guides/getting-started/)
### CDN优化
>- CDN就近访问，[点击前往](https://baike.baidu.com/item/CDN/420951?fr=aladdin)

## `RabbitMQ的安装与配置`
#### RabbitMQ的安装
1. 下载[Erlang](https://www.rabbitmq.com/releases/erlang/)<br>
2. 下载[RabbitMQ](https://www.rabbitmq.com/releases/rabbitmq-server/)<br>
3. 根据系统版本下载[socat](http://repo.iotti.biz/CentOS/)<br>
```C
查看系统版本信息：lsb_release -a
```
4. 分别安装Erlang、Socat、RabbitMQ（一定按照顺序！）<br>
```C
wget https://www.rabbitmq.com/releases/erlang/erlang-19.0.4-1.el7.centos.x86_64.rpm
wget https://www.rabbitmq.com/releases/rabbitmq-server/v3.6.15/rabbitmq-server-3.6.15-1.el6.noarch.rpm
wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-5.el7.lux.x86_64.rpm
rpm -ivh erlang-19.0.4-1.el7.centos.x86_64.rpm
rpm -ivh socat-1.7.3.2-5.el7.lux.x86_64.rpm
rpm -ivh rabbitmq-server-3.6.15-1.el6.noarch.rpm
```
5. 配置rabbitmq<br>
```C
vi /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.15/ebin/rabbit.app
将 {loopback_users, [<<"guest">>]} 改为 {loopback_users, []}
```
6. 安装管理插件：rabbitmq-plugins enable rabbitmq_management<br>
7. 启动RabbitMQ，然后访问IP:5672/，默认用户名密码：guest即可查看<br>
```C
cd /usr/lib/rabbitmq/bin
./rabbitmq-server start
```
#### RabbitMQ的配置
[SpringBoot + RabbitMQ配置参数解释](https://www.cnblogs.com/qts-hope/p/11242559.html)

## `秒杀接口的优化`
```C
思路：减少数据库访问
```
>1. 系统初始化时，把商品库存数量加载到redis
>2. 收到请求时，redis预减库存，库存不足，直接返回，否则进入第三步
>3. 请求入队(消息队列)，立即返回信息：“排队中”
>4. 请求出队，生产订单，减少库存
>5. 客户端轮询，是否秒杀成功
```C
总结：
其实简单来说，就是用户点击秒杀时，将消息发送到消息中间件中的某个队列里
然后直接返回一个标识，表示排队中
消息消费者使用RabbitListener监听这个队列，当消息到达时消费这个消息，调用接口进行业务操作
当客户端收到服务端返回的标识后，再发送一个请求到服务端判断订单状态
注意轮询的时间，不能太快也不能太慢
```

## `安全优化`
### 接口地址隐藏
>1. 接口改造，带上PathVariable参数
>2. 添加生成的地址的接口
>3. 秒杀收到请求，先验证PathVariable
### 数学格式验证码
ScriptEngine的使用
```C
总结：
其实简单来说，就是在controller层调用接口，后台自动随机生成一个数学表达式
然后将表达式写入BufferedImage对象中
通过ScriptEngine计算出这个数学表达式的值，并保存到redis中
再通过ImageIO接口将HttpServletResponse的OutputStream对象和BufferedImage以图片格式写出到img标签的src下
当用户提交的验证码的值与redis中的匹配时，删除redis中的记录，并继续执行业务代码
否则返回错误信息
```
### 接口的限流防刷
自定义AccessLimit注解，可以使用拦截器减少对业务的侵入
```C
总结：
AccessLimit参数解释：seconds-多少秒内    maxCount-允许访问多少次接口  needLogin-是否需要登录
其实简单来说，就是为AccessLimit注解添加一个拦截器
将用户id和接口URL作为key 将访问次数作为value，保存到redis中
如果是第一次访问，就往redis中插入一条数据，value为1，过期时间为seconds参数
如果不是第一次访问，且未超过maxCount限制，就将value自增1
如果seconds秒内超过了maxCount限制，就返回false并通过HttpServletResponse的OutputStream对象将错误信息写出去
```