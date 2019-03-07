/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import electric.circuits.data.ComponentType;
import javafx.scene.image.Image;

/**
 *
 * @author Wawa
 */
public class ListViewItem {

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
