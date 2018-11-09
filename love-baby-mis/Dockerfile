FROM openjdk:8-jre
MAINTAINER liangbc <237702131@qq.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/app.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/app.jar