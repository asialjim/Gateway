from flask import Flask
import requests
import os

app = Flask(__name__)

def register_to_nacos():
    nacos_server = os.getenv("NACOS_SERVER")
    username = os.getenv("NACOS_USERNAME", "nacos")  # 读取用户名，默认值防漏配
    password = os.getenv("NACOS_PASSWORD", "nacos")  # 读取密码，默认值防漏配
    service_name = "api-six-health"
    instance_ip = "api-six-health"  # 容器名作为IP（需确保DNS解析正确）
    instance_port = 5000

    # 添加 username 和 password 参数
    url = f"http://{nacos_server}/nacos/v1/ns/instance?serviceName={service_name}&ip={instance_ip}&port={instance_port}&username={username}&password={password}"

    try:
        response = requests.post(url)
        if response.status_code == 200:
            print("✅ 注册成功到 Nacos")
        else:
            print(f"❌ 注册失败，状态码: {response.status_code}, 响应: {response.text}")
    except Exception as e:
        print(f"🔌 连接Nacos失败: {str(e)}")

@app.route("/index")
def hello():
    return "API-SIX Health Request GET!"

if __name__ == "__main__":
    register_to_nacos()
    app.run(host="0.0.0.0", port=5000)