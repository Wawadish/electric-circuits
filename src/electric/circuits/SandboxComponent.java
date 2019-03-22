package electric.circuits;

import static electric.circuits.InfoPane.resistance_box;
import static electric.circuits.InfoPane.voltage_box;
import electric.circuits.component.BatteryComponent;
import electric.circuits.data.ComponentType;
import electric.circuits.data.ElectricComponent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;

/**
 *
 * @author stavr
 */
public class SandboxComponent {

    private int gridX;
    private int gridY;

    private final ImageView imageView;

    private final SandboxPane pane;
    private final ElectricComponent component;
    
    //varaibles will store the voltage and resistance that the user inputs in the text field
    private double voltage_entered;
    private double resistance_entered;
    
    

    private SandboxWire wireLeft, wireRight;

    public SandboxComponent(SandboxPane pane, ElectricComponent component) {
        this.pane = pane;
        this.component = component;
        this.imageView = new ImageView(component.getType().getImage());

        this.wireLeft = new SandboxWire(pane);
        this.wireRight = new SandboxWire(pane);
        
    }

    public void initialize() {
        // Update the position of the ImageView
        updatePosition();

        // Add the elements to the pane
        pane.getChildren().addAll(imageView);
        wireLeft.initialize(this, true);
        wireRight.initialize(this, false);

		imageView.setPickOnBounds(true);
        imageView.setOnDragDetected(e -> {
            wireLeft.removeFromPane();
            wireRight.removeFromPane();
            pane.getChildren().removeAll(imageView);
            pane.components().remove(this);

            Utils.startDrag(pane, component.getType());
        });

        imageView.setOnMouseClicked(e -> {
            pane.setSelectedObject(this);
			if (!e.isDragDetect())
				e.consume();
                        
            if(component instanceof BatteryComponent)
            {
            InfoPane.voltage.setVisible(true);
            InfoPane.voltage_box.setVisible(true);
            }
            
            else
            {
            InfoPane.topTitle.setVisible(true);
            InfoPane.k_title.setVisible(true);
            InfoPane.kir_eq.setVisible(true);
            InfoPane.o_title.setVisible(true);
            InfoPane.ohm_eq.setVisible(true);
            InfoPane.resistance.setVisible(true);
            InfoPane.resistance_box.setVisible(true);
            InfoPane.voltage.setVisible(false);
            InfoPane.voltage_box.setVisible(false);
            
            }
           
            
            
            //this will check if the user inputs a valid number as the value of voltage, if they do not then an error message will display. If they do, their value
            //will be stored in voltage_entered, and then into the component itself
            
            InfoPane.voltage_box.setOnAction(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent event)
                    {
                        try{
                            
                            if(Double.valueOf(voltage_box.getText()) <= 0 )
                            {
                                voltage_box.setText("Invalid input");
                            }
                            else
                            {
                            voltage_entered = Double.valueOf(voltage_box.getText());
                           
                               BatteryComponent b = (BatteryComponent) component;
                               b.setVoltage(voltage_entered);
                               
                               System.out.println(b.voltage());
                            }
                            
                           
                            
                        }
                        catch(Exception e)
                        {
                            voltage_box.setText("Invalid input");
                            e.printStackTrace();
                        }
                    }
                });
            
            
            //this will check if the user inputs a valid number as the value of resistance, if they do not then an error message will display. If they do, their value
            //will be stored in resistance_entered, and then into the component itself
            
             resistance_box.setOnAction(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent event)
                    {
                        try{
                            if(Double.valueOf(resistance_box.getText()) < 0)
                            {
                                resistance_box.setText("Invalid input");
                            }
                            else
                            {
                            resistance_entered = Double.valueOf(resistance_box.getText());
                            component.setResistance(resistance_entered);
                             
                            
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            resistance_box.setText("Invalid input");
                            
                        }
                    }
                });
             
            
            
            
        });
        
       
        
        
        
    }

    public void removeFromPane() {
        pane.getChildren().remove(imageView);
        wireLeft.removeFromPane();
        wireRight.removeFromPane();
    }

    public void move(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        updatePosition();
    }

    private void updatePosition() {
        imageView.setX(gridX * SandboxPane.GRID_SIZE);
        imageView.setY(gridY * SandboxPane.GRID_SIZE);
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ElectricComponent getComponent() {
        return component;
    }
    
    
}
