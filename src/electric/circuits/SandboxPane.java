package electric.circuits;

import electric.circuits.component.DummyBatteryComponent;
import electric.circuits.component.DummyComponent;
import electric.circuits.data.ElectricComponent;
import electric.circuits.data.Variable;
import electric.circuits.simulation.SimulationContext;
import java.util.IdentityHashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import electric.circuits.simulation.SimulationContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Wawa
 */
public class SandboxPane extends AnchorPane {

	public static final double GRID_SIZE = 30;

	private final SimulationContext simulation;
	private final Set<SandboxComponent> components;

	public SandboxPane() {
		this.components = new HashSet<>();
		this.simulation = new SimulationContext();

		setStyle("-fx-background-color: red;");
		setPrefSize(Main.WIDTH - Main.WIDTH / 5, Main.HEIGHT - Main.HEIGHT / 10 - Main.HEIGHT / 4);

		addDummyComponents();
	}

	private void addDummyComponents() {
		SandboxComponent battery = new SandboxComponent(this, new DummyBatteryComponent(simulation, "Battery", 10));
		SandboxComponent led1 = new SandboxComponent(this, new DummyComponent(simulation, "LED1"));
		SandboxComponent led2 = new SandboxComponent(this, new DummyComponent(simulation, "LED2"));
		SandboxComponent led3 = new SandboxComponent(this, new DummyComponent(simulation, "LED3"));
		SandboxComponent res = new SandboxComponent(this, new DummyComponent(simulation, "RES1"));

		battery.move(10, 10);
		led1.move(15, 10);
		led2.move(15, 15);
		led3.move(15, 20);
		res.move(20, 20);
		
		components.addAll(Arrays.asList(battery, led1, led2, led3, res));
		components.forEach(SandboxComponent::initialize);
	}
}
