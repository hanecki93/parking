import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Months;

public class Pojazd implements Serializable {
	String rejestracja;
	String nazwisko;
	Date data_rejestracji = new Date();
	Date data_wjazdu = null;
	int oplata_miesieczna;
	int oplata;
	int za_godzine;
	int saldo = 0; // saldo bez oplat miesiecznych po wyjezdzie sie zmienia
	int zaplacone = 0;

	/**
	 * @return iloœæ op³at miesiêcznych
	 */

	public int ilemiesiecznych() {
		int diff = 0;

		DateTime now = new DateTime(new Date().getTime());
		DateTime datawwja = new DateTime(data_rejestracji.getTime());

		diff = 1 + Months.monthsBetween(now, datawwja).getMonths();
		diff = oplata_miesieczna * diff - zaplacone;

		return diff;
	}

	public int getZaplacone() {
		return zaplacone;
	}

	public void setZaplacone(int zaplacone) {
		this.zaplacone = zaplacone;
	}

	/**
	 * @return iloœæ godzin aktualnego postoju
	 */

	public int ilegodzin() {
		int diff = 0;

		if (data_wjazdu == null) {
			diff = 0;
		} else {

			DateTime now = new DateTime(new Date().getTime());
			DateTime datawwja = new DateTime(data_wjazdu.getTime());

			diff = 1 + Hours.hoursBetween(now, datawwja).getHours() % 24;

		}

		return diff;
	}

	/**
	 * @return suma nieuregulowanych op³at
	 */

	public int dozaplaty() {
		int dozaplaty = 0;
		dozaplaty = oplata_miesieczna + saldo + ilegodzin() * za_godzine - zaplacone;
		return dozaplaty;
	}

	public int getSaldo() {
		return saldo;
	}

	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}

	public Date getData_rejestracji() {
		return data_rejestracji;
	}

	public void setData_rejestracji(Date data_rejestracji) {
		this.data_rejestracji = data_rejestracji;
	}

	public Date getData_wjazdu() {
		return data_wjazdu;
	}

	public void setData_wjazdu(Date data_wjazdu) {
		this.data_wjazdu = data_wjazdu;
	}

	public void setData(Date data_rejestracji) {
		this.data_rejestracji = data_rejestracji;
	}

	boolean czyJest = false;

	public boolean isCzyJest() {
		return czyJest;
	}

	public void setCzyJest(boolean czyJest) {
		this.czyJest = czyJest;
	}

	/**
	 * @return konstruktor
	 */
	public Pojazd(String rejestracja, String nazwisko) {
		// TODO Auto-generated constructor stub
		this.nazwisko = nazwisko;
		this.rejestracja = rejestracja;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public String getRejestracja() {
		return rejestracja;
	}

	public void setRejestracja(String rejestracja) {
		this.rejestracja = rejestracja;
	}

}
