package electric.circuits;

import electric.circuits.component.DummyComponent;
import java.awt.Point;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
    public static double draggedX;
    public static double draggedY;

    private final SandboxPane sandboxPane = new SandboxPane();

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        vbox.getChildren().addAll(new MenuPane(), sandboxPane, new InfoPane());
        hbox.getChildren().addAll(new SideBarPane(), vbox);
        stackPane.getChildren().add(hbox);
        stackPane.getChildren().add(pane);

        pane.setPickOnBounds(false);

        stackPane.setPrefHeight(vbox.getHeight());
        stackPane.setPrefWidth(hbox.getWidth());

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        Scene scene = new Scene(stackPane, WIDTH, HEIGHT);

//        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                Bounds sandboxBoundsInScene = sandboxPane.localToScene(sandboxPane.getBoundsInLocal());
//                System.out.println(sandboxBoundsInScene.getMinY() + " " + sandboxBoundsInScene.getMaxY());
//                
//                pane.getChildren().remove(SideBarPane.temporaryImage);
//                draggedX = SideBarPane.temporaryImage.getX();
//                draggedY = SideBarPane.temporaryImage.getY();
//                
//                if(draggedY > sandboxBoundsInScene.getMinY() && 
//                        draggedY < sandboxBoundsInScene.getMaxY() &&
//                        draggedX > sandboxBoundsInScene.getMinX()){
//                    System.out.println("in bounds");
//                    int gridX, gridY;
//                    gridX = (int)((draggedX - sandboxBoundsInScene.getMinX())/30.0);
//                    gridY = (int)((draggedY - sandboxBoundsInScene.getMinY())/30.0);
//                    System.out.println("gridX : " + gridX + "\tgridY : " + gridY);
//                    
//                }
//                
//            }
//        });
        primaryStage.setScene(scene);
        primaryStage.show();
        MenuPane pane = new MenuPane();
    }
}
