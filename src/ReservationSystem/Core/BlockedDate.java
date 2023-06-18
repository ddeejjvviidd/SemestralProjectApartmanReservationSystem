/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReservationSystem.Core;

import ReservationSystem.Tools.DateTools;

/**
 * Trida predstavujici objekt, ktery obsahuje data o zablokovanem terminu.
 * @author david
 */
public class BlockedDate {

    private int from;
    private int to;
    
    /**
     * Konstruktor tridy.
     * @param from
     * @param to 
     */
    public BlockedDate(int from, int to) {
        this.from = from;
        this.to = to;
    }
    
    /**
     * Vraci pocatecni datum zablokovaneho terminu v Excel formatu.
     * @return 
     */
    public int getStartingDate() {
        return from;
    }

    /**
     * Vraci koncove datum zablokovaneho terminu v Excel formatu.
     * @return 
     */
    public int getEndingDate() {
        return to;
    }
    
    /**
     * Vraci String se zformatovanou informaci o zacatku a konci zablokovaneho terminu.
     * @return 
     */
    @Override
    public String toString(){
        return String.format("Zablokovany termin od %s do %s.", DateTools.formatExcelDate(from), DateTools.formatExcelDate(to));
    }
}
