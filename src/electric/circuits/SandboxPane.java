package electric.circuits;

import electric.circuits.component.DummyBatteryComponent;
import electric.circuits.component.DummyComponent;
import electric.circuits.data.ComponentType;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Wawa
 */
public class SandboxPane extends AnchorPane {

	private static final double PREF_WIDTH = Main.WIDTH - (Main.WIDTH / 5);
	private static final double PREF_HEIGHT = Main.HEIGHT - (Main.HEIGHT / 10) - (Main.HEIGHT / 4);

	// TODO: fix max grid size
	public static final double GRID_SIZE = 30;
	public static final int MAX_GRID_X = (int)(PREF_WIDTH / GRID_SIZE);
	public static final int MAX_GRID_Y = (int)(PREF_HEIGHT / GRID_SIZE);
	
	private final SimulationContext simulation;
	private final Set<SandboxComponent> components;

	public SandboxPane() {
		this.components = new HashSet<>();
		this.simulation = new SimulationContext();

		setStyle("-fx-background-color: red;");
		setPrefSize(PREF_WIDTH, PREF_HEIGHT);

		addDummyComponents();

		this.setOnDragOver(e -> {
			e.acceptTransferModes(TransferMode.COPY);
			e.consume();
		});

		this.setOnDragDropped(e -> {
			ComponentType type = (ComponentType) e.getDragboard().getContent(DataFormat.PLAIN_TEXT);
			addComponent((int) (e.getX() / GRID_SIZE), (int) (e.getY() / GRID_SIZE), type);

		});
	}

	public Set<SandboxComponent> components() {
		return components;
	}

	private void addDummyComponents() {
		SandboxComponent battery = new SandboxComponent(this, new DummyBatteryComponent(simulation, "Battery", 10));
		SandboxComponent led1 = new SandboxComponent(this, new DummyComponent(simulation, ComponentType.LED, "LED1"));
		SandboxComponent led2 = new SandboxComponent(this, new DummyComponent(simulation, ComponentType.LED, "LED2"));
		SandboxComponent led3 = new SandboxComponent(this, new DummyComponent(simulation, ComponentType.LED, "LED3"));
		SandboxComponent res = new SandboxComponent(this, new DummyComponent(simulation, ComponentType.RESISTOR, "RES1"));

		battery.move(5, 5);
		led1.move(10, 5);
		led2.move(10, 10);
		led3.move(10, 15);
		res.move(15, 15);

		components.addAll(Arrays.asList(battery, led1, led2, led3, res));
		components.forEach(SandboxComponent::initialize);
	}

	public void addComponent(int x, int y, ComponentType comp) {
		x = Math.max(0, Math.min(MAX_GRID_X, x));
		y = Math.max(0, Math.min(MAX_GRID_Y, y));

		addComponent(x, y, new ElectricComponent(simulation, comp));
	}

	public void addComponent(int x, int y, ElectricComponent ec) {
		SandboxComponent sc = new SandboxComponent(this, ec);
		sc.move(x, y);
		sc.initialize();
		components.add(sc);
	}

}
