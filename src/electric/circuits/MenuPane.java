/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Wawa
 */
public class MenuPane extends Pane{
    
    
    private Button save_button;
    
    private Button load_button;
   
    private CheckBox real_time;
    private Label real_time_label;
    
    
    
   
    
    
    
    public MenuPane(){
        super();
        setStyle("-fx-background-color: black;");
        setPrefSize(Main.WIDTH - Main.WIDTH / 4,Main. HEIGHT / 5);
        
         save_button = new Button("Save");
         save_button.setLayoutX(100);
         save_button.setLayoutY(20);
    
          load_button = new Button("Load");
          load_button.setLayoutX(200);
          load_button.setLayoutY(20);
   
        real_time = new CheckBox();
        real_time.setLayoutX(420);
        real_time.setLayoutY(25);
        
        real_time_label = new Label("Real-Time Simulation");
        real_time_label.setLayoutX(300);
        real_time_label.setLayoutY(25);
        real_time_label.setTextFill(Color.WHITE);
        
        getChildren().addAll(save_button, load_button, real_time,real_time_label);
        
    }
    
}
