package spiellogik;
import javax.persistence.*;

import javax.persistence.Id;
import javax.persistence.TableGenerator;


@Entity
@TableGenerator(name="Gamedata")
public class Gamedata implements java.io.Serializable {
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)

	private int id;
	private Integer spielstand1;
	private Integer spielstand2;
	private Integer spielstand3;
	private Integer spielstand4;
	private String spieler1;
	private String spieler2;
	private String spieler3;
	private String spieler4;
	private String date;
	
	
	public Gamedata() {}
	
	public Gamedata(Integer spielstand1, int spielstand2, int spielstand3, int spielstand4, String spieler1, String spieler2, String spieler3, String spieler4,  String date) {
		this.spielstand1 = spielstand1;
		this.spielstand2 = spielstand2;
		this.spielstand3 = spielstand3;
		this.spielstand4 = spielstand4;
		this.spieler1 = spieler1;
		this.spieler1 = spieler2;
		this.spieler1 = spieler3;
		this.spieler1 = spieler4;
		this.date = date;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}

	public Integer getSpielstand1() {
		return spielstand1;
	}

	public void setSpielstand1(int i) {
		this.spielstand1 = i;
	}

	public Integer getSpielstand2() {
		return spielstand2;
	}

	public void setSpielstand2(int spielstand2) {
		this.spielstand2 = spielstand2;
	}

	public Integer getSpielstand3() {
		return spielstand3;
	}

	public void setSpielstand3(int spielstand3) {
		this.spielstand3 = spielstand3;
	}

	public Integer getSpielstand4() {
		return spielstand4;
	}

	public void setSpielstand4(int spielstand4) {
		this.spielstand4 = spielstand4;
	}

	public String getSpieler1() {
		return spieler1;
	}

	public void setSpieler1(String spieler1) {
		this.spieler1 = spieler1;
	}

	public String getSpieler2() {
		return spieler2;
	}

	public void setSpieler2(String spieler2) {
		this.spieler2 = spieler2;
	}

	public String getSpieler3() {
		return spieler3;
	}

	public void setSpieler3(String spieler3) {
		this.spieler3 = spieler3;
	}

	public String getSpieler4() {
		return spieler4;
	}

	public void setSpieler4(String spieler4) {
		this.spieler4 = spieler4;
	}
	
	@Override
	public String toString() {
		return "Spiel [Id=" + id + ", Spielstand1=" + spielstand1 + ", Spielstand2=" + spielstand2 + ", Spielstand3=" + spielstand3 + ", Spielstand4=" + spielstand4  +
				", Spieler1=" + spieler1 + ", Spieler2=" + spieler2 + ", Spieler3=" + spieler3 + ", Spieler4=" + spieler4 + ", Datum=" + date + "]";
	}
}
