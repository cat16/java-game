package main.java;

import main.java.rendering.Renderer;
import main.java.rendering.shader.ShaderException;
import main.java.universe.Universe;
import main.java.util.ResourceNotFoundException;
import org.lwjgl.Version;

public class Game {

    boolean running;
    Renderer renderer;
    Universe universe;

    public Game() {
        running = false;
        renderer = new Renderer();
        universe = new Universe();
    }

    public void start() {
        running = true;
        if(!renderer.isInitialized()) try{
            renderer.init();
        }catch(ShaderException e){
            System.out.println("Could not initialize main.shaders! Exiting with code 1:");
            e.printStackTrace();
            System.exit(1);
        }catch(ResourceNotFoundException e){
            System.out.println("Could not load shader files! Exiting with code 1:");
            e.printStackTrace();
            System.exit(1);
        }
        universe.init();
        loop();
    }

    private void loop() {
        while (running) {
            renderer.render(universe);
            if(renderer.shouldClose()) running = false;
        }
        renderer.cleanUp();
        universe.cleanUp();
    }

    public static void main(String[] args) {
        System.out.println("Currently running LWJGL " + Version.getVersion());
        new Game().start();
    }

}