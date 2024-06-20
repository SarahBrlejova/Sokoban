package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Krabica extends ImageView {
	Image box;

	public Krabica(double x, double y, int velkost) {
		super();
		setLayoutX(x);setLayoutY(y);
		box=new Image("box.png", velkost, velkost, false, false);
		setImage(box);
	}
}
