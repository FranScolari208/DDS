package server;

import spark.Spark;
import spark.debug.DebugScreen;

public class Server {

    public static void main(String[] args) {
        Spark.port(9000);
        Router.init();
        DebugScreen.enableDebugScreen();
        /*
         * Spark.port(getHerokuAssignedPort());
         * Router.init();
         * System.out.println(System.getenv("JAVA_ENV"));
         * if(System.getenv("JAVA_ENV") != "testCI") {
         * System.out.println("Here...");
         * entityManager();
         * }
         * DebugScreen.enableDebugScreen();
         * }
         * 
         * static int getHerokuAssignedPort() {
         * ProcessBuilder processBuilder = new ProcessBuilder();
         * if (processBuilder.environment().get("PORT") != null) {
         * return Integer.parseInt(processBuilder.environment().get("PORT"));
         * }
         * return 4567; //return default port if heroku-port isn't set (i.e. on
         * localhost)
         */
    }
}