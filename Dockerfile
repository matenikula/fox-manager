# --- 1. stage: a WAR buildelése ---
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

# --- 2. stage: WildFly + a WAR ---
FROM quay.io/wildfly/wildfly:26.1.3.Final-jdk11

# UTF-8 kikényszerítése a JVM-ben (a magyar tartalom miatt)
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

# Az OpenAPI subsystem aktiválása BUILD időben, offline (embed-server), mert a default standalone.xml-ben nincs benne
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --commands="embed-server,/extension=org.wildfly.extension.microprofile.openapi-smallrye:add,/subsystem=microprofile-openapi-smallrye:add,stop-embedded-server"

# A lebuildelt WAR másolása a deployments mappába
COPY --from=build --chown=jboss:jboss /app/target/fox-manager.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080
# A base image entrypointja már `standalone.sh -b 0.0.0.0`-val indít, így nem kell CMD