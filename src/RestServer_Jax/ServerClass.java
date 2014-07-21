package RestServer_Jax;

import java.util.Vector;

/**
 * Created by Fixit on 19.05.2014.
 */
public class ServerClass {
    public static Vector<String> ids = new Vector<String>();
    private static com.sun.net.httpserver.HttpServer server;
    public static void start() {
        try {
            System.out.println("Server is running");
            server = com.sun.jersey.api.container.httpserver.HttpServerFactory.create("http://localhost:9998/");
            server.start();
            System.out.println("Server is working! Visit http://localhost:9998/");
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
    public static void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }
}
