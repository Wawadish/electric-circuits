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
    
    private String name;
    private Image image;
    private ComponentType componentType;
    
    public ListViewItem(String name, Image image, ComponentType componentType){
        this.name = name;
        this.image = image;
        this.componentType = componentType;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }
    
    
}
