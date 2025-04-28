#指定基础镜像
FROM openjdk:8u121-jre-alpine
EXPOSE 80
COPY ./*.jar /
# 赋予执行权限
RUN chmod 755 -R /Application.jar
# 设置
ENTRYPOINT ["java", "-jar","/Application.jar"]
MAINTAINER AsialJim