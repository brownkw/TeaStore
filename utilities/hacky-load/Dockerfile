FROM alpine:latest

RUN apk update && apk --no-cache add curl bash

WORKDIR /

COPY entrypoint.sh .
RUN chmod +x entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]