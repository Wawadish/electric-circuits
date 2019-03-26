package electric.circuits;

import electric.circuits.component.BatteryComponent;
import electric.circuits.data.ElectricComponent;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 *
 * @author Wawa
 */
public class InfoPane extends Pane {

	// creates labels for the elements in the indo panne
	public static Label topTitle;
	public static Label k_title;
	public static Label o_title;
	public static Label voltageLabel;
	public static Label resistanceLabel;

	// creates textt fields to input data that will link to the components in the sandbox, i.e voltageLabel, resistanceLabel, etc
	public static TextField kir_eq;
	public static TextField ohm_eq;
	public static TextField voltageTextField;
	public static TextField resistanceTextField;

	
	public InfoPane() {
		//sets colour and size of the pane
		super();
		//this.sandbox = new SandboxPane();
		setStyle("-fx-background-color: #BDC3C7;");

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

		voltageLabel = new Label("Enter Voltage: ");
		voltageLabel.setLayoutY(50);
		voltageLabel.setLayoutX(700);
		voltageLabel.setVisible(false);

		voltageTextField = new TextField();
		voltageTextField.setMaxWidth(100);
		voltageTextField.setLayoutX(850);
		voltageTextField.setLayoutY(45);
		voltageTextField.setVisible(false);

		resistanceLabel = new Label("Enter resistance: ");
		resistanceLabel.setLayoutX(700);
		resistanceLabel.setLayoutY(100);
		resistanceLabel.setVisible(false);

		resistanceTextField = new TextField();
		resistanceTextField.setLayoutX(850);
		resistanceTextField.setLayoutY(90);
		resistanceTextField.setMaxWidth(100);
		resistanceTextField.setVisible(false);

		//adds elements to the pane
		getChildren().addAll(topTitle, k_title, kir_eq, o_title, ohm_eq, voltageLabel, voltageTextField, resistanceLabel, resistanceTextField);

	}

	//the next 9 methods will return either the labels or text fields contained in the pane. This will be useful when making them visible or hiding
	//them when clicking on compoments
	public Label getTopTitle() {
		return topTitle;
	}

	public Label getKTitle() {
		return k_title;
	}

	public Label getOTitle() {
		return o_title;
	}

	public Label getVTitle() {
		return voltageLabel;
	}

	public Label getRLabel() {
		return resistanceLabel;
	}

	public TextField getKirEq() {
		return kir_eq;
	}

	public TextField getOhmEq() {
		return ohm_eq;
	}

	public TextField getVoltBox() {
		return voltageTextField;
	}

	public TextField getResBox() {
		return resistanceTextField;
	}

	public static void onSelectComponent(ElectricComponent component) {
		if (component instanceof BatteryComponent) {
			voltageLabel.setVisible(true);
			voltageTextField.setVisible(true);
                        resistanceLabel.setVisible(false);
                        resistanceTextField.setVisible(false);
		} else {
			topTitle.setVisible(true);
			k_title.setVisible(true);
			kir_eq.setVisible(true);
			o_title.setVisible(true);
			ohm_eq.setVisible(true);
			resistanceLabel.setVisible(true);
			resistanceTextField.setVisible(true);
			voltageLabel.setVisible(false);
			voltageTextField.setVisible(false);
		}
		
		//this will check if the user inputs a valid number as the value of voltageTextField, if they do not then an error message will display. If they do, their value
		//will be stored in voltage, and then into the component itself
		voltageTextField.setOnAction((ActionEvent e) -> {
			try {
				double voltage = Double.valueOf(voltageTextField.getText());
				if (voltage <= 0) {
					throw new NumberFormatException();
				}

				((BatteryComponent) component).setVoltage(voltage);
				System.out.println("Set voltage: " + voltage);
			} catch (NumberFormatException ex) {
				voltageTextField.setText("Invalid input");
			}
		});
		
		//this will check if the user inputs a valid number as the value of resistanceTextField, if they do not then an error message will display. If they do, their value
		//will be stored in resistance, and then into the component itself
		resistanceTextField.setOnAction((ActionEvent e) -> {
			try {
				double resistance = Double.valueOf(resistanceTextField.getText());
				if (resistance < 0) {
					throw new NumberFormatException();
				}
				
				component.setResistance(resistance);
				System.out.println("Set resistance: " + resistance);
			} catch (NumberFormatException ex) {
				resistanceTextField.setText("Invalid input");
			}
		});
	}

}
