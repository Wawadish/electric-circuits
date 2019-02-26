/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import electric.circuits.data.ElectricComponent;
import javafx.scene.shape.Circle;

/**
 *
 * @author stavr
 */
public class SandboxComponent {
   // private final ElectricComponent comp;
    private double x, y;
    
    private Circle j1, j2;
    
    
    public SandboxComponent(double x,double y,Circle j1,Circle j2)
    {
        this.x = x;
        this.y = y;
        this.j1 = j1;
        this.j2 = j2;
                
    }
    
    public void setXcoord(double x)
    {
        this.x =x;
    }
    
   
    
    public void setYcoord(double y)
    {
        this.y=y;
    }
    
    
    
    
    
    public void addTo(SandboxPane pane) {
     //   pane.get
        
    }
    
}
