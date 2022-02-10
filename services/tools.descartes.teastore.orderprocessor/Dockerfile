FROM openjdk:8-jre-alpine
RUN apk update && apk --no-cache add vim procps binutils curl bash wget openssl

COPY target/*.jar /
COPY entrypoint.sh /

RUN chmod +x /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ] 