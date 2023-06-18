/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReservationSystem.UI;

import ReservationSystem.Tools.ConsoleTools;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author david
 */
public class UIapp {
    
    public static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        boolean debugMode = false;
        
        RESYapp ui = new RESYapp();
        
        Boolean exit = false;

        while (!exit) {
            try {
                if (debugMode) {
                    exit = ui.testDebugRun();
                } else {
                    exit = ui.runProgram();
                }
            } catch (Exception e) {
                System.out.println("Program spadl.");
                System.out.println(e.getMessage());
                System.out.println("Prejete si program pustit znovu? (y/n)");
                ConsoleTools.input();
                if (!sc.nextLine().equals("y")) {
                    exit = true;
                }
                System.out.println("");
            }
        }
    }
}
