package main.java.rendering;

import main.java.component.ComponentType;
import main.java.component.Point;
import main.java.component.base.ComponentGroupIterator;
import main.java.rendering.shader.Shader;
import main.java.rendering.shader.ShaderException;
import main.java.universe.Universe;
import main.java.util.ResourceNotFoundException;
import main.java.util.TextureLoader;
import main.java.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Renderer {

    private long window;
    private boolean initialized;
    private Shader shader;

    public Renderer() {
        initialized = false;
    }

    public void init() throws ShaderException, ResourceNotFoundException {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //load textures
        TextureLoader.loadTextures("/main/resources/textures.json");

        //create and init shaders
        shader = new Shader();
        shader.createVertexShader(Utils.loadResource("/main/shaders/vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("/main/shaders/frag.glsl"));
        shader.link();

        // Create uniforms
        shader.createUniform("model");
        shader.createUniform("projection");
        shader.createUniform("texture");

        // Make the window visible
        glfwShowWindow(window);

        initialized = true;
    }

    private void renderUniverse(Universe universe) {
        shader.bind();

        ComponentGroupIterator iterator = universe.createGroupIterator(ComponentType.POSITION, new String[]{}, new String[]{ComponentType.MESH});
        while (iterator.next()) {
            Point pos = (Point) iterator.get(ComponentType.POSITION).getValue();
            Mesh mesh = (Mesh) iterator.get(ComponentType.MESH).getValue();

            shader.setUniform("texture", 0);
            shader.setUniform("model", new Matrix4f().set(new Vector2f(0, 0)));

            //glColor4f(1.0f, 1.0f, 0.0f, 1.0f);

            mesh.render();
        }
        shader.unbind();
    }

    public void render(Universe universe) {
        if (initialized) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            renderUniverse(universe);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        } else {
            System.out.println("Could not render");
        }
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void cleanUp() {

        shader.cleanup();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean isInitialized() {
        return initialized;
    }

}
