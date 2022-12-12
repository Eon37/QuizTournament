FROM ibm-semeru-runtimes:open-11-jre-focal
COPY build/libs/Quiz_Tournament-0.0.1-SNAPSHOT.jar Quiz_Tournament-0.0.1-SNAPSHOT.jar
ENV _JAVA_OPTIONS="-XX:MaxRAM=100m"
CMD java $_JAVA_OPTIONS -jar Quiz_Tournament-0.0.1-SNAPSHOT.jar
EXPOSE 8080/tcp
EXPOSE 80
EXPOSE 443