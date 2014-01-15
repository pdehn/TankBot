/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tankbot;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pdehaan
 */
public class Victor {
    
    /**
     * We store channel values statically so they can be accessed from the
     * simulation loop as well. It's not terribly pretty, but it let's this
     * present roughly the same interface as the FRC API.
     */
    private static final Map<Integer, Double> values = new HashMap<>();
    
    private final int channel;
    
    public Victor(int channel) {
        this.channel = channel;
    }
    
    public void set(double speed) {
        /**
         * Clamp to [-1, 1]
         */
        speed = Math.min(1.0, Math.max(-1.0, speed));
        values.put(channel, speed);
    }
    
    /**
     * Get the last value set for the motor controller.
     * @return 
     */
    public double get() {
        return getChannelValue(channel);
    }
    
    /**
     * Gets the last value set for a given channel, or 0.0 if none given.
     * @param channel
     * @return 
     */
    protected static double getChannelValue(int channel) {
        Double value = values.get(channel);
        if (null == value) {
            value = 0.0;
            values.put(channel, value);
        }
        return value;
    }
}
