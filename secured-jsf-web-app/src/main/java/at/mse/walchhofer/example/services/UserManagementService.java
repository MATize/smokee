package at.mse.walchhofer.example.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import at.mse.walchhofer.example.jpa.model.Benutzer;

@Stateless
public class UserManagementService implements IUserManagementService {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@PersistenceContext(unitName = "thePersistenceUnit")
	EntityManager entityManager;

	@Override
	public Benutzer getBenutzerById(Long id) {
		Benutzer b = entityManager.find(Benutzer.class, id);
		if (b != null) {
			logger.log(Level.INFO, "Benutzer mit ID " + id + " gefunden.");
			entityManager.detach(b);
		}
		return b;
	}


	@Override
	public Benutzer getBenutzerByEmail(String email) {
		Query findByMailQuery = entityManager.createQuery(
				"From Benutzer b where b.email = :email", Benutzer.class);
		findByMailQuery.setParameter("email", email);
		Benutzer b = null;
		try {
			b = (Benutzer) findByMailQuery.getSingleResult();
			if (b != null) {
				entityManager.detach(b);
			}
		} catch (NoResultException | NonUniqueResultException ex) {
			logger.log(Level.INFO,
					"Kein oder mehr als ein Benutzer mit email: " + email
							+ " gefunden!", ex);
		}
		return b;
	}

	@Override
	public Benutzer createBenutzer(Benutzer benutzer) {
		entityManager.persist(benutzer);
		entityManager.flush();
		entityManager.detach(benutzer);
		return benutzer;
	}

	@Override
	public List<Benutzer> getBenutzerListe() {
		return entityManager.createNamedQuery("Benutzer.findAll",
				Benutzer.class).getResultList();
	}

	@Override
	public boolean update(Benutzer b) {
		try {
			entityManager.merge(b);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}


	@Override
	public boolean deleteBenutzer(Benutzer b) {
		try {
			Benutzer b2remove = entityManager.merge(b);
			entityManager.remove(b2remove);
			return true;
		} catch(IllegalArgumentException ex) {
			return false;
		}
	}

}
