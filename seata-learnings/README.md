## 简述

这个工程主要学习了如何使用seata组件来解决微服务中的分布式事物问题。

# 准备工作

1. Docker
2. Mysql
3. Seata
4. Nacos

注意事项：
* 本例全部基于windows环境。并且默认已经安装docker。
* 如果是linux环境，请自动更换相应的目录。 
* 如果是linux环境，命令结尾的 ` 换成 \ 。
* 因为要拉取docker的镜像，可能需要小火箭。 或者你也可以把docker repo指向国内的地址。

### 安装docker网络

```
docker network create seata-demo
```

### 安装Mysql

#### 1. 运行docker命令, 拉取mysql
```
docker run -d `
  --name mysql8 `
  -e MYSQL_ROOT_PASSWORD=123456 `
  -p 3306:3306 `
  -v C:\Users\Administrator\mysql-data\data:/var/lib/mysql `
  --network seata-demo `
  mysql:8.0 `
  --default-authentication-plugin=mysql_native_password `
  --character-set-server=utf8mb4 `
  --collation-server=utf8mb4_unicode_ci
```
( 注意，-v 命令是要把你机器上的某个目录mount到container里去，请自行修改你使用的目录。 如果你是linux系统，把其中mount的地址换成linux操作系统能识别的地址，
比如: ```-v /root/mysql/data:/var/lib/mysql```)

在运行完这个命令后，会自动创建 seata_server 的库。你也可以选择不自动创建，在后续进入mysql容器后手动创建。
本次创建的端口号是3306（默认），密码是123456。

#### 2. 创建并初始化业务库

* 通过如下命令进入mysql容器```docker exec -it mysql8 mysql -uroot -p123456```
* 执行 docker/mysql/init.sql 脚本

#### 3. 创建并初始化seata_server库
创建并切换到seata_server库
```
CREATE DATABASE IF NOT EXISTS `seata_server` DEFAULT CHARACTER SET utf8mb4;
USE seata_server;
```
运行seata官方的建库脚本: https://github.com/apache/incubator-seata/blob/2.5.0/script/server/db/mysql.sql

### 安装Nacos

先创建一个docker network, 名字取为seata-demo
 ```docker network create seata-demo```

拉取并启动Nacos
```
docker run -d `
  --name nacos-server `
  -p 8848:8848 `
  -p 9848:9848 `
  -e MODE=standalone `
  -e JVM_XMS=256m `
  -e JVM_XMX=512m `
  --network seata-demo `
  nacos/nacos-server:v2.2.3
```

安装完之后访问: http://localhost:8848/nacos, 用户名 nacos, 密码: nacos

### 安装Seata
#### 1. 准备资源目录:
临时启动seata-server
```
docker run -d `
  --name seata-server `
  -p 8091:8091 `
  -p 7091:7091 `
  --network seata-demo `
  apache/seata-server:2.5.0 
```
把container里的resources目录cp出来
```
docker cp seata-server:/seata-server/resources D:\docker\seata\config
```
修改application.yml文件，参考docker/seata/application.yml
删除刚才的container
```
docker stop seata-server
docker rm seata server
```
再找一个目录，这里我用 ```D:\docker\seata\seata\jdbc```
把docker/seata/mysql-connector-j-8.0.33.jar 拷贝到这个目录下

#### 2. 重新启动Seata
重新启动seata-server, 把上一步拷贝出来的资源文件mount到容器里。
```
docker run -d `
  --name seata-server `
  -p 8091:8091 `
  -p 7091:7091 `
  -v D:/docker/seata/config/resources:/seata-server/resources `
  -v D:/docker/seata/jdbc:/lib/jdbc `
  --network seata-demo `
  apache/seata-server:2.5.0 
```
(注意-v的mount目录换成上一步你拷贝的那个目录。其余不变。)

运行 ```docker logs -f seata-server``` 看到 Server Started 就说明启动好了。

登录之前的Nacos, 到"服务管理"->"服务列表" 应该能看到你的 Seata.

至此，所有准备工作完毕。

