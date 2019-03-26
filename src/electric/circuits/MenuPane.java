/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import electric.circuits.component.BatteryComponent;
import electric.circuits.data.ComponentType;
import electric.circuits.data.ElectricComponent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

/**
 *
 * @author Wawa
 */
public class MenuPane extends Pane {

	private final Button saveButton;

	private final Button loadButton;

	private final CheckBox realTime;
	private final Label realTimeLabel;
	private final SandboxPane sandboxPane;

	Set<BatteryComponent> batterySet;
	Set<ElectricComponent> resistor_set;
	Set<ElectricComponent> led_set;

	ComponentType type;

	public MenuPane(SandboxPane sandbox) {
		this.sandboxPane = sandbox;

		setStyle("-fx-background-color: black;");
		setPrefSize(Main.WIDTH - Main.WIDTH / 5, Main.HEIGHT / 10);

		saveButton = new Button("Save");
		saveButton.setLayoutX(700);
		saveButton.setLayoutY(20);

		loadButton = new Button("Load");
		loadButton.setLayoutX(900);
		loadButton.setLayoutY(20);

		realTime = new CheckBox();
		realTime.setLayoutX(450);
		realTime.setLayoutY(25);

		realTimeLabel = new Label("Real-Time Simulation");
		realTimeLabel.setLayoutX(300);
		realTimeLabel.setLayoutY(25);
		realTimeLabel.setTextFill(Color.WHITE);

		getChildren().addAll(saveButton, loadButton, realTime, realTimeLabel);

		saveButton.setOnAction(e -> saveCircuit());
		loadButton.setOnAction(e -> loadCircuit());
	}

	private void loadCircuit() {
		Set<SandboxComponent> com = sandboxPane.components();

		for (SandboxComponent c : com) {
			sandboxPane.deleteComponent(c);
		}

		try {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));

			chooser.setTitle("Open Circuit File");
			File file = chooser.showOpenDialog(loadButton.getScene().getWindow());

			if (file != null) {
				System.out.println(readJsonFile(file.toString()));
				JsonObject root = new JsonParser().parse(file.getAbsolutePath()).getAsJsonObject();

				JsonArray battery_array = root.getAsJsonArray("Battery");
				for (int i = 0; i < battery_array.size(); i++) {
					JsonObject battery_object = battery_array.get(i).getAsJsonObject();
					double positionx = battery_object.get("Positionx").getAsDouble();
					double positiony = battery_object.get("Positiony").getAsDouble();
					double voltage = battery_object.get("Voltage").getAsDouble();

				}

				JsonArray resistor_array = root.getAsJsonArray("Resistor");
				for (int i = 0; i < resistor_array.size(); i++) {
					JsonObject resistor_object = battery_array.get(i).getAsJsonObject();
					double positionx = resistor_object.get("Positionx").getAsDouble();
					double positiony = resistor_object.get("Positiony").getAsDouble();
					double voltage = resistor_object.get("Resistor").getAsDouble();
				}

				JsonArray led_array = root.getAsJsonArray("LED");
				for (int i = 0; i < led_array.size(); i++) {
					JsonObject led_object = battery_array.get(i).getAsJsonObject();
					double positionx = led_object.get("Positionx").getAsDouble();
					double positiony = led_object.get("Positiony").getAsDouble();
					double voltage = led_object.get("Resistor").getAsDouble();
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void saveCircuit() {
		Set<SandboxComponent> com = sandboxPane.components();

		JSONObject obj = new JSONObject();
		JSONArray battery_array = new JSONArray();
		JSONArray resistor_array = new JSONArray();
		JSONArray led_array = new JSONArray();

		try {
			for (SandboxComponent c : com) {
				type = c.getComponent().getType();

				if (type == ComponentType.BATTERY) {
					BatteryComponent b = (BatteryComponent) c.getComponent();
					Map map = new LinkedHashMap();
					map.put("Positionx", c.getGridX());
					map.put("Positiony", c.getGridY());
					map.put("Voltage", b.voltage());
					battery_array.add(map);
				}

				if (type == ComponentType.RESISTOR) {
					Map map = new LinkedHashMap();
					map.put("Positionx", c.getGridX());
					map.put("Positiony", c.getGridY());
					map.put("Resistance", c.getComponent().resistance());
					resistor_array.add(map);
				}

				if (type == ComponentType.LED) {
					Map map = new LinkedHashMap();
					map.put("Positionx", c.getGridX());
					map.put("Positiony", c.getGridY());
					map.put("Resistance", c.getComponent().resistance());
					led_array.add(map);
				}
			}

			obj.put("Batteries", battery_array);
			obj.put("Resistors", resistor_array);
			obj.put("LEDs", led_array);

			FileWriter writer = new FileWriter(".\\sample.json");
			writer.write(obj.toJSONString());
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	//to read the file and compare with the output when parsing
	private String readJsonFile(String fileName) {
		try {
			String jsonString = "";
			FileInputStream fileInput = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fileInput));
			String inputLine = "";

			while ((inputLine = in.readLine()) != null) {
				jsonString += inputLine;

			}

			in.close();
			fileInput.close();
			return jsonString;

		} catch (Exception e) {
			System.out.println("File not found");
			return "";
		}

	}

}
