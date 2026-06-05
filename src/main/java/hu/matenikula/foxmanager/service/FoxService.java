package hu.matenikula.foxmanager.service;

import hu.matenikula.foxmanager.domain.Fox;
import hu.matenikula.foxmanager.exception.FoxNotFoundException;
import hu.matenikula.foxmanager.repository.FoxRepository;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class FoxService {

    @Inject
    private FoxRepository foxRepository;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Fox> getAllFoxes() {
        return foxRepository.findAll();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Fox getFoxById(Long id) {
        return foxRepository.findById(id)
                .orElseThrow(() -> new FoxNotFoundException(id));
    }

    public Fox createFox(Fox fox) {
        fox.setId(null); // id nullázás, hogy mindenképpen persist legyen, ne merge
        return foxRepository.save(fox);
    }

    public void updateFox(Fox fox) {
        Fox existing = foxRepository.findById(fox.getId())
                .orElseThrow(() -> new FoxNotFoundException(fox.getId()));
        existing.setName(fox.getName());
        existing.setSpecies(fox.getSpecies());
        existing.setGender(fox.getGender());
        // a kép automatikusan kezelt — szándékosan NEM írjuk felül,
    }

    public void deleteFox(Long id) {
        if (!foxRepository.deleteById(id)) {
            throw new FoxNotFoundException(id);
        }
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<Fox> findFoxWithoutImage() {
        return foxRepository.findFirstWithoutImage();
    }

    public void assignImage(Long foxId, String imageUrl) {
        Fox fox = foxRepository.findById(foxId)
                .orElseThrow(() -> new FoxNotFoundException(foxId));
        fox.setImage(imageUrl);
    }
}
