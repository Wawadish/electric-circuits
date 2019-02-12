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
public class InfoPane extends Pane{
    
    
    public InfoPane(){
        super();
        setStyle("-fx-background-color: orange;");
        setPrefSize(Main.WIDTH - Main.WIDTH / 4,Main. HEIGHT / 4);
    }
    
}
