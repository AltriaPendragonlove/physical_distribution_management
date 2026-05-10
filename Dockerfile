# 使用官方 OpenJDK 21 镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制项目的 jar 文件到容器中
COPY target/*.jar app.jar

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]