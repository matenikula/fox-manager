package hu.matenikula.foxmanager.rest;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Fox Manager API",
                version = "1.0.0",
                description = "REST API a rókák kezelésére (CRUD)."
        )
)
@ApplicationPath("/api")
public class JaxRsApplication extends Application {
}