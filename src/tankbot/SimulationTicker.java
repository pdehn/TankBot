/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tankbot;

import javafx.scene.canvas.GraphicsContext;

/**
 * Executed periodically to instruct tank to simulate it's operation and motion
 */
public class SimulationTicker implements Runnable {

    private long n = 0;
    private final GraphicsContext ctx;
    private final TankController tank;
    private final double dt;
    
    private final boolean teleop;
    
    public SimulationTicker(TankController tank, GraphicsContext ctx, double dt, boolean teleop) {
        this.tank = tank;
        this.ctx = ctx;
        this.dt = dt;
        this.teleop = teleop;
    }
    
    @Override
    public void run() {
        tank.update(dt, teleop);
    }
    
}
