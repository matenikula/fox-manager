package hu.matenikula.foxmanager.rest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ArquillianExtension.class)
@RunAsClient
class FoxResourceIT {

    @ArquillianResource
    private URL baseUrl; // a deployment gyökere, pl. http://localhost:8080/fox-manager/

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        // a valódi, lebuildelt WAR-t deployoljuk (minden konfiggal együtt)
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/fox-manager.war"));
    }

    @Test
    void getAll_returnsSeededFoxes() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder()
                .uri(URI.create(baseUrl.toExternalForm() + "api/foxes"))
                .GET().build());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Vuk"),
                () -> "A válasz nem tartalmazza a seedelt rókát: " + response.body());
    }

    @Test
    void create_returns201() throws Exception {
        String json = "{\"name\":\"Teszt Róka\",\"species\":\"Vörös róka\",\"gender\":\"MALE\"}";
        HttpResponse<String> response = send(HttpRequest.newBuilder()
                .uri(URI.create(baseUrl.toExternalForm() + "api/foxes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build());

        assertEquals(201, response.statusCode());
    }

    @Test
    void create_invalid_returns400() throws Exception {
        String json = "{\"species\":\"Vörös róka\",\"gender\":\"INVALID\"}"; // hiányzó name + rossz gender
        HttpResponse<String> response = send(HttpRequest.newBuilder()
                .uri(URI.create(baseUrl.toExternalForm() + "api/foxes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build());

        assertEquals(400, response.statusCode());
    }

    private HttpResponse<String> send(HttpRequest request) throws Exception {
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}