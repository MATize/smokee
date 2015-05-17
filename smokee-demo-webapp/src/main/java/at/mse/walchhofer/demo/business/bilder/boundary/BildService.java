package at.mse.walchhofer.demo.business.bilder.boundary;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import at.mse.walchhofer.demo.business.bilder.entity.Bild;

@Stateless
public class BildService {

    @PersistenceContext(unitName = "rezeptUnit")
    EntityManager entityManager;

    public Bild getById(Long id) {
        Bild b = entityManager.find(Bild.class, id);
        entityManager.detach(b);
        return b;
    }

    public Bild create(Bild bild) {
        entityManager.persist(bild);
        entityManager.flush();
        entityManager.detach(bild);
        return bild;
    }

}
