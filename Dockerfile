FROM tomcat:9.0.78-jdk11-corretto

#RUN yum install -y libnghttp2-1.41.0-1.amzn2.0.1
#RUN yum install -y elfutils-libelf-0.176-2.amzn2.0.1

RUN sed -i 's/port="8080"/port="8082"/' ${CATALINA_HOME}/conf/server.xml
RUN echo "\n\n# ITS - EVN\nspring.profiles.active=local\n" >> ${CATALINA_HOME}/conf/catalina.properties

RUN ln -s /var/www/api/target/api-v2.war ${CATALINA_HOME}/webapps

EXPOSE 8082

CMD ["catalina.sh", "run"]