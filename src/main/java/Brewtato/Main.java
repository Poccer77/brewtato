package Brewtato;

import Brewtato.GameObjects.Weapons.Shotgun;
import Brewtato.Phases.*;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.File;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    // The window handle
    public static long window;
    public static GLFWVidMode vidmode;
    public static List<Phase> phases = new ArrayList<>();
    private static Phase currentPhase;
    public static boolean paused = false;
    private static Pause pauseScreen = new Pause();
    private static int phaseCounter = 0;
    public static int tickTime = 10;
    public static TrueTypeFont ttf;



    public static void run() {

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private static void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Get information about user screen
        vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Create the window
        window = glfwCreateWindow(vidmode.width(), vidmode.height(), "Brewtato", glfwGetPrimaryMonitor(), NULL);
        if ( window == NULL ) throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                paused = !paused; // We will detect this in the rendering loop
            if (key == GLFW_KEY_0 && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
        });

        glfwSetMouseButtonCallback(window, (window, key, action, mods) -> {});
        for (int i = 0; i < 3; i++) {
            Stats.weapons.add(new Shotgun(1, 100, 60, 30, 1000));
        }

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

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

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        try {
            ttf = new TrueTypeFont("src/main/resources/monofonto.ttf");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        glEnable(GLFW_CURSOR);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // Resets any previous projection matrices
        glOrtho(0, vidmode.width(), 0, vidmode.height(),-1, 1);
        glMatrixMode(GL_MODELVIEW);

        glClearColor(0.53F, 0.53F, 0.53F, 1);

        phases.add(new Game());
        //phases.add(new Chests());
        phases.add(new LevelUp());
        //phases.add(new Shop());

        phases.get(phaseCounter).init();

    }

    private static void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            if (paused) {
                phases.get(phaseCounter).draw();

                pauseScreen.frameForward();
            } else if (phases.get(phaseCounter).finished()) {
                    phaseCounter = (phaseCounter + 1) % phases.size();
                    phases.get(phaseCounter).init();
            } else phases.get(phaseCounter).frameForward();
            glfwSwapBuffers(window); // swap the color buffers
            try {Thread.sleep(tickTime);}
            catch (InterruptedException e) {
                System.out.println("tf happened");
            }
        }
    }

    public static void main(String[] args) {
        run();
    }

}