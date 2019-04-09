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
 * The Information and Output Pane allows the user to configure the voltage of
 * batteries and the resistance of components, as well as viewing the results of
 * the simulation, including the current and the voltage passing through each
 * component.
 *
 * Whenever a component is (de)selected, the info pane rebuilds itself to show
 * relevant information for each component.
 *
 * @author Wawa, Tomer Moran
 */
public class InfoPane extends GridPane {

	// Label texts of the info pane
	private final Label titleLabel;
	private final Label voltageReadingLabel;
	private final Label currentReadingLabel;
	private final Label voltagePromptLabel;
	private final Label resistancePromptLabel;
	private final Label noElementLabel;

	// Configurable text fields of the info pane
	private final TextField voltageTextField;
	private final TextField resistanceTextField;

	private final Main main;
	private ElectricComponent selectedComponent;

	public InfoPane(Main main) {
		this.main = main;

		noElementLabel = new Label("No element selected!");
		titleLabel = new Label("Equations and Configurations");
		currentReadingLabel = new Label("Ampmeter Reading: 0.0A");
		voltageReadingLabel = new Label("Voltmeter Reading: 0.0V");

		voltageTextField = new TextField();
		resistanceTextField = new TextField();
		voltagePromptLabel = new Label("Enter Voltage: ");
		resistancePromptLabel = new Label("Enter resistance: ");

		initInfoPane();

		// Add elements to the pane
		ColumnConstraints constr = new ColumnConstraints();
		constr.setFillWidth(true);
		constr.setHalignment(HPos.CENTER);
		constr.setHgrow(Priority.ALWAYS);
		getColumnConstraints().setAll(constr, constr);

		onSelectComponent(null);
	}

	/**
	 * Initializes this {@code InfoPane} by styling all the components.
	 */
	private void initInfoPane() {
		setStyle("-fx-background-color: #dedfe0;");
		setPrefSize(Main.WIDTH - Main.WIDTH / 5, Main.HEIGHT / 4);

		Font textFont = new Font("System", 17);
		Font promptFont = new Font("System", 13);
		Font titleFont = new Font("System Bold", 30);

		// Style the components
		noElementLabel.setPadding(new Insets(20, 0, 0, 0));
		voltageReadingLabel.setPadding(new Insets(20, 40, 0, 0));
		currentReadingLabel.setPadding(new Insets(20, 40, 0, 0));

		titleLabel.setFont(titleFont);
		noElementLabel.setFont(textFont);
		voltageReadingLabel.setFont(textFont);
		currentReadingLabel.setFont(textFont);
		voltagePromptLabel.setFont(textFont);
		resistancePromptLabel.setFont(textFont);
		voltageTextField.setFont(promptFont);
		resistanceTextField.setFont(promptFont);

		voltageTextField.setMaxWidth(200);
		resistanceTextField.setMaxWidth(200);

		// Updates the voltage of the battery if a valid, positive number was entered.
		voltageTextField.setOnAction(e -> updateBatteryVoltage());

		// Updates the resistance of the component if a valid, positive number was entered.
		resistanceTextField.setOnAction(e -> updateResistance());
	}

	/**
	 * Invoked when a component is selected or unselected. Rebuilds the info
	 * pane and displays the appropriate fields.
	 *
	 * @param component the newly selected {@code ElectricComponent}, or
	 * {@code null} if unselecting.
	 */
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
			double current = Math.abs(component.current().get());
			currentReadingLabel.setText(String.format("Ampmeter Reading: %.1f A", current));
		} else {
			currentReadingLabel.setText("Ampmeter Reading: ---");
		}

		HBox box;
		if (component instanceof BatteryComponent) {
			double voltage = Math.abs(((BatteryComponent) component).voltage());
			voltageTextField.setText("" + voltage);
			voltageReadingLabel.setText(String.format("Voltmeter Reading: %.1f V", voltage));
			box = new HBox(voltagePromptLabel, voltageTextField);

		} else {
			resistanceTextField.setText("" + component.resistance());
			box = new HBox(resistancePromptLabel, resistanceTextField);

			if (resolved) {
				double voltage = Math.abs(component.resistance() * component.current().get());
				voltageReadingLabel.setText(String.format("Voltmeter Reading: %.1f V", voltage));
			} else {
				voltageReadingLabel.setText("Voltmeter Reading: ---");
			}

		}

		box.setPadding(new Insets(20, 0, 0, 40));

		add(box, 1, 1);
	}

	/**
	 * Updates the resistance of the currently selected
	 * {@code ElectricComponent} depending on the value entered by the user in
	 * the resistance text field.
	 */
	private void updateResistance() {
		try {
			double resistance = Double.valueOf(resistanceTextField.getText());
			if (resistance < 0) {
				throw new NumberFormatException();
			}

			selectedComponent.setResistance(resistance);
			Utils.debug("Set resistance: " + resistance);
			setSuccess(resistanceTextField);

			main.getSandboxPane().runSimulation();
		} catch (NumberFormatException ex) {
			resistanceTextField.setText("Invalid input");
			setError(resistanceTextField);
		}
	}

	/**
	 * Updates the voltage of the currently selected {@code BatteryComponent}
	 * depending on the value entered by the user in the voltage text field.
	 */
	private void updateBatteryVoltage() {
		try {
			double voltage = Double.valueOf(voltageTextField.getText());
			if (voltage <= 0) {
				throw new NumberFormatException();
			}

			voltageReadingLabel.setText(String.format("Voltmeter Reading: %.1f V", voltage));
			((BatteryComponent) selectedComponent).setVoltage(voltage);
			Utils.debug("Set voltage: " + voltage);
			setSuccess(voltageTextField);

			main.getSandboxPane().runSimulation();
		} catch (NumberFormatException ex) {
			voltageTextField.setText("");
			voltageTextField.setPromptText("Enter a positive number");
			setError(voltageTextField);
		}
	}

	/**
	 * Styles a {@code TextField} to indicate success.
	 *
	 * @param field the {@code TextField} to style.
	 */
	private void setSuccess(TextField field) {
		field.setStyle("-fx-text-box-border: green; -fx-focus-color: green;");
	}

	/**
	 * Styles a {@code TextField} to indicate failure.
	 *
	 * @param field the {@code TextField} to style.
	 */
	private void setError(TextField field) {
		field.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
	}
}
