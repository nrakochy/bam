FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/bam.jar /bam/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/bam/app.jar"]
