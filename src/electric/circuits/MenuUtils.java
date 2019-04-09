package electric.circuits;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import electric.circuits.component.BatteryComponent;
import electric.circuits.component.ComponentType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Contains utility methods for loading and saving a circuit.
 *
 * @author Stavros, Tomer
 */
public class MenuUtils {

	// JSON property names
	private static final String VOLTAGE = "Voltage";
	private static final String RESISTANCE = "Resistance";
	private static final String POSITION_X = "Positionx";
	private static final String POSITION_Y = "Positiony";

	/**
	 * Loads a circuit from a user selected file.
	 *
	 * @param sandboxPane the sandbox pane to which the components should be
	 * added.
	 * @param scene the scene of the GUI.
	 */
	public static void loadCircuit(SandboxPane sandboxPane, Scene scene) {
		// Let the user choose the file
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));
		chooser.setTitle("Open Circuit File");
		File file = chooser.showOpenDialog(scene.getWindow());
		if (file == null) {
			return;
		}

		// Start parsing the file
		try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
			JsonObject root = new JsonParser().parse(reader).getAsJsonObject();
			sandboxPane.clearStage();

			// Load batteries
			JsonArray batteriesArray = root.getAsJsonArray("Batteries");
			for (int i = 0; i < batteriesArray.size(); i++) {
				JsonObject batteryObject = batteriesArray.get(i).getAsJsonObject();
				double voltage = batteryObject.get(VOLTAGE).getAsDouble();
				SandboxComponent comp = loadComponent(batteryObject, sandboxPane, ComponentType.BATTERY);
				((BatteryComponent) comp.getComponent()).setVoltage(voltage);

			}

			// Load resistors
			JsonArray resistorsArray = root.getAsJsonArray("Resistors");
			for (int i = 0; i < resistorsArray.size(); i++) {
				JsonObject resistorObject = resistorsArray.get(i).getAsJsonObject();
				double resistance = resistorObject.get(RESISTANCE).getAsDouble();
				SandboxComponent comp = loadComponent(resistorObject, sandboxPane, ComponentType.RESISTOR);
				comp.getComponent().setResistance(resistance);
			}

			// Load LEDs
			JsonArray ledArray = root.getAsJsonArray("LEDs");
			for (int i = 0; i < ledArray.size(); i++) {
				JsonObject ledObject = ledArray.get(i).getAsJsonObject();
				double resistance = ledObject.get(RESISTANCE).getAsDouble();
				SandboxComponent comp = loadComponent(ledObject, sandboxPane, ComponentType.LED);
				comp.getComponent().setResistance(resistance);
			}
		} catch (JsonIOException | JsonSyntaxException | IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Loads a component from a {@code JsonObject}.
	 *
	 * @param jsonObject the JSON object to parse.
	 * @param sandboxPane the sandbox pane to which to add the component.
	 * @param type the type of the component
	 * @return the newly created {@code SandboxComponent}.
	 */
	private static SandboxComponent loadComponent(JsonObject jsonObject, SandboxPane sandboxPane, ComponentType type) {
		int positionx = jsonObject.get(POSITION_X).getAsInt();
		int positiony = jsonObject.get(POSITION_Y).getAsInt();
		return sandboxPane.addComponent(positionx, positiony, type);
	}

	/**
	 * Saves the current circuit to a user-selected file.
	 *
	 * @param sandboxPane the sandbox pane from which to gather the components
	 * to be saved.
	 */
	public static void saveCircuit(SandboxPane sandboxPane) {

		// Let the user choose the file
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));
		File file = chooser.showSaveDialog(null);
		if (file == null) {
			return;
		}

		// Prepare JSON objects.
		JSONObject obj = new JSONObject();
		JSONArray batteriesArray = new JSONArray();
		JSONArray resistorArray = new JSONArray();
		JSONArray ledArray = new JSONArray();

		// Get components to be saved
		Set<SandboxComponent> com = sandboxPane.components();

		// Save the components
		try (FileWriter writer = new FileWriter(file)) {
			for (SandboxComponent c : com) {
				ComponentType type = c.getComponent().getType();

				// Save battery
				if (type == ComponentType.BATTERY) {
					BatteryComponent b = (BatteryComponent) c.getComponent();
					Map<String, Object> map = new LinkedHashMap<>();
					map.put(POSITION_X, c.getGridX());
					map.put(POSITION_Y, c.getGridY());
					map.put(VOLTAGE, b.voltage());
					batteriesArray.add(map);
					continue;
				}

				// Save resistor
				if (type == ComponentType.RESISTOR) {
					Map<String, Object> map = new LinkedHashMap<>();
					map.put(POSITION_X, c.getGridX());
					map.put(POSITION_Y, c.getGridY());
					map.put(RESISTANCE, c.getComponent().resistance());
					resistorArray.add(map);
					continue;
				}

				// Save LED
				if (type == ComponentType.LED) {
					Map<String, Object> map = new LinkedHashMap<>();
					map.put(POSITION_X, c.getGridX());
					map.put(POSITION_Y, c.getGridY());
					map.put(RESISTANCE, c.getComponent().resistance());
					ledArray.add(map);
					continue;
				}

				throw new AssertionError("Saving of type not implemented: " + type);
			}

			// Write the JSON to the file
			obj.put("Batteries", batteriesArray);
			obj.put("Resistors", resistorArray);
			obj.put("LEDs", ledArray);
			obj.writeJSONString(writer);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
