/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import javafx.scene.layout.Pane;

/**
 *
 * @author Wawa
 */
public class SandboxPane extends Pane{

    public SandboxPane(){
        super();
        setStyle("-fx-background-color: red;");
        setPrefSize(Main.WIDTH - Main.WIDTH / 4,Main. HEIGHT - Main.HEIGHT/10 - Main.HEIGHT/4);
    }
}
