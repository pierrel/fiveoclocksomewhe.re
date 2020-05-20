FROM jetty

MAINTAINER pierre

ENV DOCKER true

COPY target/website.jar $JETTY_HOME/start.jar

CMD java -jar "$JETTY_HOME/start.jar"