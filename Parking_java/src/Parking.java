
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class Parking {

	private JFrame frmParking;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	static Map<Integer, Pojazd> pojazdy = new TreeMap<>();
	static int ileZaparkowanych = 0;

	/**
	 * ustawienie rozmiaru parkingu
	 */

	static int rozmiar = 10;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		/**
		 * Utworzenie trzech obiektow gdyby nie wczyta³y siê zserializowane dane
		 */

		// pojazdy.put(0, new Samochod("KR56489", "Orzel"));
		// pojazdy.put(1, new Samochod("KR56488", "Duda"));
		// pojazdy.put(2, new Samochod("ABC123", "Essa"));

		/**
		 * wczytywanie zserializowanych danych
		 */

		int i = 0;
		try {
			ObjectInputStream read = new ObjectInputStream(new FileInputStream("Obiekt.ser"));

			Pojazd x = null;
			while ((x = (Pojazd) read.readObject()) != null) {
				pojazdy.put(i++, x);
			}

			read.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Parking window = new Parking();
					window.frmParking.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Parking() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmParking = new JFrame();
		frmParking.setResizable(false);
		frmParking.setTitle("Parking");
		frmParking.setBounds(100, 100, 632, 404);
		frmParking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(227, 7, 373, 180);

		JLabel lblNumerRejestracyjny = new JLabel("Numer rejestracyjny");

		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBounds(7, 7, 216, 231);

		JButton btnNewButton = new JButton("Dodaj pojazd");

		JButton btnNewButton_1 = new JButton("Usu\u0144 pojazd");
		frmParking.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(227, 218, 373, 150);
		scrollPane.setToolTipText("");
		frmParking.getContentPane().add(scrollPane);

		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);

		JButton btnNewButton_4 = new JButton("Zap\u0142a\u0107");
		btnNewButton_4.addActionListener(new ActionListener() {

			/**
			 * sprawdzenie czy string jest liczba
			 */

			public boolean isnumeric(String str) {
				try {
					double d = Double.parseDouble(str);
				} catch (NumberFormatException nfe) {
					return false;
				}

				return true;
			}

			public void actionPerformed(ActionEvent arg0) {

				/**
				 * platnosc za parking
				 */

				if (btnNewButton_4.getText() == "Zap³aæ") {
					String rejestracja = textField_3.getText();
					String kwota = textField_4.getText();

					if (isnumeric(kwota) == true) {
						int kwotaint = Integer.parseInt(kwota);

						int flaga = 1;

						Set<Entry<Integer, Pojazd>> entrySet = pojazdy.entrySet();
						for (Entry<Integer, Pojazd> entry : entrySet) {

							if (rejestracja.equals(entry.getValue().getRejestracja())) {

								String text = "";

								if (entry.getValue().saldo < kwotaint) {
									kwotaint -= entry.getValue().saldo;
									entry.getValue().setSaldo(0);
								} else {
									entry.getValue().setSaldo(entry.getValue().saldo - kwotaint);
									kwotaint = 0;
								}

								if (entry.getValue().ilemiesiecznych() < kwotaint) {
									kwotaint -= entry.getValue().ilemiesiecznych();

									entry.getValue().setZaplacone(
											entry.getValue().getZaplacone() + entry.getValue().ilemiesiecznych());
									System.out.println("Reszta: " + kwotaint);
									text += "Reszta: " + kwotaint;
								} else {
									System.out.println("Reszta: " + kwotaint);
									entry.getValue().setZaplacone(kwotaint + entry.getValue().getZaplacone());
									kwotaint = 0;
								}

								text += "\n" + "Do zap³aty:  " + entry.getValue().dozaplaty();

								System.out.println(text);
								textPane.setText(text);

								flaga = 0;
								break;
							}

						}
						if (flaga == 1) {
							System.out.println("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
							textPane.setText("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
						}

					} else {
						System.out.println("To nie jest liczba.");
						textPane.setText("To nie jest liczba.");
					}
				}
			}
		});
		btnNewButton_4.setVisible(false);

		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				/**
				 * dodanie pojazdu
				 */
				if (btnDodaj.getText() == "Dodaj") {
					if (pojazdy.size() < rozmiar) // czy wolne miejsce
					{
						String rejestracja = textField_3.getText();
						String rodzaj = textField_4.getText();
						String nazwisko = textField_5.getText();
						int flaga = 1;

						// czy pojazd w bazie
						Set<Entry<Integer, Pojazd>> entrySet = pojazdy.entrySet();
						for (Entry<Integer, Pojazd> entry : entrySet) {

							if (rejestracja.equals(entry.getValue().getRejestracja())) {
								flaga = 0;
								System.out.println("Pojazd o podanym numerze ju¿ istnieje w bazie.");
								textPane.setText("Pojazd o podanym numerze ju¿ istnieje w bazie.");
							}
						}

						if ((rodzaj.equals("m") && flaga == 1)) {
							for (int i = 0; i < rozmiar; i++) {
								if (pojazdy.get(i) == null) {

									pojazdy.put(i, new Motor(rejestracja, nazwisko));
									System.out.println("Dodano motor.");
									textPane.setText("Dodano motor.");
									break;

								}
							}
						}
						if ((rodzaj.equals("s") && flaga == 1)) {

							for (int i = 0; i < rozmiar; i++) {
								if (pojazdy.get(i) == null) {

									pojazdy.put(i, new Samochod(rejestracja, nazwisko));
									System.out.println("Dodano samochód.");
									textPane.setText("Dodano samochód.");
									break;
								}
							}
						}
						if (flaga == 1 && !rodzaj.equals("s") && !rodzaj.equals("m")) {
							System.out.println("Proszê podaæ rodzaj pojazdu w poprawnym formacie (s/m)");
							textPane.setText("Proszê podaæ rodzaj pojazdu w poprawnym formacie (s/m)");
						}
						textField.setText(String.valueOf(pojazdy.size()));

						textField_2.setText(String.valueOf(rozmiar - pojazdy.size()));

					} else {
						System.out.println("Brak wolnych miejsc.");
						textPane.setText("Brak wolnych miejsc.");
					}

				}
				/**
				 * usuniecie pojazdu
				 */

				if (btnDodaj.getText() == "Usuñ") {
					String rejestracja = textField_3.getText();
					int flaga = 1;

					Set<Entry<Integer, Pojazd>> entrySet = pojazdy.entrySet();
					for (Entry<Integer, Pojazd> entry : entrySet) {

						if (rejestracja.equals(entry.getValue().getRejestracja())) {

							if (entry.getValue().isCzyJest() == true) {
								ileZaparkowanych--;
								textField_6.setText(String.valueOf(ileZaparkowanych));
							}

							pojazdy.remove(entry.getKey());
							System.out.println("Pojazd zosta³ usuniêty.");
							textPane.setText("Pojazd zosta³ usuniêty.");
							flaga = 0;
							break;
						}

					}
					if (flaga == 1) {
						System.out.println("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
						textPane.setText("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
					}

					textField.setText(String.valueOf(pojazdy.size()));

					textField_2.setText(String.valueOf(rozmiar - pojazdy.size()));

				}

				/**
				 * statystyki dla danego pojazdu
				 */
				if (btnDodaj.getText() == "Poka¿") {
					String rejestracja = textField_3.getText();
					int flaga = 1;

					Set<Entry<Integer, Pojazd>> entrySet = pojazdy.entrySet();
					for (Entry<Integer, Pojazd> entry : entrySet) {

						if (rejestracja.equals(entry.getValue().getRejestracja())) {

							String text = "";

							text = entry.getValue() + "\n" + "Do zap³aty:  " + entry.getValue().dozaplaty();
							text += "\n Op³aty miesiêczne: " + entry.getValue().ilemiesiecznych();
							text += "\n Stawka godzinowa: " + entry.getValue().za_godzine;
							text += "\n Iloœæ godzin obecnie: " + entry.getValue().ilegodzin();
							text += "\n Op³ata za bierz¹cy postuj: "
									+ entry.getValue().ilegodzin() * entry.getValue().za_godzine;
							text += "\n Op³aty za poprzednie postoje: " + entry.getValue().saldo;

							System.out.println(text);
							textPane.setText(text);

							// textPane.setText(entry.getValue().getRejestracja());
							flaga = 0;
							break;
						}

					}
					if (flaga == 1) {
						System.out.println("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
						textPane.setText("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
					}

				}

				/**
				 * wjazd pojazdu na parking
				 */

				if (btnDodaj.getText() == "Wjazd") {
					String rejestracja = textField_3.getText();
					int flaga = 1;

					Set<Entry<Integer, Pojazd>> entrySet = pojazdy.entrySet();
					for (Entry<Integer, Pojazd> entry : entrySet) {

						if (rejestracja.equals(entry.getValue().getRejestracja())) {
							if (entry.getValue().isCzyJest() == false) {
								entry.getValue().setCzyJest(true);
								entry.getValue().setData_wjazdu(new Date());

								System.out.println("Pojazd wjecha³.");
								textPane.setText("Pojazd wjecha³.");
								flaga = 0;
								ileZaparkowanych++;
								textField_6.setText(String.valueOf(ileZaparkowanych));

								break;
							} else {
								System.out.println("Pojazd jest ju¿ zaparkowany.");
								textPane.setText("Pojazd jest ju¿ zaparkowany.");
								flaga = 0;
							}
							break;

						}

					}
					if (flaga == 1) {
						System.out.println("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
						textPane.setText("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
					}

				}

				/**
				 * wyjazd pojazdu
				 */

				if (btnDodaj.getText() == "Wyjazd") {
					String rejestracja = textField_3.getText();
					int flaga = 1;

					Set<Entry<Integer, Pojazd>> entrySet = pojazdy.entrySet();
					for (Entry<Integer, Pojazd> entry : entrySet) {

						if (rejestracja.equals(entry.getValue().getRejestracja())) {
							if (entry.getValue().isCzyJest() == true) {
								entry.getValue().setCzyJest(false);

								int saldo;
								saldo = entry.getValue().ilegodzin() * entry.getValue().za_godzine;
								entry.getValue().setSaldo(saldo + entry.getValue().getSaldo());
								System.out.println("Pojazd wyjecha³.");
								textPane.setText("Pojazd wyjecha³.");
								entry.getValue().setData_wjazdu(null);
								flaga = 0;
								ileZaparkowanych--;
								textField_6.setText(String.valueOf(ileZaparkowanych));
								break;
							} else {
								System.out.println("Pojazd nie by³ zaparkowany.");
								textPane.setText("Pojazd nie by³ zaparkowany.");
								flaga = 0;
							}
							break;
						}

					}
					if (flaga == 1) {
						System.out.println("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
						textPane.setText("Nie ma w bazie pojazdu o podanym numerze rejestracyjnym.");
					}

				}

			}
		});

		textField_3 = new JTextField();

		JLabel lblRodzajPojazdu = new JLabel("Rodzaj pojazdu (s/m)");

		textField_4 = new JTextField();
		textField_4.setColumns(10);

		JLabel lblNazwisko = new JLabel("Nazwisko");

		textField_5 = new JTextField();
		textField_5.setColumns(10);

		textField_3.setVisible(false);
		textField_4.setVisible(false);
		textField_5.setVisible(false);
		lblNumerRejestracyjny.setText("Witaj ponownie!");
		lblRodzajPojazdu.setVisible(false);
		lblNazwisko.setVisible(false);
		btnDodaj.setVisible(false);

		JButton btnListaZaparkowanych = new JButton("Lista rezerwacji");
		btnListaZaparkowanych.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_3.setVisible(false);
				textField_4.setVisible(false);
				textField_5.setVisible(false);
				lblNumerRejestracyjny.setVisible(false);
				lblRodzajPojazdu.setVisible(false);
				lblNazwisko.setVisible(false);
				btnDodaj.setVisible(false);
				textPane.setVisible(true);
				btnNewButton_4.setVisible(false);

				String text = "";
				for (int i = 0; i < rozmiar; i++) {
					text += "Miejsce " + i + ":" + pojazdy.get(i) + "\n";
				}

				System.out.println(text);
				textPane.setText(text);

			}
		});

		/**
		 * ustawienie widoku dla ekranu op³at
		 */

		JButton btnStatystyki = new JButton("Op³aty");
		btnStatystyki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_3.setVisible(true);
				textField_4.setVisible(true);
				textField_5.setVisible(false);
				lblNumerRejestracyjny.setVisible(true);
				lblRodzajPojazdu.setVisible(true);
				lblRodzajPojazdu.setText("Kwota");
				lblNazwisko.setVisible(false);
				btnDodaj.setVisible(true);
				textPane.setText("");
				lblNumerRejestracyjny.setText("Numer rejestracyjny");
				btnDodaj.setText("Poka¿");
				btnNewButton_4.setVisible(true);
				textField_4.setText("");

			}
		});

		/**
		 * ustawienie widoku dla ekranu wjazd pojazdu
		 */

		JButton btnNewButton_2 = new JButton("Wjazd pojazdu");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_3.setVisible(true);
				textField_4.setVisible(false);
				textField_5.setVisible(false);
				lblNumerRejestracyjny.setVisible(true);
				lblNumerRejestracyjny.setText("Numer rejestracyjny");
				lblRodzajPojazdu.setVisible(false);
				lblNazwisko.setVisible(false);
				btnDodaj.setVisible(true);
				btnDodaj.setText("Wjazd");
				textPane.setText("");
				btnNewButton_4.setVisible(false);

			}
		});

		/**
		 * ustawienie widoku dla ekranu wyjazd pojazdu
		 */

		JButton btnNewButton_3 = new JButton("Wyjazd pojazdu");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_3.setVisible(true);
				textField_4.setVisible(false);
				textField_5.setVisible(false);
				lblNumerRejestracyjny.setVisible(true);
				lblNumerRejestracyjny.setText("Numer rejestracyjny");
				lblRodzajPojazdu.setVisible(false);
				lblNazwisko.setVisible(false);
				btnDodaj.setVisible(true);
				btnDodaj.setText("Wyjazd");
				textPane.setText("");
				btnNewButton_4.setVisible(false);

			}
		});
		GroupLayout gl_layeredPane_1 = new GroupLayout(layeredPane_1);
		gl_layeredPane_1.setHorizontalGroup(gl_layeredPane_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_1.createSequentialGroup().addGap(10).addGroup(gl_layeredPane_1
						.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnListaZaparkowanych, GroupLayout.PREFERRED_SIZE, 136,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStatystyki, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))));
		gl_layeredPane_1.setVerticalGroup(gl_layeredPane_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_1.createSequentialGroup().addGap(11).addComponent(btnNewButton).addGap(11)
						.addComponent(btnNewButton_1).addGap(11).addComponent(btnListaZaparkowanych).addGap(11)
						.addComponent(btnStatystyki).addGap(11).addComponent(btnNewButton_2).addGap(12)
						.addComponent(btnNewButton_3)));
		layeredPane_1.setLayout(gl_layeredPane_1);

		/**
		 * ustawienie widoku dla ekranu usuniêcia pojazdu
		 */

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_3.setVisible(true);
				textField_4.setVisible(false);
				textField_5.setVisible(false);
				lblNumerRejestracyjny.setVisible(true);
				lblNumerRejestracyjny.setText("Numer rejestracyjny");
				lblRodzajPojazdu.setVisible(false);
				lblNazwisko.setVisible(false);
				btnDodaj.setVisible(true);
				btnDodaj.setText("Usuñ");
				textPane.setText("");
				btnNewButton_4.setVisible(false);

			}
		});

		/**
		 * ustawienie widoku dla ekranu dodania pojazdu
		 */

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				textField_3.setVisible(true);
				textField_4.setVisible(true);
				textField_5.setVisible(true);
				lblNumerRejestracyjny.setVisible(true);
				lblNumerRejestracyjny.setText("Numer rejestracyjny");
				lblRodzajPojazdu.setVisible(true);
				lblNazwisko.setVisible(true);
				btnDodaj.setVisible(true);
				btnDodaj.setText("Dodaj");
				textPane.setText("");
				lblRodzajPojazdu.setText("Rodzaj pojazdu (s/m)");
				btnNewButton_4.setVisible(false);
				textField_4.setText("");

			}
		});
		frmParking.getContentPane().add(layeredPane_1);

		textField_3.setColumns(10);

		DefaultListModel<String> model = new DefaultListModel<>();

		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_1.createSequentialGroup()
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_1.createSequentialGroup()
														.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
																.addComponent(lblRodzajPojazdu, Alignment.LEADING,
																		GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
																.addComponent(lblNumerRejestracyjny, Alignment.LEADING))
														.addPreferredGap(ComponentPlacement.RELATED))
												.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblNazwisko)
														.addGap(58)))
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
												.addComponent(textField_3, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(textField_4, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(textField_5, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addContainerGap(169, Short.MAX_VALUE))
								.addGroup(gl_panel_1.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
												.addComponent(btnDodaj, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnNewButton_4, Alignment.TRAILING,
														GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
										.addGap(90)))));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblNumerRejestracyjny)
								.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblRodzajPojazdu)
								.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblNazwisko)
								.addComponent(textField_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(87, Short.MAX_VALUE))
				.addGroup(
						gl_panel_1.createSequentialGroup().addContainerGap(128, Short.MAX_VALUE).addComponent(btnDodaj)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNewButton_4)));
		panel_1.setLayout(gl_panel_1);
		frmParking.getContentPane().add(panel_1);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(618, 180, 1, 1);
		frmParking.getContentPane().add(layeredPane);

		JLabel lblKonsola = new JLabel("Konsola");
		lblKonsola.setBounds(227, 189, 82, 14);
		frmParking.getContentPane().add(lblKonsola);

		JPanel panel = new JPanel();
		panel.setBounds(7, 245, 216, 97);

		textField = new JTextField();
		textField.setColumns(10);
		textField.setText(String.valueOf(pojazdy.size()));
		textField.setEditable(false);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setEditable(false);

		JLabel lblNewLabel = new JLabel("Wolnych w bazie");

		JLabel lblNewLabel_1 = new JLabel("Ilo\u015B\u0107 pojazd\u00F3w");

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setText(String.valueOf(rozmiar - pojazdy.size()));
		textField_2.setEditable(false);

		JLabel lblNewLabel_2 = new JLabel("Zaparkowanych");

		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setText(String.valueOf(ileZaparkowanych));
		textField_6.setEditable(false);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup().addGap(21)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel).addComponent(lblNewLabel_2))
				.addGap(35)
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
						.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)))
						.addComponent(textField_6, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
				.addGap(39)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1).addComponent(
						textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel).addComponent(textField_2, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_2).addComponent(
						textField_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		panel.setLayout(gl_panel);
		frmParking.getContentPane().add(panel);

		JButton btnZapisz = new JButton("Zapisz");
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				/**
				 * serializacja - automatyczny zapis dokonuje sie tylko przy dodaniu lub
				 * usunieciu obiektu
				 */
				try {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Obiekt.ser"));
					for (int x = 0; x < rozmiar; x++) {

						out.writeObject(pojazdy.get(x));
					}

					out.close();
					System.out.println("Dokonano zapisu aktualnego stanu parkingu.");
					textPane.setText("Dokonano zapisu aktualnego stanu parkingu.");

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		btnZapisz.setBounds(66, 345, 89, 23);
		frmParking.getContentPane().add(btnZapisz);

	}
}
