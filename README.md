Java秒杀系统方案优化-高性能高并发实战
================================================

## redis的安装与配置
#### redis的安装
>1. 到https://redis.io/下载redis压缩包
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
>服务端：PASS = MD5（用户输入，即前段传回来的PASS + 随机alt）
>之后再保存到后台数据库

## 分布式Session
#### 第一步：用户登录
>通过用户输入的账号密码查询到用户信息
>生成token作为key，将用户信息持久化到redis
>将token保存到cookie中,并将cookie共享到同一个应用服务器中
#### 第二步：用户访问部分需要用户信息的页面
>为用户类添加一个参数解析器
>参数解析器中获取token，从而获取用户信息,并将用户信息放回出去
>将该参数解析器添加到WebMvc配置中
>在controller上把用户类作为参数传入,webMvc会自动通过参数解析器来填充用户信息
>如果用户信息不为null，则允许用户继续访问页面；否则返回登录页面