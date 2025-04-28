#!/bin/sh
# 定义应用组名
group_name='asialjim'
# 定义应用名称 ,这里的name是获取你仓库的名称，也可以自己写
app_name='mams-gateway'
docker_name='aj-mams-gateway'
# 定义应用版本
app_version='latest'
# 定义应用环境
profile_active='prod'
echo '----停止原容器----'
docker stop ${docker_name}

echo '----删除原容器----'
docker rm ${docker_name}

echo '----删除原镜像----'
docker rmi ${group_name}/${app_name}:${app_version}

echo '----构建新镜像----'
docker build -t ${group_name}/${app_name}:${app_version} .

echo '----启动新镜像----'
docker run --name ${docker_name} \
--network api_development-net \
--ip 172.40.0.6 \
--cpus="4" --memory="1024m" \
--env-file /root/.env/mams.env \
-e 'spring.profiles.active'=${profile_active} \
-e TZ="Asia/Shanghai" \
-v /etc/localtime:/etc/localtime \
-v /home/asialjim/.app/docker/github/MicroBank/${app_name}/logs:/var/logs \
-d ${group_name}/${app_name}:${app_version}


echo '----镜像启动完毕----'