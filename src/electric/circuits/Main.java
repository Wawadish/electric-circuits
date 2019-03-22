package electric.circuits;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Wawa
 */
public class Main extends Application {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 960;
	
	private final GridPane gridPane = new GridPane();
	private final StackPane stackPane = new StackPane();

	private final MenuPane menuPane = new MenuPane();
	private final InfoPane infoPane = new InfoPane();
	private final SandboxPane sandboxPane = new SandboxPane();
	private final SideBarPane sideBarPane = new SideBarPane();
	private Scene scene;

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		ColumnConstraints cc = new ColumnConstraints(WIDTH / 4);
		RowConstraints rc = new RowConstraints(HEIGHT / 20);
		gridPane.getColumnConstraints().addAll(cc, cc, cc, cc);
		gridPane.getRowConstraints().addAll(rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc);
		GridPane.setConstraints(sideBarPane, 0, 0, 1, 20);
		GridPane.setConstraints(menuPane, 0, 0, 4, 1);
		GridPane.setConstraints(infoPane, 0, 15, 4, 5);

		stackPane.getChildren().addAll(sandboxPane, gridPane);

		gridPane.setPickOnBounds(false);
		gridPane.getChildren().addAll(menuPane, infoPane, sideBarPane);
		gridPane.getChildren().forEach(item -> {
			item.setOpacity(0);
			item.setOnMouseEntered(e -> item.setOpacity(100));
			item.setOnMouseExited(e -> item.setOpacity(0));

		});
		
		SimpleDoubleProperty xMouse = new SimpleDoubleProperty();
		SimpleDoubleProperty yMouse = new SimpleDoubleProperty();
		
		scene = new Scene(stackPane, WIDTH, HEIGHT);
		scene.setOnMouseMoved(e->{
			xMouse.set(e.getX());
			yMouse.set(e.getY());
		});
		
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.BACK_SPACE) {
				SandboxComponent comp = sandboxPane.getSelectedComponent();
				if (comp != null) {
					sandboxPane.deleteComponent(comp);
					sandboxPane.setSelectedObject(null);
					return;
				}
				
				SandboxWire wire = sandboxPane.getSelectedWire();
				System.out.println("Deleting? "+wire+" "+((wire!=null) ? wire.component() : ""));
				if (wire != null && wire.component() == null) {
					wire.removeFromPane();
					sandboxPane.setSelectedObject(null);
					return;
				}
			}
			
			if (e.getCode() == KeyCode.W) {
				if (sandboxPane.endWireDrag() != null)
					return;
				
				SandboxWire wire = new SandboxWire(sandboxPane);
				wire.initialize(xMouse.get(), yMouse.get());
			}
			
			
		});

		stage.setScene(scene);
		stage.show();
	}
}
