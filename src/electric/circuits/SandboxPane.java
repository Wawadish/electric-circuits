package electric.circuits;

import electric.circuits.component.DummyBatteryComponent;
import electric.circuits.component.DummyComponent;
import electric.circuits.data.ComponentType;
import electric.circuits.data.ElectricComponent;
import javafx.scene.image.Image;
import electric.circuits.simulation.SimulationContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
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
	public static final int MAX_GRID_X = (int) (PREF_WIDTH / GRID_SIZE);
	public static final int MAX_GRID_Y = (int) (PREF_HEIGHT / GRID_SIZE);

	private final SimulationContext simulation;
	private final Set<SandboxComponent> components;
        private SandboxComponent selectedComponent; 

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
			Image image = (Image) e.getDragboard().getContent(DataFormat.IMAGE);
			ComponentType type = (ComponentType) e.getDragboard().getContent(DataFormat.PLAIN_TEXT);

			double mouseX = e.getX() - (image.getWidth() / 2);
			double mouseY = e.getY() - (image.getHeight() / 2);
			addComponent((int) (mouseX / GRID_SIZE), (int) (mouseY / GRID_SIZE), type);
		});
                
                this.setOnMouseClicked(e -> {
                    double mouseX = e.getX() / GRID_SIZE;
                    double mouseY = e.getY()/ GRID_SIZE;
                    
                    
                    System.out.println("yeet");
                    
                    //Checks if the x y of the mouse is in the area of an image
                    for(SandboxComponent sc : components){
                        Bounds imageBounds = sc.getImageView().getLayoutBounds();
                        if((mouseX > sc.getGridX()) && (mouseX < sc.getGridX() + (imageBounds.getWidth()/ GRID_SIZE)) 
                                && (mouseY > sc.getGridY()) && (mouseY < sc.getGridY() + (imageBounds.getHeight())/ GRID_SIZE)){
                            selectedComponent = sc;
                            System.out.println(selectedComponent.getGridX() + " y : " + selectedComponent.getGridY());
                            InfoPane.topTitle.setVisible(true);
                            InfoPane.k_title.setVisible(true);
                            InfoPane.kir_eq.setVisible(true);
                            InfoPane.o_title.setVisible(true);
                            InfoPane.ohm_eq.setVisible(true);
                            InfoPane.resistance.setVisible(true);
                            InfoPane.resistance_box.setVisible(true);
                            InfoPane.voltage.setVisible(true);
                            InfoPane.voltage_box.setVisible(true);
                            
                        }
                    }
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
        
        public void deleteComponent(int x, int y){
        }
        
        public void deleteLastComponent(){
            this.components.remove(this.components.size() - 1);
        }

}
