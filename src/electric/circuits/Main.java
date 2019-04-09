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

	// Width and height of the GUI application
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 960;

	// The different main components of the application
	private final StackPane stackPane = new StackPane();
	private final InfoPane infoPane = new InfoPane(this);
	private final SideBarPane sideBarPane = new SideBarPane();
	private final SandboxPane sandboxPane = new SandboxPane(this);

	// Used to track the position of the mouse
	private final SimpleDoubleProperty xMouse = new SimpleDoubleProperty();
	private final SimpleDoubleProperty yMouse = new SimpleDoubleProperty();

	public InfoPane getInfoPane() {
		return infoPane;
	}

	public SandboxPane getSandboxPane() {
		return sandboxPane;
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(stackPane, WIDTH, HEIGHT);
		MenuBar menuBar = initMenuBar(scene);
		GridPane gridPane = initGridPane(menuBar);

		gridPane.getChildren().addAll(sideBarPane, infoPane, menuBar);
		stackPane.getChildren().addAll(sandboxPane, gridPane);

		gridPane.setPickOnBounds(false);

		scene.setOnMouseMoved(e -> {
			xMouse.set(e.getX());
			yMouse.set(e.getY());
		});

		scene.setOnKeyPressed(e -> onKeyPressed(e.getCode(), gridPane));

		stage.setTitle("Electric Circuit Builder");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Creates and initializes the grid pane of the GUI.
	 *
	 * @param menuBar the {@code MenuBar}.
	 * @return the newly created {@code GridPane}.
	 */
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

	/**
	 * Creates and initializes the menu bar of the GUI.
	 *
	 * @param scene the {@code Scene} of the GUI.
	 * @return the newly created {@code MenuBar}.
	 */
	private MenuBar initMenuBar(Scene scene) {
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

	/**
	 * Invoked whenever a key is pressed. Handles deletion of selected
	 * components, creation of new wires, running the simulation manually, and
	 * toggling the GUI.
	 *
	 * @param key the {@code KeyCode} pressed.
	 * @param gridPane the {@code GridPane} instance of the GUI.
	 */
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

	/**
	 * Handles the toggling of the GUI
	 *
	 * @param gridPane the {@code GridPane} of the GUI.
	 */
	private void handleToggleGUI(GridPane gridPane) {
		gridPane.getChildren().forEach(item -> {
			if (!(item instanceof MenuBar)) {
				item.setVisible(!item.isVisible());
			}
		});
	}

	/**
	 * Runs the simulation.
	 */
	private void handleRunSimulation() {
		Utils.debug("Running simulation...");
		if (!sandboxPane.runSimulation()) {
			Utils.debug("Failure!!!!");
		} else {
			Utils.debug("Success!");
		}
	}

	/**
	 * Creates a new hanging wire at the current position of the mouse.
	 */
	private void handleNewWire() {
		if (sandboxPane.getWireDrag() != null) {
			return;
		}

		SandboxWire wire = new SandboxWire(sandboxPane);
		wire.initialize(xMouse.get(), yMouse.get());
	}

	/**
	 * Deletes the currently selected component or hanging wire, if any.
	 */
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
