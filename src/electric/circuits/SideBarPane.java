package electric.circuits;

import electric.circuits.data.ComponentType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 * The side bar of the program, with a list of the component types.
 *
 * @author Wawa
 */
public class SideBarPane extends Pane {

	private final ListView<ListViewItem> listView = new ListView();

	public SideBarPane() {
		// Setting the background color and dimensions of the sidebar
		setStyle("-fx-background-color: grey;");

		ObservableList<ListViewItem> listItems = FXCollections.observableArrayList();

		// Add temporary components to the side list
		listItems.add(new ListViewItem("Battery", ComponentType.BATTERY));
		listItems.add(new ListViewItem("LED", ComponentType.LED));
		listItems.add(new ListViewItem("Resistance", ComponentType.RESISTOR));

		// Each cell of the ListView will have an image and text.
		listView.setCellFactory(cellFactory());

		// Executes whenever an element of the ListView is dragged.
		listView.setOnDragDetected(e -> {
			// Gets the currently selected item
			ListViewItem item = listView.getSelectionModel().getSelectedItem();
			if (item == null) {
				return;
			}

			Utils.startDrag(listView, item.getComponentType());
		});

		//Adding ObservableItems to the ListView and defining the background color and dimensions of the ListView.
		listView.setItems(listItems);
		listView.setStyle("-fx-control-inner-background: grey;");
		listView.setPrefSize(Main.WIDTH / 4, Main.HEIGHT);
		//Adding the ListView to this Pane (the sidebar).
		getChildren().add(listView);
	}

	/**
	 * Generates a cell factory for the {@code ListView}.
	 *
	 * @return
	 */
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

	public static class ListViewItem {

		private final String name;
		private final ComponentType componentType;

		public ListViewItem(String name, ComponentType componentType) {
			this.name = name;
			this.componentType = componentType;
		}

		public String getName() {
			return name;
		}

		public Image getImage() {
			return componentType.getImage();
		}

		public ComponentType getComponentType() {
			return componentType;
		}

	}
}
