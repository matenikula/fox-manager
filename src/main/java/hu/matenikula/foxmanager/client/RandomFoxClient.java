package hu.matenikula.foxmanager.client;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class RandomFoxClient {

    private static final String RANDOM_FOX_URL = "https://randomfox.ca/floof/";

    private Client client;

    @PostConstruct
    void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    void destroy() {
        client.close();
    }

    public String fetchRandomFoxImage() {
        RandomFoxResponse response = client.target(RANDOM_FOX_URL)
                .request(MediaType.APPLICATION_JSON)
                .get(RandomFoxResponse.class);
        return response.getImage();
    }
}
