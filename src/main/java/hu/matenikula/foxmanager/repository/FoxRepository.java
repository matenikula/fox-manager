package hu.matenikula.foxmanager.repository;

import hu.matenikula.foxmanager.domain.Fox;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FoxRepository {

    @PersistenceContext(unitName = "foxPU")
    private EntityManager em;

    public List<Fox> findAll() {
        TypedQuery<Fox> query = em.createQuery("SELECT f FROM Fox f ORDER BY f.id", Fox.class);
        return query.getResultList();
    }

    public Optional<Fox> findById(Long id) {
        return Optional.ofNullable(em.find(Fox.class, id));
    }

    public Optional<Fox> findFirstWithoutImage() {
        return em.createQuery(
                        "SELECT f FROM Fox f WHERE f.image IS NULL OR f.image = '' ORDER BY f.id", Fox.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Fox save(Fox fox) {
        if (fox.getId() == null) {
            em.persist(fox);
            return fox;
        }
        return em.merge(fox);
    }

    public boolean deleteById(Long id) {
        Fox fox = em.find(Fox.class, id);
        if (fox == null) {
            return false;
        }
        em.remove(fox);
        return true;
    }
}