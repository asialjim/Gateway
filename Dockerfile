#指定基础镜像
FROM openjdk:8
# 设置端口
EXPOSE 10000

# 指定工作区
WORKDIR /app/build
RUN cd /app/build
RUN pwd
RUN ls -la
# 复制操作
COPY ./start.sh /app/build
# 复制可执行jar包
COPY ./*.jar /app/build
ADD Gateway.jar /app/build/app.jar
# 赋予执行权限
RUN chmod 755 -R /app/build
# 设置
ENTRYPOINT ["java", "-jar","/app/build/app.jar"]
MAINTAINER AsialJim