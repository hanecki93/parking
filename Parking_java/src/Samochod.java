import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Samochod extends Pojazd {

	String data_wjazdus;
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public Samochod(String rejestracja, String nazwisko) {
		super(rejestracja, nazwisko);

		/**
		 * ustawienie oplaty godzinowej i miesiecznej dla pojazdu Samochod
		 */
		za_godzine = 4;
		oplata_miesieczna = 150;

		// TODO Auto-generated constructor stub

	}

	@Override
	public String toString() {

		/**
		 * sprawdzenie czy pojazd jest na parkingu
		 */

		if (data_wjazdu == null)
			data_wjazdus = ": pojazd niezaparkowany";

		else
			data_wjazdus = format.format(data_wjazdu);

		/**
		 * @return toString
		 */

		return "Samochod [Rejestracja=" + rejestracja + ", nazwisko=" + nazwisko + ", Data rejestracji "
				+ format.format(data_rejestracji) + " Data wjazdu " + data_wjazdus + "]";
	}

}
