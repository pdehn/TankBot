/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tankbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Patrick DeHaan
 */
public class TankBotApplication extends Application {
    
    private final ScheduledExecutorService exec =
            Executors.newScheduledThreadPool(2);
    
    /**
     * Places to store futures from simulation and render
     * tasks while running (so we can cancel them later)
     */
    private ScheduledFuture<?> simulationFuture = null;
    private ScheduledFuture<?> renderFuture = null;
    
    /**
     * GUI components
     */
    private ToggleGroup playPauseGroup;
    private ToggleButton autoBtn;
    private ToggleButton teleopBtn;
    private ToggleButton pauseBtn;
    
    private Canvas canvas;
    private GraphicsContext ctx;
    
    private final TankController tank = new MyTankController();
    
    EventHandler<ActionEvent> toggleStartStop = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            
            /**
             * figure out some basic state information
             */
            Toggle selected = playPauseGroup.getSelectedToggle();
            boolean auto = autoBtn == selected;
            boolean tele = teleopBtn == selected;
            boolean pause = pauseBtn == selected;
            boolean running = null != simulationFuture;
            
            if (auto && !running) {
                /**
                 * start simulation and rendering as both are off
                 */
                simulationFuture = exec.scheduleAtFixedRate(
                        new SimulationTicker(tank, ctx, 1.0 / 20.0, false),
                        0, 50, TimeUnit.MILLISECONDS);
                renderFuture = exec.scheduleAtFixedRate(
                        new Renderer(ctx, tank),
                        0, 50, TimeUnit.MILLISECONDS);
            } else if (auto) {
                /**
                 * only switch simulation as renderer should be running
                 */
                simulationFuture.cancel(true);
                simulationFuture = exec.scheduleAtFixedRate(
                        new SimulationTicker(tank, ctx, 1.0 / 20.0, false),
                        0, 50, TimeUnit.MILLISECONDS);
            } else if (tele && !running) {
                /**
                 * start simulation and rendering as both are off
                 */
                simulationFuture = exec.scheduleAtFixedRate(
                        new SimulationTicker(tank, ctx, 1.0 / 20.0, true),
                        0, 50, TimeUnit.MILLISECONDS);
                renderFuture = exec.scheduleAtFixedRate(
                        new Renderer(ctx, tank),
                        0, 50, TimeUnit.MILLISECONDS);
            } else if (tele) {
                /**
                 * only switch simulation as renderer should be running
                 */
                simulationFuture.cancel(true);
                simulationFuture = exec.scheduleAtFixedRate(
                        new SimulationTicker(tank, ctx, 1.0 / 20.0, true),
                        0, 50, TimeUnit.MILLISECONDS);
            } else if (pause && running) {
                /**
                 * cancel simulation and render tasks
                 * (we don't need to render as nothing will be moving)
                 */
                simulationFuture.cancel(true);
                renderFuture.cancel(true);
                simulationFuture = null;
            }
        }
    };
    
    public void start(Stage stage) {
        
        playPauseGroup = new ToggleGroup();
        autoBtn = new ToggleButton();
        autoBtn.setText("Autonomous Mode");
        autoBtn.setOnAction(toggleStartStop);
        autoBtn.setToggleGroup(playPauseGroup);
        
        teleopBtn = new ToggleButton();
        teleopBtn.setText("Teleop Mode");
        teleopBtn.setOnAction(toggleStartStop);
        teleopBtn.setToggleGroup(playPauseGroup);
        
        pauseBtn = new ToggleButton();
        pauseBtn.setText("Pause Simulation");
        pauseBtn.setOnAction(toggleStartStop);
        pauseBtn.setToggleGroup(playPauseGroup);
        playPauseGroup.selectToggle(pauseBtn);
        
        HBox simulationControls = new HBox();
        simulationControls.getChildren().add(autoBtn);
        simulationControls.getChildren().add(teleopBtn);
        simulationControls.getChildren().add(pauseBtn);
        
        canvas = new Canvas(640, 640);
        ctx = canvas.getGraphicsContext2D();
        
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, 640, 640);
        
        tank.draw(ctx);
        
        VBox vbox = new VBox();
        vbox.getChildren().add(simulationControls);
        vbox.getChildren().add(canvas);
        
        Scene scene = new Scene(vbox);
        
        stage.setTitle("TankBot");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                /**
                 * Kill everything when the window is closed
                 */
                exec.shutdownNow();
                Platform.exit();
            }
        });
        
        tank.robotInit();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
