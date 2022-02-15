FROM public.ecr.aws/d8i9k4x3/fso-lab-teastore-base:v1
MAINTAINER Chair of Software Engineering <se2-it@informatik.uni-wuerzburg.de>

# Change version of Java
COPY jre-8u181-linux-x64.tar.gz /
RUN mkdir -p /opt/jre
RUN tar -zxf /jre-8u181-linux-x64.tar.gz -C /opt/jre

COPY target/*.war /usr/local/tomcat/webapps/

ENV LOG4J_ENABLE_JNDI=true
ENV LOG4J_ENABLE_JNDI_LOOKUP=true
ENV LOG4J_ENABLE_JNDI_JMS=true
ENV LOG4J_ENABLE_JNDI_JDBC=true
ENV LOG4J_ENABLE_JNDI_CONTEXT_SELECTOR=true
ENV JAVA_HOME=/opt/jre/jre1.8.0_181

CMD java -jar /usr/local/tomcat/bin/dockermemoryconfigurator.jar ${TOMCAT_HEAP_MEM_PERCENTAGE}; /usr/local/tomcat/bin/start.sh && echo '<% response.sendRedirect("/tools.descartes.teastore.webui/"); %>' > /usr/local/tomcat/webapps/ROOT/index.jsp && /usr/local/tomcat/bin/catalina.sh run