package org.jetbrains.kotlin.junit;

public class App {
    public static void main(String[] args) {
        try {
            TodoApplication app = new TodoApplication();
            app.run();
        } catch (Exception e) {
            System.err.println("Error starting Todo Application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
