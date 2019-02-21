/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

/**
 *
 * @author Wawa
 */
public class InfoPane extends Pane{
    
    private Label topTitle;
    private Label k_title;
    private Label o_title;
    private Label voltage;
    private Label resistance;
    
    private TextField kir_eq;
    private TextField ohm_eq;
    private TextField voltage_box;
    private TextField resistance_box;
    
    
    
    
    
    
    
    public InfoPane(){
        super();
        setStyle("-fx-background-color: orange;");
        setPrefSize(Main.WIDTH - Main.WIDTH / 5,Main. HEIGHT / 4);
        
       // GridPane grid = new GridPane();
       // grid.getColumnConstraints().addAll(constr(HPos.LEFT), constr(HPos.CENTER),constr(HPos.RIGHT));
       // grid.addColumn(0, k_title);
       
        
        topTitle = new Label("Equations and Configurations");
        topTitle.setLayoutX(400);
        topTitle.setFont(new Font(20));
        
        k_title = new Label("Kirchhoff's Equation: ");
        k_title.setLayoutX(50);
        k_title.setLayoutY(50);
        
        kir_eq = new TextField();
        kir_eq.setLayoutY(40);
        kir_eq.setLayoutX(200);
        
        o_title = new Label("Ohm's Law: ");
        o_title.setLayoutY(100);
        o_title.setLayoutX(50);
        
        ohm_eq = new TextField();
        ohm_eq.setLayoutX(200);
        ohm_eq.setLayoutY(95);
        
        voltage = new Label("Enter Voltage: ");
        voltage.setLayoutY(50);
        voltage.setLayoutX(700);
        
        voltage_box = new TextField();
        voltage_box.setMaxWidth(100);
        voltage_box.setLayoutX(850);
        voltage_box.setLayoutY(45);
        
        resistance = new Label("Enter resistance: ");
        resistance.setLayoutX(700);
        resistance.setLayoutY(100);
        
        resistance_box = new TextField();
        resistance_box.setLayoutX(850);
        resistance_box.setLayoutY(90);
        resistance_box.setMaxWidth(100);
        
        
        
       
        
        
       
        getChildren().addAll(topTitle, k_title,kir_eq, o_title, ohm_eq,voltage,voltage_box,resistance,resistance_box);
    }
    /*
    
    private static ColumnConstraints constr(HPos hpos) {
		ColumnConstraints constr = new ColumnConstraints();
		constr.setFillWidth(true);
		constr.setHgrow(Priority.ALWAYS);
		constr.setHalignment(hpos);
		return constr;
	}*/
    
}
