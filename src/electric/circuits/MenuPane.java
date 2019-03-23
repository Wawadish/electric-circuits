/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import electric.circuits.component.BatteryComponent;
import electric.circuits.data.ComponentType;
import electric.circuits.data.ElectricComponent;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


/**
 *
 * @author Wawa
 */
public class MenuPane extends Pane {

	private Button save_button;

	private Button load_button;

	private CheckBox real_time;
	private Label real_time_label;
        SandboxPane sandbox;
        
        ComponentType type;

	public MenuPane() {
		super();
		setStyle("-fx-background-color: black;");
		setPrefSize(Main.WIDTH - Main.WIDTH / 5, Main.HEIGHT / 10);

		save_button = new Button("Save");
		save_button.setLayoutX(700);
		save_button.setLayoutY(20);

		load_button = new Button("Load");
		load_button.setLayoutX(200);
		load_button.setLayoutY(20);

		real_time = new CheckBox();
		real_time.setLayoutX(450);
		real_time.setLayoutY(25);

		real_time_label = new Label("Real-Time Simulation");
		real_time_label.setLayoutX(300);
		real_time_label.setLayoutY(25);
		real_time_label.setTextFill(Color.WHITE);

		getChildren().addAll(save_button, load_button, real_time, real_time_label);
                
                
                save_button.setOnAction((ActionEvent e) -> {
                    
                    sandbox = new SandboxPane();
                    Set<SandboxComponent> com = sandbox.components();
                    
                    JSONObject obj = new JSONObject();
                    JSONArray battery_array = new JSONArray();
                    JSONArray resistor_array = new JSONArray();
                    JSONArray led_array = new JSONArray();
                    
                    try
                    {
                    
                    for(SandboxComponent c: com)
                    {
                        
                        type = c.getComponent().getType();
                        
                        if(type == ComponentType.BATTERY)
                        {
                            BatteryComponent b = (BatteryComponent) c.getComponent();
                           
                            Map map = new LinkedHashMap(3);

                            map.put("Positionx" , c.getGridX());
                            map.put("Positiony", c.getGridY());
                            map.put("Voltage" ,b.voltage());
                            battery_array.add(map);
                            
                           
                            
                        }
                        
                        if(type == ComponentType.RESISTOR)
                        {
                            
                            Map map = new LinkedHashMap(3);
                            
                            map.put("Positionx" , c.getGridX());
                            map.put("Positiony", c.getGridY());
                            map.put("Resistance", c.getComponent().resistance());
                            resistor_array.add(map);
                                    
                        }
                        
                        if(type == ComponentType.LED)
                        {
                            
                            Map map = new LinkedHashMap(3);
                            
                            map.put("Positionx" , c.getGridX());
                            map.put("Positiony", c.getGridY());
                            map.put("Resistance", c.getComponent().resistance());
                            led_array.add(map);
                            
                            
                        }
                        
                        
                    }
                    
                    
                    obj.put("Battery", battery_array);
                    obj.put("Resistor", resistor_array);
                    obj.put("LED", led_array);
                    
                    
                    
                        FileWriter writer = new FileWriter("C:\\Users\\stavr\\Desktop\\sample.json");
                        writer.write(obj.toJSONString());
                        writer.close();
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    
                    
                    
                           
                    
                    
                });
                
                
                load_button.setOnAction((ActionEvent e) -> {
                
                
                
                
                
                });

	}
        
        
        

}
