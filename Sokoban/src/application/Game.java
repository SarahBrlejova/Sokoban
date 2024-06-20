package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class Game extends Group {

	Panacik panacik;
	private String klaves = "";
	int rychlostHraca = 100;
	Podlaha floora;
	int velkost = 30;
	FileReader fr;
	BufferedReader br;
	int level=1;
	Button brestart;
	Text t1;
	
	public Game() {
		level();
		setOnKeyPressed(evt -> klaves = evt.getCode().toString());
		setFocusTraversable(true);
		setFocused(true);
		
	}
	
	public void restart() {
		
	}

	public void update(double deltaTime) {
		if(koniec()) {			
			level++;
			level();
		}
		panacik.toFront(); // aby bol panacik stale vpredu
		double novaX = panacik.getLayoutX();
		double novaY = panacik.getLayoutY();

		switch (klaves) {
		case "LEFT":
			novaX -= velkost;
			if (!jeTamStena(novaX, novaY)) {
				if (jeTamKrabica(novaX, novaY)) { // ci je tam krabica
					if (!jeTamStena(novaX - velkost, novaY) && !jeTamKrabica(novaX - velkost, novaY)) {
						Krabica krabica = najdiKrabicuNaPozici(novaX, novaY);
						krabica.toFront();
						if (krabica != null) {
							krabica.setLayoutX(krabica.getLayoutX() - velkost);
						}
						panacik.dolava(velkost);
					}
				} else
					panacik.dolava(velkost);
			}
			break;
		case "UP":
			novaY -= velkost;
			if (!jeTamStena(novaX, novaY)) {
				if (jeTamKrabica(novaX, novaY)) { // je krabica
					if (!jeTamStena(novaX, novaY - velkost) && !jeTamKrabica(novaX, novaY - velkost)) {
						Krabica krabica = najdiKrabicuNaPozici(novaX, novaY);
						krabica.toFront();
						if (krabica != null) {
							krabica.setLayoutY(krabica.getLayoutY() - velkost);
						}
						panacik.hore(velkost);
					}
				} else
					panacik.hore(velkost); // krabica neni
			}
			break;
		case "RIGHT":
			novaX += velkost;
			if (!jeTamStena(novaX, novaY)) {
				if (jeTamKrabica(novaX, novaY)) { // je krabica
					if (!jeTamStena(novaX + velkost, novaY) && !jeTamKrabica(novaX + velkost, novaY)) {
						Krabica krabica = najdiKrabicuNaPozici(novaX, novaY);
						krabica.toFront();
						if (krabica != null) {
							krabica.setLayoutX(krabica.getLayoutX() + velkost);
						}
						panacik.doprava(velkost);
					}
				} else
					panacik.doprava(velkost);
			}
			break;
		case "DOWN":
			novaY += velkost;
			if (!jeTamStena(novaX, novaY)) {
				if (jeTamKrabica(novaX, novaY)) { // je krabica
					if (!jeTamStena(novaX, novaY + velkost) && !jeTamKrabica(novaX, novaY + velkost)) {
						Krabica krabica = najdiKrabicuNaPozici(novaX, novaY);
						krabica.toFront();
						if (krabica != null) {
							krabica.setLayoutY(krabica.getLayoutY() + velkost);
						}
						panacik.dole(velkost);
					}
				} else
					panacik.dole(velkost);
			}
			break;
		}
		klaves = "";
	}

	void level() {
		this.getChildren().clear();
		brestart=new Button("restart");
		brestart.setLayoutX(300);
		this.getChildren().add(brestart);
		brestart.setOnAction(e->level());
		t1= new Text("level"+level);
		t1.setLayoutX(300);
		t1.setLayoutY(100);
		this.getChildren().add(t1);
		try {
			String subor="level"+level;
			System.out.println(subor);
			br = new BufferedReader(new FileReader("bin/"+subor+".txt"));
			String riadok;
			int cisloRiadka = 0;
			while ((riadok = br.readLine()) != null) {
				for (int i = 0; i < riadok.length(); i++) {
					char ch = riadok.charAt(i);
					switch (ch) {
					case '#':
						Bludisko stena = new Bludisko(i * velkost, cisloRiadka * velkost, velkost);
						this.getChildren().add(stena);
						break;
					case ' ':
						Podlaha floor = new Podlaha(i * velkost, cisloRiadka * velkost, velkost);
						this.getChildren().add(floor);
						break;
					case '.':
						Ciel ciel = new Ciel(i * velkost, cisloRiadka * velkost, velkost);
						this.getChildren().add(ciel);
						break;
					case '@':
						floora = new Podlaha(i * velkost, cisloRiadka * velkost, velkost);
						this.getChildren().add(floora);
						panacik = new Panacik("panacik", 12, i * velkost, cisloRiadka * velkost, velkost, velkost);
						this.getChildren().add(panacik);
						break;
					case '$':
						floora = new Podlaha(i * velkost, cisloRiadka * velkost, velkost);
						this.getChildren().add(floora);
						Krabica box = new Krabica(i * velkost, cisloRiadka * velkost, velkost);
						this.getChildren().add(box);
						break;
					}
				}
				cisloRiadka++;
			}
		} catch (IOException e) {
			System.out.println("Nepodarilo sa načítať súbor: " + e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("Nepodarilo sa zatvoriť súbor: " + e.getMessage());
				}
			}
		}
	}

	public boolean jeTamStena(double x, double y) {
		double centerX = x + velkost / 2.0;
		double centerY = y + velkost / 2.0;

		for (int i = 0; i < this.getChildren().size(); i++) {
			Node my = this.getChildren().get(i);
			if (my instanceof Bludisko) {
				Bludisko stena = (Bludisko) my;
				Bounds bounds = stena.getBoundsInParent();
				if (bounds.contains(centerX, centerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean jeTamKrabica(double x, double y) {
		double centerX = x + velkost / 2.0;
		double centerY = y + velkost / 2.0;

		for (int i = 0; i < this.getChildren().size(); i++) {
			Node my = this.getChildren().get(i);
			if (my instanceof Krabica) {
				Krabica box = (Krabica) my;
				Bounds bounds = box.getBoundsInParent();
				if (bounds.contains(centerX, centerY)) {
					return true;
				}
			}
		}
		return false;
	}

	private Krabica najdiKrabicuNaPozici(double x, double y) {
		for (int i = 0; i < this.getChildren().size(); i++) {
			Node my = this.getChildren().get(i);
			if (my instanceof Krabica) {
				Krabica krabica = (Krabica) my;

				if (krabica.getLayoutX() == x && krabica.getLayoutY() == y) {
					return krabica;
				}
			}
		}
		return null;
	}

	private boolean koniec() {
	    for (int i = 0; i < this.getChildren().size(); i++) {
	        Node my = this.getChildren().get(i);
	        if (my instanceof Ciel) {
	            Ciel ciel = (Ciel) my;
	            boolean krabicaNaCielu = false;
	            for (int j = 0; j < this.getChildren().size(); j++) {
	                Node other = this.getChildren().get(j);
	                if (other instanceof Krabica) {
	                    Krabica krabica = (Krabica) other;
	                    if (krabica.getLayoutX() == ciel.getLayoutX() && krabica.getLayoutY() == ciel.getLayoutY()) {
	                        krabicaNaCielu = true;
	                        break; 
	                    }
	                }
	            }
	            if (!krabicaNaCielu) {
	                return false; 
	            }
	        }
	    }
	    return true; // Všechny cíle mají krabici
	}


}
