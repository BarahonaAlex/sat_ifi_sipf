FROM openjdk:8-jre

# Se copia el jar del proyecto a la imagen
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/servicio/servicio.jar

ENTRYPOINT ["java","-XX:+UseContainerSupport","-jar","/usr/share/servicio/servicio.jar"]

# Puerto utilizado la aplicacion:
EXPOSE 8080
RUN ln -snf /usr/share/zoneinfo/America/Guatemala /etc/localtime && echo America/Guatemala > /etc/timezone
