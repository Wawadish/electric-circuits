package electric.circuits;

import electric.circuits.data.ComponentType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 *
 * @author Wawa
 */
public class SideBarPane extends Pane {

	private static final Image PLACEHOLDER = new Image("file:assets/images/placeholder.png");

	private final ListView<ListViewItem> listView = new ListView();

	public SideBarPane() {
		// Setting the background color and dimensions of the sidebar
		setStyle("-fx-background-color: grey;");
		setPrefSize(Main.WIDTH / 5, Main.HEIGHT);

		ObservableList<ListViewItem> listItems = FXCollections.observableArrayList();

		// Add temporary components to the side list
		listItems.add(new ListViewItem("Battery", PLACEHOLDER, ComponentType.BATTERY));
		listItems.add(new ListViewItem("LED", PLACEHOLDER, ComponentType.LED));
		listItems.add(new ListViewItem("Resistance", PLACEHOLDER, ComponentType.RESISTANCE));

		// Each cell of the ListView will have an image and text.
		listView.setCellFactory(cellFactory());

		// Executes whenever an element of the ListView is dragged.
		listView.setOnDragDetected(e -> {
			// Gets the currently selected item
			ListViewItem item = listView.getSelectionModel().getSelectedItem();

			Dragboard db = listView.startDragAndDrop(TransferMode.COPY);
			db.setDragView(item.getImage());

			ClipboardContent cc = new ClipboardContent();
			cc.put(DataFormat.PLAIN_TEXT, item.getComponentType());
			db.setContent(cc);
		});

		//Adding ObservableItems to the ListView and defining the background color and dimensions of the ListView.
		listView.setItems(listItems);
		listView.setStyle("-fx-control-inner-background: grey;");
		listView.setPrefSize(Main.WIDTH, Main.HEIGHT);

		//Adding the ListView to this Pane (the sidebar).
		getChildren().add(listView);
	}

	private static Callback<ListView<ListViewItem>, ListCell<ListViewItem>> cellFactory() {
		return param -> new ListCell<ListViewItem>() {
			final ImageView imageView = new ImageView();

			@Override
			public void updateItem(ListViewItem lvi, boolean empty) {
				super.updateItem(lvi, empty);
				if (lvi == null || empty) {
					setText(null);
					setGraphic(null);
					return;
				}

				setText(lvi.getName());
				imageView.setImage(lvi.getImage());
				setGraphic(imageView);
			}
		};
	}

}
