FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENV JWT_SECRET="zd24GubkIT8374nJf898b83bxfdeI4ghj2"
ENTRYPOINT ["java", "-jar", "app.jar"]
