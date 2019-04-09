package electric.circuits;

import electric.circuits.SandboxWire.WireDragData;
import electric.circuits.component.BatteryComponent;
import electric.circuits.component.ComponentType;
import electric.circuits.component.ElectricComponent;
import electric.circuits.simulation.Variable;
import electric.circuits.simulation.SimulationContext;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

/**
 * The sandbox pane, where the circuit is built.
 *
 * @author Wawa, Tomer Moran
 */
public class SandboxPane extends AnchorPane {

	// The preferred width and height of the sandbox pane.
	private static final double PREF_WIDTH = Main.WIDTH;
	private static final double PREF_HEIGHT = Main.HEIGHT;

	// Defines the grid constants for the sandbox pane.
	public static final double GRID_SIZE = 30;
	public static final int MAX_GRID_X = (int) (PREF_WIDTH / GRID_SIZE);
	public static final int MAX_GRID_Y = (int) (PREF_HEIGHT / GRID_SIZE);

	private final Main main;
	private final SimulationContext simulation;
	private final Set<SandboxComponent> components;

	private Object selectedObject;
	private WireDragData wireDragData;

	public SandboxPane(Main main) {
		this.main = main;
		this.components = new HashSet<>();
		this.simulation = new SimulationContext();

		setStyle("-fx-background-color: green;");
		setPrefSize(PREF_WIDTH, PREF_HEIGHT);

		this.setOnDragOver(this::onDragOver);
		this.setOnDragDropped(this::onDragDropped);

		this.setOnMousePressed(e -> {
			setSelectedObject(null);
			Utils.debug("unselecting");
		});
	}

	/**
	 * Handles a drag drop event. This is invoked whenever a component is
	 * dropped (after being dragged) on the sandbox pane, and results in the
	 * creation of a new component.
	 *
	 * @param e the {@code DragEvent} instance.
	 */
	private void onDragDropped(DragEvent e) {
		// Check that a component is being dragged (and not a wire)
		if (e.getTransferMode() != TransferMode.COPY) {
			return;
		}

		// Get the type of the component being dragged.
		Image image = (Image) e.getDragboard().getContent(DataFormat.IMAGE);
		ComponentType type = (ComponentType) e.getDragboard().getContent(DataFormat.PLAIN_TEXT);

		// Get mouse position and place the new component
		double mouseX = e.getX() - (image.getWidth() / 2);
		double mouseY = e.getY() - (image.getHeight() / 2);
		addComponent(Utils.toGrid(mouseX), Utils.toGrid(mouseY), type);
	}

	/**
	 * Handles a drag over event. This is invoked whenever a drag is occurring,
	 * both for component and wires. In the case of wires, this also updates the
	 * display so it follows the mouse.
	 *
	 * @param e the {@code DragEvent} instance.
	 */
	private void onDragOver(DragEvent e) {
		e.acceptTransferModes(TransferMode.COPY, TransferMode.MOVE);

		// Dragging a wire
		if (e.getTransferMode() == TransferMode.MOVE) {
			// Move the display
			double mouseX = e.getX();
			double mouseY = e.getY();

			wireDragData.getCircle().setCenterX(mouseX);
			wireDragData.getCircle().setCenterY(mouseY);

		}
	}

	public InfoPane infoPane() {
		return main.getInfoPane();
	}

	public Set<SandboxComponent> components() {
		return components;
	}

	/**
	 * Adds an electric component to the sandbox pane at the specified grid
	 * coordinates.
	 *
	 * @param x the x-grid coordinate.
	 * @param y the y-grid coordinate.
	 * @param comp the type of the component.
	 * @return the newly created {@code SandboxComponent}.
	 */
	public SandboxComponent addComponent(int x, int y, ComponentType comp) {
		x = Math.max(0, Math.min(MAX_GRID_X, x));
		y = Math.max(0, Math.min(MAX_GRID_Y, y));

		ElectricComponent ec = comp.create(simulation);
		if (!(ec instanceof BatteryComponent)) {
			ec.setResistance(1);
		}

		return addComponent(x, y, ec);
	}

	/**
	 * Adds an electric component to the sandbox pane at the specified grid
	 * coordinates.
	 *
	 * @param x the x-grid coordinate.
	 * @param y the y-grid coordinate.
	 * @param ec the component to add.
	 * @return the newly created {@code SandboxComponent}.
	 */
	public SandboxComponent addComponent(int x, int y, ElectricComponent ec) {
		SandboxComponent sc = new SandboxComponent(this, ec);
		sc.move(x, y);
		sc.initialize();
		components.add(sc);
		setSelectedObject(sc);
		return sc;
	}

	/**
	 * Removes a component from the sandbox pane. This also runs the simulation.
	 *
	 * @param comp the component to remove.
	 */
	public void deleteComponent(SandboxComponent comp) {
		comp.removeFromPane();
		components.remove(comp);

		runSimulation();
	}

	/**
	 * Clears the sandbox pane from all wires and components, and deselects any
	 * currently selected object.
	 */
	public void clearStage() {
		components.forEach(SandboxComponent::removeFromPane);
		components.clear();
		this.getChildren().clear();
		setSelectedObject(null);
	}

	/**
	 * Selects a new object. This should always be either an electric component
	 * or a wire.
	 *
	 * @param obj the object to select.
	 */
	public void setSelectedObject(Object obj) {
		this.selectedObject = obj;
		main.getInfoPane().onSelectComponent(selectedObject instanceof SandboxComponent
				? ((SandboxComponent) selectedObject).getComponent()
				: null);
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

	/**
	 * Signals the end of a wire drag.
	 *
	 * @return the ended wire drag data.
	 */
	public WireDragData endWireDrag() {
		WireDragData wdd = wireDragData;
		wireDragData = null;
		return wdd;
	}

	/**
	 * Signals the start of a wire drag.
	 *
	 * @param wireDragData the data of the drag.
	 */
	public void startWireDrag(WireDragData wireDragData) {
		this.wireDragData = wireDragData;
		Dragboard db = startDragAndDrop(TransferMode.MOVE);
		ClipboardContent cc = new ClipboardContent();
		cc.putString("wire123");
		db.setContent(cc);
	}

	/**
	 * Runs the simulation. If succeeds, the voltage and current unknowns are
	 * updated and so are the graphics.
	 *
	 * @return {@code true} if the simulation succeeded, {@code false}
	 * otherwise.
	 */
	public boolean runSimulation() {
		simulation.clearVariables();

		// Gets the battery of the circuit
		BatteryComponent battery = (BatteryComponent) components.stream()
				.map(SandboxComponent::getComponent)
				.filter(c -> c instanceof BatteryComponent)
				.findAny().orElse(null);

		if (battery == null) {
			return false;
		}

		// Runs the simulation and updates the LED graphics.
		simulation.runSimulation(battery);
		components.stream().forEach(sc -> {
			ElectricComponent comp = sc.getComponent();
			Variable current = comp.current();
			if (comp.getType() == ComponentType.LED) {
				sc.setImage((!current.resolve() || Utils.equals(current.get(), 0))
						? ComponentType.LED_OFF
						: ComponentType.LED_ON);
			}

			if (!current.resolve()) {
				return;
			}

			Utils.debug("Component " + comp + ": " + current.get() + " A");
		});

		return true;
	}
}
