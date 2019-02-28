package electric.circuits;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Wawa
 */
public class Main extends Application {

    private HBox hbox = new HBox();
    private VBox vbox = new VBox();
    public static StackPane stackPane = new StackPane();
    public static Pane pane = new Pane();
    public static final int WIDTH = 1280, HEIGHT = 960;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        vbox.getChildren().addAll(new MenuPane(), new SandboxPane(), new InfoPane());
        hbox.getChildren().addAll(new SideBarPane(), vbox);
        stackPane.getChildren().add(hbox);
        stackPane.getChildren().add(pane);

        pane.setPickOnBounds(false);

        stackPane.setPrefHeight(vbox.getHeight());
        stackPane.setPrefWidth(hbox.getWidth());

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        Scene scene = new Scene(stackPane, WIDTH, HEIGHT);

        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().remove(SideBarPane.temporaryImage);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        MenuPane pane = new MenuPane();
    }
}
