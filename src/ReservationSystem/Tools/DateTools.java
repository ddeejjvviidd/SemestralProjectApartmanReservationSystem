package ReservationSystem.Tools;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

/**
 * Obsahuje metody pro konvertovani datumu mezi LocalDate, Excel formatem a normalnim datem.
 * @author david
 */
public class DateTools {
    private static final LocalDate EXCELOUTSET = LocalDate.of(1899, 12, 30);
    
    /**
     * Konvertuje LocalDate do Excel formatu.
     * @param date
     * @return 
     */
    public static int toExcelFormat(LocalDate date){
        int days = (int) ChronoUnit.DAYS.between(EXCELOUTSET, date);
        return days;
    }
    
    /**
     * Ze zadaneho dne, mesice a roku vyrobi Integer datum v Excel formatu.
     * @param day
     * @param month
     * @param year
     * @return
     * @throws IllegalArgumentException 
     */
    public static int toExcelFormat(int day, int month, int year) throws IllegalArgumentException{
        if(day < 1 || day > 31 || month < 1 || month > 12 || year < 1900){
            throw new IllegalArgumentException("Invalid date.");
        }
        return toExcelFormat(LocalDate.of(year, month, day));
    }
    
    /**
     * Preformatuje Excel datum do LocalDate.
     * @param date
     * @return 
     */
    public static LocalDate toLocalDateFormat(int date){
        return EXCELOUTSET.plusDays(date);
    }
    
    /**
     * Zformatuje vstupni datum v Excel formatu do Stringu v dd.mm.yyyy formatu.
     * @param date
     * @return 
     */
    public static String formatExcelDate(int date){
        return formatLocalDate(toLocalDateFormat(date));
    }
    
    /**
     * Zformatuje vstupni datum v LocalDate do dd.mm.yyyy formatu.
     * @param date
     * @return 
     */
    public static String formatLocalDate(LocalDate date){
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        
        return String.format("%d.%d.%d", day, month, year);
    }
    
}
