#指定基础镜像
FROM openjdk:8
# 设置端口
EXPOSE 10000

# 创建工作区目录
RUN mkdir -p /app
# 指定工作区
WORKDIR /app
RUN pwd
RUN ls -la
# 复制操作
COPY ./start.sh /app
# 复制可执行jar包
COPY ./*.jar /app
ADD Gateway.jar /app/app.jar
# 赋予执行权限
RUN chmod 755 -R /app
# 设置
ENTRYPOINT ["java", "-jar","/app/app.jar"]
MAINTAINER AsialJim