/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tankbot.api;

import tankbot.api.Victor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author pdehaan
 */
public abstract class TankController {
    
    private volatile Vector2 position;
    private volatile double rotation;
    
    public TankController() {
        this.position = new Vector2(7.5, 7.5);
        this.rotation = 0.0;
    }
    
    synchronized public void draw(GraphicsContext ctx) {
        
        double pxWidth = ctx.getCanvas().getWidth();
        double pxHeight = ctx.getCanvas().getHeight();
        double unitWidth = 15;
        double unitHeight = 15;
        
        double scale = pxWidth / unitWidth;
        
        ctx.save();
        
        /**
         * Transform for robot position/orientation
         * y and rotation inverted so things work visually (canvas y is
         * inverse of typical graph)
         */
        ctx.translate(position.x * scale, (15.0 - position.y) * scale);
        ctx.rotate(Math.toDegrees(-rotation));
        
        /**
         * Draw robot "chassis"
         */
        ctx.setFill(Color.WHITE);
        ctx.fillRect(-0.5 * scale, -0.5 * scale, scale, scale);
        
        /**
         * Draw robot "wheels"
         */
        ctx.setFill(Color.GRAY);
        ctx.fillRect(-0.5 * scale, -0.6 * scale, 0.3 * scale, 0.2 * scale);
        ctx.fillRect(0.2 * scale, -0.6 * scale, 0.3 * scale, 0.2 * scale);
        ctx.fillRect(-0.5 * scale, 0.4 * scale, 0.3 * scale, 0.2 * scale);
        ctx.fillRect(0.2 * scale, 0.4 * scale, 0.3 * scale, 0.2 * scale);
        
        /**
         * Draw front indicator arrow
         */
        ctx.setStroke(Color.BLUE);
        ctx.beginPath();
        ctx.moveTo(0, -0.25 * scale);
        ctx.lineTo(0.25 * scale, 0);
        ctx.lineTo(0, 0.25 * scale);
        ctx.stroke();
        
        /**
         * pop the transform stack
         */
        ctx.restore();
    }
    
    synchronized public void update(double dt, boolean teleop) {
        
        /**
         * Let the robot update it's controls
         */
        if (teleop) {
            periodicTeleop();
        } else {
            periodicAutonomous();
        }
        
        /**
         * Wheel speeds
         * [-1,1] units/second
         */
        double vl = Victor.getChannelValue(1);
        double vr = Victor.getChannelValue(2);
        
        /**
         * Just move straight if wheel speeds are the same.
         */
        if (vl == vr) {
            double x = position.x + vl * dt * Math.cos(rotation);
            double y = position.y + vl * dt * Math.sin(rotation);
            position = new Vector2(clamp(x, 0.0, 15.0), clamp(y, 0.0, 15.0));
            return;
        }
        
        /**
         * Kinematic model, see:
         * http://www8.cs.umu.se/~thomash/reports/KinematicsEquationsForDifferentialDriveAndArticulatedSteeringUMINF-11.19.pdf
         */
        double l = 1; // wheel distance
        double R = l / 2 * (vl + vr) / (vr - vl);
        double w = (vr - vl) / l;
        double wdt = w * dt;
        double iccx = position.x - R * Math.sin(rotation);
        double iccy = position.y + R * Math.cos(rotation);
        
        double x = (position.x - iccx) * Math.cos(wdt) - (position.y - iccy) * Math.sin(wdt) + iccx;
        double y = (position.x - iccx) * Math.sin(wdt) + (position.y - iccy) * Math.cos(wdt) + iccy;
        double t = rotation + wdt;
        
        /**
         * Naively clamp coordinates to stay on screen
         */
        position = new Vector2(clamp(x, 0.0, 15.0), clamp(y, 0.0, 15.0));
        rotation = t;
    }
    
    private double clamp(double v, double min, double max) {
        return Math.max(Math.min(v, max), min);
    }
    
    public abstract void periodicAutonomous();
    
    public abstract void periodicTeleop();
    
    public abstract void robotInit();
}
