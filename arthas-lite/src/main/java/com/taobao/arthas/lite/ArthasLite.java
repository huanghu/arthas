package com.taobao.arthas.lite;

import com.taobao.arthas.lite.command.impl.ThreadCommand;
import com.taobao.arthas.lite.command.impl.TraceCommand;
import java.util.Scanner;

/**
 * Arthas Lite main class
 * @author Arthas Team
 */
public class ArthasLite {
    public static void main(String[] args) {
        System.out.println("Arthas Lite started successfully!");
        System.out.println("Available commands: thread, trace");
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("arthas-lite > ");
            String command = scanner.nextLine().trim();
            
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Arthas Lite exited.");
                break;
            } else if (command.startsWith("thread")) {
                ThreadCommand.execute(command);
            } else if (command.startsWith("trace")) {
                TraceCommand.execute(command);
            } else {
                System.out.println("Unknown command: " + command);
                System.out.println("Available commands: thread, trace, exit");
            }
        }
        scanner.close();
    }
}
