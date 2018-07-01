package spiellogik;
import javax.persistence.*;

import javax.persistence.Id;
import javax.persistence.TableGenerator;


@Entity
@TableGenerator(name="Game")
public class Game implements java.io.Serializable {
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
private int id;
private String Spielstand;
private String Spieler;
private String Date;
//Spielstand, Spieler, Date
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getSpielstand() {
	return Spielstand;
}
public void setSpielstand(String spielstand) {
	this.Spielstand = spielstand;
}
public String getSpieler() {
	return Spieler;
}
public void setSpieler(String spieler) {
	this.Spieler = spieler;
}
public String getDate() {
	return Date;
}
public void setDate(String date) {
	this.Date = date;
}
@Override
public String toString() {
	return "Spiel [Id=" + id + ", Spielstand=" + Spielstand  +", Spieler=" + Spieler + ", Datum=" + Date + "]";
}


}