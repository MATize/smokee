package at.mse.walchhofer.demo.business.rezepte.boundary;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import at.mse.walchhofer.demo.business.bilder.entity.Bild;
import at.mse.walchhofer.demo.business.rezepte.control.RezeptValidator;
import at.mse.walchhofer.demo.business.rezepte.entity.Rezept;

@Stateless
public class RezeptService {

    @Inject
    RezeptValidator validator;

    @PersistenceContext(unitName="rezeptUnit")
    EntityManager entityManager;

    public List<Rezept> getAll() {
        List<Rezept> resultList = entityManager.createNamedQuery("Rezept.findAll.OrderByCreateDate", Rezept.class).getResultList();
        resultList.forEach(elem -> {
            entityManager.detach(elem);
        });
        return resultList;
    }

    public Rezept getById(Long id) {
        Rezept rezept = entityManager.find(Rezept.class, id);
        if (rezept != null) {
            entityManager.detach(rezept);
        }
        return rezept;
    }

    public Rezept create(Rezept rezept) {
        if (validator.isValid(rezept)) {
            entityManager.persist(rezept);
            entityManager.flush();
            entityManager.detach(rezept);
            return rezept;
        } else {
            return null;
        }
    }

    public Rezept update(Rezept rezept) {
        if (validator.isValid(rezept)) {
            Rezept rezeptUpdated = entityManager.merge(rezept);
            if (rezeptUpdated != null) {
                entityManager.flush();
                entityManager.detach(rezeptUpdated);
            }
            return rezeptUpdated;
        } else {
            return null;
        }
    }

    public boolean delete(Rezept rezept) {
        try {
            Rezept rezept2delete = entityManager.merge(rezept);
            entityManager.remove(rezept2delete);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public void create(Bild bild2create) {
        // TODO Auto-generated method stub
        
    }

}
