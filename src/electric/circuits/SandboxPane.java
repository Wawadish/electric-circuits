/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import electric.circuits.data.ElectricComponent;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Wawa
 */
public class SandboxPane extends Pane {

    private final Set<SandboxComponent> components = new HashSet<>();

    public SandboxPane() {
        super();
        setStyle("-fx-background-color: red;");
        setPrefSize(Main.WIDTH - Main.WIDTH / 5, Main.HEIGHT - Main.HEIGHT / 10 - Main.HEIGHT / 4);

        //****Add possible confirm button to add all junctions *****////
        //adds the junctions to the component placed in sandbox

    }

    private void drawJunctions() {
        for (SandboxComponent c : components) {
            ImageView img = new ImageView((Image) null);

            //gets the width and height of the image of the component
            double width = img.getImage().getWidth();
            double height = img.getImage().getHeight();

            //gets the right and left x coordinate of the image
            double left_x = img.getX();
            double right_x = img.getX() + width;

            //gets the y coordinate of the midpoint of the image
            double bottom = img.getY() - height;
            double midpoint = (img.getY() - bottom) / 2;

            //creates a circle that acts as a junction for the left side of the image
            Circle leftC = new Circle(left_x, midpoint, 10);
            leftC.setFill(Color.WHITE);
            leftC.setStroke(Color.BLACK);

            //creates a circle that acts as a junction for the right side of the image
            Circle rightC = new Circle(right_x, midpoint, 10);
            rightC.setFill(Color.WHITE);
            rightC.setStroke(Color.BLACK);

            //adds the junctions to the pane
            getChildren().addAll(leftC, rightC);

        }
    }
}

