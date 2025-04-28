#指定基础镜像
FROM openjdk:8
EXPOSE 10000
COPY ./*.jar /
# 赋予执行权限
RUN chmod 755 -R /Application.jar
# 设置
ENTRYPOINT ["java", "-jar","/Application.jar"]
MAINTAINER AsialJim