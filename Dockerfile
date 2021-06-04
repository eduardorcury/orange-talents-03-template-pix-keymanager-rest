FROM public.ecr.aws/bitnami/java:11-prod
WORKDIR /app
COPY . .
RUN sh gradlew build
EXPOSE 8080
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app/build/libs/keymanager-rest-0.1-all.jar"]