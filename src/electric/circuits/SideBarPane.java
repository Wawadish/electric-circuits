/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import electric.circuits.data.ComponentType;
import java.awt.MouseInfo;
import java.io.File;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
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
    ListViewItem selectedItem;
    ImageView temporaryImage;

    public SideBarPane() {
        super();
        //Setting the background color and dimensions of the sidebar
        setStyle("-fx-background-color: grey;");
        setPrefSize(Main.WIDTH / 5, Main.HEIGHT);

        //Creating ListViewItems and adding them to an ArrayList
        File a = new File("assets/images/256x192.png");
        System.out.println(a.getAbsolutePath());
        listViewItems.add(new ListViewItem("Battery", new Image("file:assets/images/lol.png"), ComponentType.BATTERY));
        listViewItems.add(new ListViewItem("LED", null, ComponentType.LED));
        listViewItems.add(new ListViewItem("Resistance", null, ComponentType.RESISTANCE));

        //Adding the ListViewItems to an ObservableList.
        for (ListViewItem lvi : listViewItems) {
            items.add(lvi);
        }

        //Each cell of the listview will have an image and text.
        listView.setCellFactory((ListView<ListViewItem> param) -> new ListCell<ListViewItem>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(ListViewItem lvi, boolean empty) {
                super.updateItem(lvi, empty);

                if (lvi == null || empty) {
                    return;
                }

                setText(lvi.getName());
                imageView.setImage(lvi.getImage());
                setGraphic(imageView);
            }
        });

        //Executes whenever an element of the listview is dragged.
        listView.setOnDragDetected(e -> {

            //Gets the currently selected item
            selectedItem = listView.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem.getName());

            //The image will follow the position of the mouse during the drag motion
            temporaryImage = new ImageView(selectedItem.getImage());
            temporaryImage.setX(MouseInfo.getPointerInfo().getLocation().getX());
            temporaryImage.setY(MouseInfo.getPointerInfo().getLocation().getY());
            getChildren().add(temporaryImage);

        });
        listView.setOnDragDropped(e -> {

            temporaryImage.setImage(null);

        });
        //Adding ObservableItems to the ListView and defining the background color and dimensions of the ListView.
        listView.setItems(items);
        listView.setStyle("-fx-control-inner-background: grey;");
        listView.setPrefSize(Main.WIDTH, Main.HEIGHT);

        //Adding the ListView to this Pane (the sidebar).
        getChildren().add(listView);
    }

}
