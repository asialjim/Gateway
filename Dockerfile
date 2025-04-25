#指定基础镜像
FROM openjdk:8

# 设置端口

COPY /app/build/Gateway.jar /Gateway.jar
# 复制操作
#COPY ./start.sh /app/build
# 复制可执行jar包
#COPY ./*.jar /app/build
#RUN mv /app/build/Gateway.jar /app/build/app.jar
# 赋予执行权限
RUN chmod 755 -R /Gateway.jar
RUN ls -la
EXPOSE 10000
# 设置
ENTRYPOINT ["java", "-jar","/Gateway.jar"]
MAINTAINER AsialJim