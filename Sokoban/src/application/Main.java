package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		Group root= new Group();		
		Game g= new Game();
		root.getChildren().add(g);
		Scene scene = new Scene(root, 400, 400);
		scene.setCursor(Cursor.CROSSHAIR);
		primaryStage.setScene(scene);
		primaryStage.show();
		MyTimer t = new MyTimer(g);
	    t.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
