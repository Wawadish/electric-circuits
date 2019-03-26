package electric.circuits;

import electric.circuits.SandboxWire.WireDragData;
import electric.circuits.component.BatteryComponent;
import electric.circuits.data.ComponentType;
import electric.circuits.data.ElectricComponent;
import electric.circuits.data.Variable;
import electric.circuits.simulation.SimulationContext;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
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
	public static final int MAX_GRID_X = (int) (PREF_WIDTH / GRID_SIZE);
	public static final int MAX_GRID_Y = (int) (PREF_HEIGHT / GRID_SIZE);

	private final SimulationContext simulation;
	private final Set<SandboxComponent> components;
	private Object selectedObject;

	private WireDragData wireDragData;

	public SandboxPane() {
		this.components = new HashSet<>();
		this.simulation = new SimulationContext();

		setStyle("-fx-background-color: green;");
		setPrefSize(PREF_WIDTH, PREF_HEIGHT);

		this.setOnDragOver(e -> {
			e.acceptTransferModes(TransferMode.COPY, TransferMode.MOVE);

			// Dragging a wire
			if (e.getTransferMode() == TransferMode.MOVE) {
				double mouseX = e.getX();
				double mouseY = e.getY();

				wireDragData.getCircle().setCenterX(mouseX);
				wireDragData.getCircle().setCenterY(mouseY);

			}
		});

		this.setOnDragDropped(e -> {
			if (e.getTransferMode() == TransferMode.COPY) {
				Image image = (Image) e.getDragboard().getContent(DataFormat.IMAGE);
				ComponentType type = (ComponentType) e.getDragboard().getContent(DataFormat.PLAIN_TEXT);

				double mouseX = e.getX() - (image.getWidth() / 2);
				double mouseY = e.getY() - (image.getHeight() / 2);
				addComponent(Utils.toGrid(mouseX), Utils.toGrid(mouseY), type);
			}
		});

		this.setOnMousePressed(e -> {
			selectedObject = null;
			System.out.println("unselecting");
		});
	}

	public Set<SandboxComponent> components() {
		return components;
	}

	public void addComponent(int x, int y, ComponentType comp) {
		x = Math.max(0, Math.min(MAX_GRID_X, x));
		y = Math.max(0, Math.min(MAX_GRID_Y, y));

		ElectricComponent ec = comp.create(simulation);
		if (!(ec instanceof BatteryComponent))
			ec.setResistance(1);

		addComponent(x, y, ec);
	}

	public void addComponent(int x, int y, ElectricComponent ec) {
		SandboxComponent sc = new SandboxComponent(this, ec);
		sc.move(x, y);
		sc.initialize();
		components.add(sc);
		selectedObject = sc;
	}

	public void deleteComponent(SandboxComponent comp) {
		comp.removeFromPane();
		components.remove(comp);
	}
	
	public void clearComponents() {
		components.forEach(SandboxComponent::removeFromPane);
		components.clear();
	}

	public void setSelectedObject(Object selectedComponent) {
		this.selectedObject = selectedComponent;
	}

	public SandboxComponent getSelectedComponent() {
		return selectedObject instanceof SandboxComponent ? (SandboxComponent) selectedObject : null;
	}

	public SandboxWire getSelectedWire() {
		return selectedObject instanceof SandboxWire ? (SandboxWire) selectedObject : null;
	}

	public WireDragData getWireDrag() {
		return wireDragData;
	}

	public WireDragData endWireDrag() {
		WireDragData wdd = wireDragData;
		wireDragData = null;
		return wdd;
	}

	public void startWireDrag(WireDragData wireDragData) {
		this.wireDragData = wireDragData;
		Dragboard db = startDragAndDrop(TransferMode.MOVE);
		ClipboardContent cc = new ClipboardContent();
		cc.putString("wire123");
		db.setContent(cc);
	}

	public boolean runSimulation() {
		simulation.clearVariables();

		BatteryComponent battery = (BatteryComponent) components.stream()
				.map(SandboxComponent::getComponent)
				.filter(c -> c instanceof BatteryComponent)
				.findAny().orElse(null);

		if (battery == null)
			return false;

		simulation.runSimulation(battery);
		components.stream().forEach(sc -> {
			ElectricComponent comp = sc.getComponent();
			Variable current = comp.current();
			if (comp.getType() == ComponentType.LED)
				sc.setImage((!current.resolve() || Utils.equals(current.get(), 0)) 
						? ComponentType.LED_OFF 
						: ComponentType.LED_ON);

			if (!current.resolve())
				return;

			System.out.println("Component " + comp + ": " + current.get() + " A");
		});
		return true;
	}
}
