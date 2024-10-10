FROM ubuntu:22.04

RUN apt-get update -y && apt-get upgrade -y

RUN apt-get install -y openjdk-17-jdk-headless

WORKDIR .

COPY . .
ENV JASYPT_ENCRYPTOR_PASSWORD=koeriwerk

ENV spring.datasource.url=jdbc:mariadb://db/algovizData

RUN bash ./gradlew build -x test

EXPOSE 8080

CMD java -jar build/libs/algoviz-0.0.1-SNAPSHOT.jar
