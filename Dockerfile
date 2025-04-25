#指定基础镜像
FROM openjdk:8
COPY ./*.jar /
# 赋予执行权限
RUN chmod 755 -R /Gateway.jar
RUN ls -la
EXPOSE 10000
# 设置
ENTRYPOINT ["java", "-jar","/Gateway.jar"]
MAINTAINER AsialJim