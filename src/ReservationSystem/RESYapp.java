/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReservationSystem;
import java.util.Scanner;

/**
 *
 * @author david
 */
public class RESYapp {
    
    public static Scanner sc = new Scanner(System.in);
    
    public static void main(String args[]){
        
        System.out.println("Aplikace RESY, stable build 1.0");
        ConsoleTools.textContainer("Vitej v rezervacnim systemu\n");
        
        while(true){
            ConsoleTools.textContainer("""
                                   Menu:
                                   1 - Vytvoreni rezervace
                                   2 - 
                                   3 - 
                                   4 - Administrace
                                   0 - Ukonceni aplikace""");
            ConsoleTools.emptyLine();
            ConsoleTools.input();
            int input = sc.nextInt();

            if (input == 0) {
                break;
            }

            switch(input){
                default: System.out.println("ERROR: Neplatna volba");
                case 1: 
                    System.out.println("docasne nedostupno");
                    break;
                case 2: 
                    System.out.println("docasne nedostupno");
                    break;
                case 3: 
                    System.out.println("docasne nedostupno");
                    break;
                case 4: 
                    System.out.println("docasne nedostupno");
                    break;
            }
            
            ConsoleTools.emptyLine();
        }
        
    }
    
}
