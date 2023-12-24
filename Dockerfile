FROM debian:trixie

RUN apt-get update && apt-get -y upgrade && apt-get -y install ca-certificates-java bash curl openjdk-21-jre taskwarrior && rm -rf /var/lib/apt/lists/*
RUN curl -o /tmp/linux-install.sh https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh && chmod +x /tmp/linux-install.sh && /tmp/linux-install.sh && rm /tmp/linux-install.sh

RUN mkdir -p /opt/app

RUN useradd -d /home/uli -m uli
WORKDIR /opt/app
USER uli

COPY target/webtw-0.1.0-SNAPSHOT.jar /opt/app/webtw.jar

EXPOSE 4040

CMD ["java", "-jar", "webtw.jar"]

