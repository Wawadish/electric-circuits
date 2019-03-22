package electric.circuits;

import electric.circuits.component.BatteryComponent;
import electric.circuits.data.ElectricComponent;
import java.util.InputMismatchException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 *
 * @author Wawa
 */
public class InfoPane extends Pane {

    //creates labels for the elements in the indo panne
	public static Label topTitle;
	public static Label k_title;
	public static Label o_title;
	public static Label voltage;
	public static Label resistance;
        
        
        
       
        

        //creates textt fields to input data that will link to the components in the sandbox, i.e voltage, resistance, etc
	public static TextField kir_eq;
	public static TextField ohm_eq;
	public static TextField voltage_box;
	public static TextField resistance_box;
        
        //variables that will hold the values entered in the text fields
      

	public InfoPane() 
        {
            //sets colour and size of the pane
		super();
                //this.sandbox = new SandboxPane();
		setStyle("-fx-background-color: #BDC3C7;");
		setPrefSize(Main.WIDTH - Main.WIDTH / 5, Main.HEIGHT / 4);

		//sets all elements' size, and poistion in the pane
		topTitle = new Label("Equations and Configurations");
		topTitle.setLayoutX(400);
		topTitle.setFont(new Font(20));
                topTitle.setVisible(false);

		k_title = new Label("Kirchhoff's Equation: ");
		k_title.setLayoutX(50);
		k_title.setLayoutY(50);
                k_title.setVisible(false);

		kir_eq = new TextField();
		kir_eq.setLayoutY(40);
		kir_eq.setLayoutX(200);
                kir_eq.setVisible(false);

		o_title = new Label("Ohm's Law: ");
		o_title.setLayoutY(100);
		o_title.setLayoutX(50);
                o_title.setVisible(false);

		ohm_eq = new TextField();
		ohm_eq.setLayoutX(200);
		ohm_eq.setLayoutY(95);
                ohm_eq.setVisible(false);

		voltage = new Label("Enter Voltage: ");
		voltage.setLayoutY(50);
		voltage.setLayoutX(700);
                voltage.setVisible(false);

		voltage_box = new TextField();
		voltage_box.setMaxWidth(100);
		voltage_box.setLayoutX(850);
		voltage_box.setLayoutY(45);
                voltage_box.setVisible(false);

		resistance = new Label("Enter resistance: ");
		resistance.setLayoutX(700);
		resistance.setLayoutY(100);
                resistance.setVisible(false);

		resistance_box = new TextField();
		resistance_box.setLayoutX(850);
		resistance_box.setLayoutY(90);
		resistance_box.setMaxWidth(100);
                resistance_box.setVisible(false);

                //adds elements to the pane
		getChildren().addAll(topTitle, k_title, kir_eq, o_title, ohm_eq, voltage, voltage_box, resistance, resistance_box);
                
                
               
                
                
                
	}
        
        //the next 9 methods will return either the labels or text fields contained in the pane. This will be useful when making them visible or hiding
        //them when clicking on compoments
        public Label getTopTitle()
        {
            return topTitle;
        }
        
        public Label getKTitle()
        {
            return k_title;
        }
        
        public Label getOTitle()
        {
            return o_title;
        }
        
        public Label getVTitle()
        {
            return voltage;
        }
        
        public Label getRLabel()
        {
            return resistance;
        }
        
        public TextField getKirEq()
        {
            return kir_eq;
        }
        
        public TextField getOhmEq()
        {
            return ohm_eq;
        }
        
        public TextField getVoltBox()
        {
            return voltage_box;
        }
        
        public TextField getResBox()
        {
            return resistance_box;
        }
        
        
        
        
        
        
       


}
