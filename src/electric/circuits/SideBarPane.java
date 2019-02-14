/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import electric.circuits.data.ComponentType;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;


/**
 *
 * @author Wawa
 * 
 */
public class SideBarPane extends Pane{
    
    ArrayList<ComponentType> componentList = new ArrayList<>();
    ListView listView = new ListView();
    
    public SideBarPane(){
        super();
        setStyle("-fx-background-color: grey;");
        setPrefSize(Main.WIDTH / 5,Main. HEIGHT);
        for(ComponentType ct : ComponentType.values()){
            componentList.add(ct);
        }
    }
    
}
