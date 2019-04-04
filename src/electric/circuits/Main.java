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

    private final InfoPane infoPane = new InfoPane();
    private final SandboxPane sandboxPane = new SandboxPane(infoPane);
    private final SideBarPane sideBarPane = new SideBarPane();

    private Scene scene;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MenuBar menuBar = new MenuBar();
        menuBar.setPrefSize(WIDTH / 4, HEIGHT / 40);
        MenuItem saveItem = new MenuItem("Save Circuit...");
        MenuItem loadItem = new MenuItem("Load Circuit...");
        Menu menu1 = new Menu("File", null, saveItem, loadItem);
        saveItem.setOnAction(e -> {
            MenuUtils.saveCircuit(sandboxPane);
        });
        loadItem.setOnAction(e -> {
            MenuUtils.loadCircuit(sandboxPane, scene);
        });
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        loadItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        menuBar.getMenus().addAll(menu1);
        ColumnConstraints cc = new ColumnConstraints(WIDTH / 4);
        RowConstraints rc = new RowConstraints(HEIGHT / 40);
        gridPane.getColumnConstraints().addAll(cc, cc, cc, cc);
        gridPane.getRowConstraints().addAll(rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc, rc);
        GridPane.setConstraints(menuBar, 0, 0, 4, 1);
        GridPane.setConstraints(sideBarPane, 0, 1, 1, 39);
        GridPane.setConstraints(infoPane, 1, 40, 3, 5);

        gridPane.getChildren().addAll(sideBarPane, infoPane, menuBar);
        stackPane.getChildren().addAll(sandboxPane, gridPane);

        gridPane.setPickOnBounds(false);

        SimpleDoubleProperty xMouse = new SimpleDoubleProperty();
        SimpleDoubleProperty yMouse = new SimpleDoubleProperty();

        scene = new Scene(stackPane, WIDTH, HEIGHT);
        scene.setOnMouseMoved(e -> {
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
                System.out.println("Deleting? " + wire + " " + ((wire != null) ? wire.component() : ""));
                if (wire != null && wire.component() == null) {
                    wire.removeFromPane();
                    sandboxPane.setSelectedObject(null);
                    return;
                }
            }

            if (e.getCode() == KeyCode.W) {
                if (sandboxPane.getWireDrag() != null) {
                    return;
                }

                SandboxWire wire = new SandboxWire(sandboxPane);
                wire.initialize(xMouse.get(), yMouse.get());
            }

            if (e.getCode() == KeyCode.S) {
                System.out.println("Running simulation...");
                if (!sandboxPane.runSimulation()) {
                    System.out.println("Failure!!!!");
                }

            }

            if (e.getCode() == KeyCode.C) {
                gridPane.getChildren().forEach(item -> {
                    if (!item.equals(menuBar)) {
                        item.setVisible(!item.isVisible());
                    }
                });
            }
        });

        stage.setTitle("Electric Circuit Builder");
        stage.setScene(scene);
        stage.show();
    }
}
