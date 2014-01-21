/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tankbot.api;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author pdehaan
 */
public class KeyboardButton {
    private static Set<String> pressed = new HashSet<String>();
    
    private final String key;
    
    public KeyboardButton(String key) {
        this.key = key;
    }
    
    public boolean get() {
        return pressed.contains(key);
    }
    
    protected static void set(String key, boolean on) {
        if (on) {
            pressed.add(key);
        } else {
            pressed.remove(key);
        }
    }
}
