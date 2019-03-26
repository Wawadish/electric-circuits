package electric.circuits;

import electric.circuits.component.BatteryComponent;
import electric.circuits.data.ElectricComponent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

/**
 *
 * @author Wawa
 */
public class InfoPane extends GridPane {

	// creates labels for the elements in the indo panne
	private final Label titleLabel;
	private final Label voltageReadingLabel;
	private final Label currentReadingLabel;
	private final Label voltagePromptLabel;
	private final Label resistancePromptLabel;
	private final Label noElementLabel;

	// creates textt fields to input data that will link to the components in the sandbox, i.e voltageLabel, resistanceLabel, etc
	private final TextField voltageTextField;
	private final TextField resistanceTextField;
	private ElectricComponent selectedComponent;

	public InfoPane() {
		setStyle("-fx-background-color: #dedfe0;");
		setPrefSize(Main.WIDTH - Main.WIDTH / 5, Main.HEIGHT / 4);

		Font textFont = new Font("System", 17);
		Font promptFont = new Font("System", 13);

		// Sets all elements' size, and poistion in the pane
		titleLabel = new Label("Equations and Configurations");
		titleLabel.setFont(new Font("System Bold", 30));

		noElementLabel = new Label("No element selected!");
		noElementLabel.setPadding(new Insets(20, 0, 0, 0));
		noElementLabel.setFont(textFont);

		voltageReadingLabel = new Label("Voltmeter Reading: 0.0V");
		voltageReadingLabel.setFont(textFont);
		currentReadingLabel = new Label("Ampmeter Reading: 0.0A");
		currentReadingLabel.setFont(textFont);

		voltageReadingLabel.setPadding(new Insets(20, 40, 0, 0));
		currentReadingLabel.setPadding(new Insets(20, 40, 0, 0));

		voltagePromptLabel = new Label("Enter Voltage: ");
		voltagePromptLabel.setFont(textFont);
		voltageTextField = new TextField();
		voltageTextField.setMaxWidth(200);
		voltageTextField.setFont(promptFont);

		resistancePromptLabel = new Label("Enter resistance: ");
		resistancePromptLabel.setFont(textFont);
		resistanceTextField = new TextField();
		resistanceTextField.setMaxWidth(200);
		resistanceTextField.setFont(promptFont);

		// Updates the voltage of the battery if a valid, positive number was entered.
		voltageTextField.setOnAction(e -> updateBatteryVoltage());

		// Updates the resistance of the component if a valid, positive number was entered.
		resistanceTextField.setOnAction(e -> updateResistance());

		// Add elements to the pane
		ColumnConstraints constr = new ColumnConstraints();
		constr.setFillWidth(true);
		constr.setHalignment(HPos.CENTER);
		constr.setHgrow(Priority.ALWAYS);
		getColumnConstraints().setAll(constr, constr);

		onSelectComponent(null);
	}

	public void onSelectComponent(ElectricComponent component) {
		this.selectedComponent = component;

		this.getChildren().clear();
		add(titleLabel, 0, 0, 2, 1);

		if (component == null) {
			add(noElementLabel, 0, 1, 2, 1);
			return;
		}

		add(voltageReadingLabel, 0, 1);
		add(currentReadingLabel, 0, 2);

		boolean resolved = component.current().resolve();
		if (resolved) {
			currentReadingLabel.setText(String.format("Ampmeter Reading: %.1f A", component.current().get()));
		}

		HBox box;
		if (component instanceof BatteryComponent) {
			double voltage = ((BatteryComponent) component).voltage();
			voltageTextField.setText("" + voltage);
			voltageReadingLabel.setText(String.format("Voltmeter Reading: %.1f V", voltage));
			box = new HBox(voltagePromptLabel, voltageTextField);

		} else {
			resistanceTextField.setText("" + component.resistance());
			box = new HBox(resistancePromptLabel, resistanceTextField);

			if (resolved) {
				voltageReadingLabel.setText(String.format("Voltmeter Reading: %.1f V", component.resistance() * component.current().get()));
			}

		}

		box.setPadding(new Insets(20, 0, 0, 40));

		add(box, 1, 1);
	}

	private void updateResistance() {
		try {
			double resistance = Double.valueOf(resistanceTextField.getText());
			if (resistance < 0) {
				throw new NumberFormatException();
			}

			selectedComponent.setResistance(resistance);
			System.out.println("Set resistance: " + resistance);
			setSuccess(resistanceTextField);
		} catch (NumberFormatException ex) {
			resistanceTextField.setText("Invalid input");
			setError(resistanceTextField);
		}
	}

	private void updateBatteryVoltage() {
		try {
			double voltage = Double.valueOf(voltageTextField.getText());
			if (voltage <= 0) {
				throw new NumberFormatException();
			}

			voltageReadingLabel.setText(String.format("Voltmeter Reading: %.1f V", voltage));
			((BatteryComponent) selectedComponent).setVoltage(voltage);
			System.out.println("Set voltage: " + voltage);
			setSuccess(voltageTextField);
		} catch (NumberFormatException ex) {
			voltageTextField.setText("");
			voltageTextField.setPromptText("Enter a positive number");
			setError(voltageTextField);
		}
	}

	private void setSuccess(TextField field) {
		field.setStyle("-fx-text-box-border: green; -fx-focus-color: green;");
	}

	private void setError(TextField field) {
		field.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
	}
}
