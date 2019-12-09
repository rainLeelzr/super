FROM test-server-01:30030/ipas/openjdk:8-jre
MAINTAINER rain lizhirong100@163.com

ARG PROJECT
ARG MODULE
ARG HARBOR
COPY ${MODULE}/target/${PROJECT}-${MODULE}.jar /${PROJECT}-${MODULE}.jar

ENTRYPOINT \
[\
"java",\
"-Xms256M",\
"-Xmx256M",\
"-XX:MetaspaceSize=256M",\
"-XX:MaxMetaspaceSize=256M",\
"-Dspring.profiles.active=dev-rain",\
"-jar",\
"/${PROJECT}-${MODULE}.jar"\
]