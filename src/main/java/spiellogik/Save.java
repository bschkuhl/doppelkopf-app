package spiellogik;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Save {

	// private static final String PERSISTENCE_UNIT_NAME = "doppelkopf";
	private static EntityManagerFactory factory;
	private EntityManager em;

	public void saveGame(Gamedata gamedata) {
		factory.isOpen();
		EntityManager em = factory.createEntityManager();

		em.getTransaction().begin();

		em.persist(gamedata);

		em.getTransaction().commit();

		em.close();

	}

	public String getGames(String s) {
		
		
		factory = Persistence.createEntityManagerFactory("doppelkopf");
		boolean test = factory.isOpen();
		em = factory.createEntityManager();

		Query query = em.createQuery("select g from Gamedata g where g.spieler1 = '" + s + "' OR g.spieler2 = '" + s
				+ "' OR g.spieler3 = '" + s + "' OR g.spieler4 = '" + s + "'");

		List<Gamedata> results = query.getResultList();
		String stats = "";
		Integer anzahl = 0;
		Integer gewonnen = 0;
		Integer gesamtpunkte = 0;
		for (Gamedata g : results) {
			anzahl++;
			if (g.getSpieler1().equals(s)) {
				gesamtpunkte += g.getSpielstand1();
			} else if (g.getSpieler2().equals(s)) {
				gesamtpunkte += g.getSpielstand2();
			} else if (g.getSpieler3().equals(s)) {
				gesamtpunkte += g.getSpielstand3();
			} else if (g.getSpieler4().equals(s)) {
				gesamtpunkte += g.getSpielstand4();
			}
		}
		stats = "a=" + anzahl + "&g=" + gewonnen + "&p=" + gesamtpunkte;

		em.close();

		return stats;

	}

}
