/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import static com.sun.javafx.robot.impl.FXRobotHelper.getChildren;
import electric.circuits.data.ComponentType;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author Wawa
 *
 */
public class SideBarPane extends Pane {

    ArrayList<ComponentType> componentList = new ArrayList<>();
    ListView<ListViewItem> listView = new ListView();
    ArrayList<ListViewItem> listViewItems = new ArrayList<>();
    ObservableList<ListViewItem> items = FXCollections.observableArrayList();

    public SideBarPane() {
        super();
        setStyle("-fx-background-color: grey;");
        setPrefSize(Main.WIDTH / 5, Main.HEIGHT);

        listViewItems.add(new ListViewItem("Battery", null, ComponentType.BATTERY));
        listViewItems.add(new ListViewItem("LED", null, ComponentType.LED));
        listViewItems.add(new ListViewItem("Resistance", null, ComponentType.RESISTANCE));
        
        for(ListViewItem lvi : listViewItems){
            items.add(lvi);
        }
        
        listView.setCellFactory((ListView<ListViewItem> param) -> new ListCell<ListViewItem>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(ListViewItem lvi, boolean empty) {
                super.updateItem(lvi, empty);
                
                if(lvi == null || empty){
                   return; 
                }
                
                setText(lvi.getName());
                imageView.setImage(lvi.getImage());
                setGraphic(imageView);
            }
        });
        listView.setOnDragDetected(e -> {
            
        });
        listView.setItems(items);
        listView.setStyle("-fx-control-inner-background: grey;");
        listView.setPrefSize(Main.WIDTH, Main.HEIGHT);

        getChildren()
                .add(listView);
    }

}
