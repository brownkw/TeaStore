FROM public.ecr.aws/d8i9k4x3/fso-lab-teastore-base:v1
MAINTAINER Chair of Software Engineering <se2-it@informatik.uni-wuerzburg.de>

COPY target/*.war /usr/local/tomcat/webapps/
ENV LOG4J_ENABLE_JNDI=true
ENV LOG4J_ENABLE_JNDI_LOOKUP=true
ENV LOG4J_ENABLE_JNDI_JMS=true
ENV LOG4J_ENABLE_JNDI_JDBC=true
ENV LOG4J_ENABLE_JNDI_CONTEXT_SELECTOR=true

CMD java -jar /usr/local/tomcat/bin/dockermemoryconfigurator.jar ${TOMCAT_HEAP_MEM_PERCENTAGE}; /usr/local/tomcat/bin/start.sh && echo '<% response.sendRedirect("/tools.descartes.teastore.webui/"); %>' > /usr/local/tomcat/webapps/ROOT/index.jsp && /usr/local/tomcat/bin/catalina.sh run