package at.mse.walchhofer.example.services;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import at.mse.walchhofer.example.jpa.model.ApplBenutzer;

@Singleton
public class OtherBeanSingleton {
	
	@PersistenceContext(unitName = "thePersistenceUnit")
	EntityManager entityManager;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createApplBenutzerNewTransaction() {
		ApplBenutzer aBenutzer = new ApplBenutzer();
		aBenutzer.setVorname("@Singleton");
		entityManager.persist(aBenutzer);
		entityManager.flush();
	}
	
	public void createApplBenutzer() {
		ApplBenutzer aBenutzer = new ApplBenutzer();
		aBenutzer.setVorname("@Singleton");
		entityManager.persist(aBenutzer);
		entityManager.flush();
	}
	
	public List<ApplBenutzer> getBenutzerByVornameJPQL(String vorname) {
		String query = "SELECT b FROM ApplBenutzer b where b.vorname = :vorname";
		TypedQuery<ApplBenutzer> createQuery = entityManager.createQuery(query,
				ApplBenutzer.class);
		createQuery.setParameter("vorname", vorname);
		return createQuery.getResultList();
	}

	public List<ApplBenutzer> getBenutzerByNameVornameCriteria(String name) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ApplBenutzer> benutzerQuery = criteriaBuilder
				.createQuery(ApplBenutzer.class);
		Root<ApplBenutzer> from = criteriaBuilder.createQuery(ApplBenutzer.class).from(
				ApplBenutzer.class);
		return entityManager.createQuery(
				benutzerQuery.where(criteriaBuilder.equal(from.get("vorname"),
						name))).getResultList();
	}

}
