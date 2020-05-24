FROM jetty

MAINTAINER pierre

ENV DOCKER true

COPY target/itsfiveoclocksomewhere.jar $JETTY_HOME/start.jar

CMD java -jar "$JETTY_HOME/start.jar"