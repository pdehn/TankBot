/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tankbot.api;

/**
 *
 * @author pdehaan
 */
public interface MotorController {
    
    public void set(double speed);
    
    public double get();
}
