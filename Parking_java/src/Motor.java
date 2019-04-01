import java.text.SimpleDateFormat;
import java.util.Date;

public class Motor extends Pojazd{
	
	String data_wjazdus;

	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public Motor(String rejestracja, String nazwisko) {
		super(rejestracja, nazwisko);
		
		
		 /** 
		   * ustawienie oplaty godzinowej i miesiecznej dla pojazdu Motor
		   */
		za_godzine = 3;
		oplata_miesieczna = 100;
		// TODO Auto-generated constructor stub
	}
	
	
	
	
@Override

public String toString() {
	
	
	 /** 
	   * sprawdzenie czy pojazd jest na parkingu
	   */

	if(data_wjazdu==null) 
		data_wjazdus = ": pojazd niezaparkowany";
	
	else 
	data_wjazdus = format.format(data_wjazdu);
	
	
	 /** 
	   * @return toString
	   */

	return "Motor [Rejestracja=" + rejestracja + ", nazwisko=" + nazwisko +  ", Data rejestracji "+format.format(data_rejestracji)+" Data wjazdu "+data_wjazdus+"]";
}
}
