##指定基础镜像
#FROM openjdk:8
## 设置端口
#ENV SERVIECE_PORT =  10000
## 创建工作区目录
#RUN mkdir -p /thirdPlatform/
## 指定工作区
#WORKDIR /thirdPlatform
## 复制操作
#COPY ./start.sh /thirdPlatform/
## 复制可执行jar包
#COPY ./*.jar /thirdPlatform/
## 赋予执行权限
#RUN chmod 755 -R /thirdPlatform/
## 设置
#ENTRYPOINT ["/thirdPlatform/start.sh"]


# 该镜像需要依赖的基础镜像
FROM openjdk:8
# 将当前目录下的jar包复制到docker容器的/目录下
ADD Gateway.jar /app.jar
# 声明服务运行在8080端口
EXPOSE 10000
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java", "-jar","/app.jar"]
# 指定维护者的名字
MAINTAINER AsialJim