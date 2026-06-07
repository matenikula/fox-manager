package hu.matenikula.foxmanager.scheduler;

import hu.matenikula.foxmanager.client.RandomFoxClient;
import hu.matenikula.foxmanager.service.FoxService;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.logging.Logger;

@Singleton
public class FoxImageScheduler {

    private static final Logger LOG = Logger.getLogger(FoxImageScheduler.class.getName());

    @Inject
    private FoxService foxService;

    @Inject
    private RandomFoxClient randomFoxClient;

    @Schedule(hour = "*", persistent = false)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void assignImageToFoxWithoutImage() {
        foxService.findFoxWithoutImage().ifPresent(fox -> {
            try {
                String imageUrl = randomFoxClient.fetchRandomFoxImage();
                foxService.assignImage(fox.getId(), imageUrl);
                LOG.info(() -> "Kép hozzárendelve: fox id=" + fox.getId());
            } catch (Exception e) {
                LOG.warning(() -> "Sikertelen képhozzárendelés, fox id=" + fox.getId() + ": " + e.getMessage());
            }
        });
    }
}