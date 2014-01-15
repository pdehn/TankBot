/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tankbot;

/**
 * Internal Math utility
 */
public class Vector2 {
    public final double x;
    public final double y;
    
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2 add(Vector2 b) {
        return new Vector2(x + b.x, y + b.y);
    }
    
    public Vector2 subtract(Vector2 b) {
        return new Vector2(x - b.x, y - b.y);
    }
    
    public Vector2 scale(double scale) {
        return new Vector2(x * scale, y * scale);
    }
}
