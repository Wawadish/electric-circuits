/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Wawa
 */
public class Main extends Application {

    private HBox hbox = new HBox();
    private VBox vbox = new VBox();
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        hbox.getChildren().add(vbox);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        Scene scene = new Scene(hbox, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
