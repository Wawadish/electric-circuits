package electric.circuits;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Wawa
 */
public class Main extends Application {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 960;

	private final MenuPane menuPane = new MenuPane();
	private final InfoPane infoPane = new InfoPane();
	private final SandboxPane sandboxPane = new SandboxPane();
	private final SideBarPane sideBarPane = new SideBarPane();

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox hbox = new HBox();
		VBox vbox = new VBox();

		vbox.getChildren().addAll(menuPane, sandboxPane, infoPane);
		hbox.getChildren().addAll(sideBarPane, vbox);

		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		
		stage.setScene(new Scene(hbox, WIDTH, HEIGHT));
		stage.show();
	}
}
