package tankbot;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Executed periodically to render the simulation
 */
public class Renderer implements Runnable {
    
    private final GraphicsContext ctx;
    private final TankController tank;
    
    public Renderer(GraphicsContext ctx, TankController tank) {
        this.ctx = ctx;
        this.tank = tank;
    }

    @Override
    public void run() {
        
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, 640, 640);
        
        tank.draw(ctx);
        
    }
    
    
    
}
