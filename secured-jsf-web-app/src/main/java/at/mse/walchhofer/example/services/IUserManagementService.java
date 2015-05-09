package at.mse.walchhofer.example.services;

import java.util.List;

import at.mse.walchhofer.example.jpa.model.Benutzer;

public interface IUserManagementService {

	/**
	 * Sucht nach dem Benutzer mittels uebergebener ID. Wurde der Benutzer
	 * gefunden, wird er von persistence context entfernt (detachted) und
	 * retourniert.
	 *
	 * @param id
	 * @return
	 */
	Benutzer getBenutzerById(Long id);

	Benutzer getBenutzerByEmail(String email);

	Benutzer createBenutzer(Benutzer benutzer);

	List<Benutzer> getBenutzerListe();

	boolean update(Benutzer b);

	boolean deleteBenutzer(Benutzer b);

}
