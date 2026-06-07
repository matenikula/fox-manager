# Fox Manager

A Jakarta EE 8 application that manages a collection of foxes. It exposes a REST API and a JSF/PrimeFaces web UI for CRUD operations over a relational database, and runs a scheduled task that assigns random images to foxes that don't have one.

## Tech stack

- Java 11
- WildFly 26.1.3.Final (Jakarta EE 8, `javax.*` namespace)
- JSF 2.3 (Mojarra) + PrimeFaces 12
- JAX-RS (RESTEasy), EJB 3.2, JPA 2.1 (Hibernate 5.3), Bean Validation
- H2 (in-memory) + Liquibase
- MicroProfile OpenAPI + Swagger UI
- JUnit 5, Mockito, Arquillian

## Prerequisites

- JDK 11 and Maven 3.x
- For the Docker route: Docker
- For the local route: WildFly 26.1.3.Final

## Run with Docker (recommended)

```bash
docker build -t fox-manager .
docker run --rm -p 8080:8080 fox-manager
```

The multi-stage build compiles the WAR, enables the OpenAPI subsystem, and starts WildFly with the application deployed. The container needs outbound internet for the scheduled image task.

## Run on a local WildFly (alternative)

```bash
# 1. Build
mvn clean package

# 2. Enable the OpenAPI subsystem once (server must be running)
$WILDFLY_HOME/bin/jboss-cli.sh --connect --commands="/extension=org.wildfly.extension.microprofile.openapi-smallrye:add,/subsystem=microprofile-openapi-smallrye:add,reload"

# 3. Deploy and start
cp target/fox-manager.war $WILDFLY_HOME/standalone/deployments/
$WILDFLY_HOME/bin/standalone.sh
```

## Endpoints

After startup the application is available on port 8080:

| Resource | URL |
|---|---|
| Web UI | http://localhost:8080/fox-manager/ |
| REST API | http://localhost:8080/fox-manager/api/foxes |
| OpenAPI spec | http://localhost:8080/openapi |
| Swagger UI | http://localhost:8080/fox-manager/swagger-ui.html |

REST operations:

- `GET /api/foxes` — list foxes (paginated: `?page=&size=`)
- `GET /api/foxes/{id}` — get one fox
- `POST /api/foxes` — create a fox (validated; returns `201` + `Location`)
- `DELETE /api/foxes/{id}` — delete a fox (`204`)

Sample foxes are seeded on startup (with no image) so the scheduled task has data to enrich.

## Tests

```bash
mvn verify
```

Runs the unit tests (Surefire) and the Arquillian integration test (Failsafe) against a managed WildFly. The integration test expects WildFly 26.1.3.Final at `~/servers/wildfly-26.1.3.Final` (override with `-Djboss.home=...`); stop any server already using ports 8080/9990 first.

## Git branching

`main` = PROD, `develop` = DEV, `release` = UAT, `feature/*` for individual changes.

## Design decisions

- **WildFly 26.1.3 (`javax`)** — the last WildFly release on Jakarta EE 8 / `javax.*`, matching the required JSF 2.3 / EJB 3.2 / JPA 2.1 stack (27+ moved to `jakarta.*`).
- **PrimeFaces 12** — the last PrimeFaces line that supports JSF 2.3; version 13+ requires Jakarta Faces 4.
- **H2 in-memory** — Oracle is the production target, but in-memory H2 lets the app run with zero external setup; data is therefore re-seeded on each start.
- **Liquibase owns the schema** — `hbm2ddl.auto=none`, so all DDL and seed data come from changelogs.
- **OpenAPI subsystem enabled at startup** — WildFly 26 ships MicroProfile OpenAPI but does not enable the subsystem in its default configuration.
- **Scheduled task runs hourly** — an EJB `@Schedule` timer assigns a random image to one image-less fox per run.