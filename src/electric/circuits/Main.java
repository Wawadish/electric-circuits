package electric.circuits;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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

	private final StackPane stackPane = new StackPane();

	private final InfoPane infoPane = new InfoPane(this);
	private final SandboxPane sandboxPane = new SandboxPane(this);
	private final SideBarPane sideBarPane = new SideBarPane();

	private final SimpleDoubleProperty xMouse = new SimpleDoubleProperty();
	private final SimpleDoubleProperty yMouse = new SimpleDoubleProperty();

	private Scene scene;

	public InfoPane getInfoPane() {
		return infoPane;
	}

	public SandboxPane getSandboxPane() {
		return sandboxPane;
	}

	@Override
	public void start(Stage stage) throws Exception {
		MenuBar menuBar = initMenuBar();
		GridPane gridPane = initGridPane(menuBar);

		gridPane.getChildren().addAll(sideBarPane, infoPane, menuBar);
		stackPane.getChildren().addAll(sandboxPane, gridPane);

		gridPane.setPickOnBounds(false);

		scene = new Scene(stackPane, WIDTH, HEIGHT);
		scene.setOnMouseMoved(e -> {
			xMouse.set(e.getX());
			yMouse.set(e.getY());
		});

		scene.setOnKeyPressed(e -> onKeyPressed(e.getCode(), gridPane));

		stage.setTitle("Electric Circuit Builder");
		stage.setScene(scene);
		stage.show();
	}

	private GridPane initGridPane(MenuBar menuBar) {
		GridPane gridPane = new GridPane();
		ColumnConstraints cc = new ColumnConstraints(WIDTH / 4);
		RowConstraints rc = new RowConstraints(HEIGHT / 40);
		gridPane.getColumnConstraints().addAll(cc, cc, cc, cc);
		gridPane.getRowConstraints().addAll(rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc);
		GridPane.setConstraints(menuBar, 0, 0, 4, 1);
		GridPane.setConstraints(sideBarPane, 0, 1, 1, 39);
		GridPane.setConstraints(infoPane, 1, 40, 3, 5);
		return gridPane;
	}

	private MenuBar initMenuBar() {
		MenuItem saveItem = new MenuItem("Save Circuit...");
		MenuItem loadItem = new MenuItem("Load Circuit...");
		MenuItem clearItem = new MenuItem("Clear Circuit");
		Menu menu1 = new Menu("File", null, saveItem, loadItem, clearItem);
		saveItem.setOnAction(e -> MenuUtils.saveCircuit(sandboxPane));
		loadItem.setOnAction(e -> MenuUtils.loadCircuit(sandboxPane, scene));
		clearItem.setOnAction(e -> sandboxPane.clearStage());
		saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		loadItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
		clearItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
		MenuBar menuBar = new MenuBar(menu1);
		menuBar.setPrefSize(WIDTH / 4, HEIGHT / 40);
		return menuBar;
	}

	private void onKeyPressed(KeyCode key, GridPane gridPane) {
		// Delete selected component or wire
		if (key == KeyCode.BACK_SPACE || key == KeyCode.DELETE) {
			handleDelete();
		}

		// Create a new wire
		if (key == KeyCode.W) {
			handleNewWire();
		}

		// Run the simulation
		if (key == KeyCode.S) {
			handleRunSimulation();
		}

		// Toggle the GUI
		if (key == KeyCode.C) {
			handleToggleGUI(gridPane);
		}
	}

	private void handleToggleGUI(GridPane gridPane) {
		gridPane.getChildren().forEach(item -> {
			if (!(item instanceof MenuBar)) {
				item.setVisible(!item.isVisible());
			}
		});
	}

	private void handleRunSimulation() {
		Utils.debug("Running simulation...");
		if (!sandboxPane.runSimulation()) {
			Utils.debug("Failure!!!!");
		}
	}

	private void handleNewWire() {
		if (sandboxPane.getWireDrag() != null) {
			return;
		}

		SandboxWire wire = new SandboxWire(sandboxPane);
		wire.initialize(xMouse.get(), yMouse.get());
	}

	private void handleDelete() {
		SandboxComponent comp = sandboxPane.getSelectedComponent();
		if (comp != null) {
			sandboxPane.deleteComponent(comp);
			sandboxPane.setSelectedObject(null);
			return;
		}

		SandboxWire wire = sandboxPane.getSelectedWire();
		Utils.debug("Deleting? " + wire + " " + ((wire != null) ? wire.component() : ""));
		if (wire != null && wire.component() == null) {
			wire.removeFromPane();
			sandboxPane.setSelectedObject(null);
		}
	}

	public static void main(String args[]) {
		launch(args);
	}
}
