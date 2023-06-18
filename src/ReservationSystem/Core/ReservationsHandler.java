package ReservationSystem.Core;

import ReservationSystem.Tools.DateTools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

/**
 * Trida reprezentujici data rezervaci a blokovane terminy. Obsahuje list objektu tridy Reservation a list objektu tridy BlockedDate.
 */
public class ReservationsHandler {

    private List<Reservation> reservations;
    private List<BlockedDate> blockedDates;
    
    //private String csvFilePath = "C:\\Users\\david\\Desktop\\Stuff\\PRO\\Java\\2023SemestralApp\\data\\reservations.csv";
    
    private final String FILE_SEPARATOR = System.getProperty("file.separator"); //file separator pro kompatabilitu v ruznych OS
    private String reservationsDataFilePath = "data" + FILE_SEPARATOR + "reservations.csv"; //relativni cesta na soubor s daty o rezervacich
    private String blockedDatesDataFilePath = "data" + FILE_SEPARATOR + "blockedDates.csv"; //relativni cesta na soubor se zablokovanymi daty
    private String pathToDownloads = System.getProperty("user.home") + FILE_SEPARATOR + "Downloads" + FILE_SEPARATOR;
    private String projectPath = System.getProperty("user.dir");

    /**
     * Konstruktor tridy ReservationData.
     */
    public ReservationsHandler() {
        reservations = new ArrayList<>();
        blockedDates = new ArrayList<>();
    }
    
    /**
     * Nacte rezervace ulozene v CSV souboru.
     * @throws IOException 
     */
    public void loadReservations() throws IOException{
        
        try (BufferedReader br = new BufferedReader(new FileReader(reservationsDataFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 12) {
                    //nacitani jednotlivych dat
                    int reservationNumber = Integer.parseInt(data[0].trim().replaceAll("[^0-9]", ""));
                    int arrivalDate = Integer.parseInt(data[1].trim().replaceAll("[^0-9]", ""));//LocalDate.of(1899,12,30).plusDays(Long.parseLong(data[1].trim()));
                    int departureDate = Integer.parseInt(data[2].trim().replaceAll("[^0-9]", ""));
                    int numberOfGuests = Integer.parseInt(data[3].trim().replaceAll("[^0-9]", ""));
                    String firstName = data[4].trim().replaceAll("[^a-zA-Z ]", "");
                    String lastName = data[5].trim().replaceAll("[^a-zA-Z ]", "");
                    String idNumber = data[6].trim().replaceAll("[^a-zA-Z0-9 ]", "");
                    int birthDate = Integer.parseInt(data[7].trim().replaceAll("[^0-9]", ""));
                    String email = data[8].trim();
                    String phoneNumber = data[9].trim().replaceAll("[^0-9+]", "");
                    float price;
                    if (!data[10].isBlank()) {
                        price = Float.parseFloat(data[10].trim().replaceAll("[^0-9]", ""));
                    } else {
                        price = (float) 0.0;
                    }
                    String note = data[11].trim();

                    //vytvoreni objektu
                    Reservation reservation = new Reservation(reservationNumber, arrivalDate, departureDate,
                            numberOfGuests, firstName, lastName, idNumber, birthDate, email, phoneNumber, price, note);

                    //ulozeni objektu do listu
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error pri cteni ze souboru: " + e.getMessage());
        }
    }
    
    /**
     * Nacte zablokovane terminy ulozene v CSV souboru.
     * @throws IOException 
     */
    public void loadBlockedDates() throws IOException {
        
        try (BufferedReader br = new BufferedReader(new FileReader(blockedDatesDataFilePath))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (data.length == 2) {
                    //nacte datumy
                    int from = Integer.parseInt(data[0].trim());
                    int to = Integer.parseInt(data[1].trim());
                    
                    //vytvori objekt
                    BlockedDate blocked = new BlockedDate(from, to);
                    
                    //ulozi ho do listu
                    blockedDates.add(blocked);
                }
            }
        } catch (IOException e){
            throw new IOException("Nastala chyba pri cteni ze souboru: "+e.getMessage());
        }
    }
    
    /**
     * Setridi rezervace v Listu reservations podle cisla rezervace.
     */
    public void sortReservations() {
        //vytvoreni komparatoru pro porovnavani
        Comparator<Reservation> reservationNumberComparator = Comparator.comparingInt(Reservation::getReservationNumber);
        //setrideni
        Collections.sort(reservations, reservationNumberComparator);
    }
    
    /**
     * Setridi rezervace v Listu blockedDates podle pocatecniho datumu.
     */
    public void sortBlockedDates() {
        //vytvoreni komparatoru pro porovnani
        Comparator<BlockedDate> blockedDateComparator = Comparator.comparingInt(BlockedDate::getStartingDate);
        
        Collections.sort(blockedDates, blockedDateComparator);
    }
    
    /**
     * Smaze list rezervaci a bez ulozeni je znovu nacte ze souboru.
     * @throws IOException 
     */
    public void reloadReservationsWithoutSaving() throws IOException{
        reservations.clear();
        loadReservations();
    }
    
    /**
     * Smaze list zablokovanych terminu a bez ulozeni je znovu nacte ze souboru.
     * @throws IOException 
     */
    public void reloadBlockedDatesWithoutSaving() throws IOException{
        blockedDates.clear();
        loadBlockedDates();
    }

    /**
     * Prepise rezervace z Listu reservations do CSV souboru.
     * @throws IOException 
     */
    public void saveAndOverwriteReservations() throws IOException{
        sortReservations();
        try (FileWriter writer = new FileWriter(reservationsDataFilePath)) {
            writer.write("");
            for (Reservation reservation : reservations) {
                writer.write(reservation.getReservationNumber() + ","
                        + reservation.getArrivalDate() + ","
                        + reservation.getDepartureDate() + ","
                        + reservation.getNumberOfGuests() + ","
                        + reservation.getFirstName() + ","
                        + reservation.getLastName() + ","
                        + reservation.getIdNumber() + ","
                        + reservation.getBirthDate() + ","
                        + reservation.getEmail() + ","
                        + reservation.getPhoneNumber() + ","
                        + reservation.getPrice() + ","
                        + reservation.getNote() + "\n");
            }
        } catch (IOException e) {
            throw new IOException("Chyba při zápisu do souboru: " + e.getMessage());
        }
    }
    
    /**
     * Prepise rezervace z Listu blockedDates do CSV souboru.
     * @throws IOException 
     */
    public void saveAndOverwriteBlockedDates() throws IOException {
        sortBlockedDates();
        try (FileWriter writer = new FileWriter(blockedDatesDataFilePath)){
            writer.write("");
            for (BlockedDate blockedDate : blockedDates) {
                writer.write(blockedDate.getStartingDate() + "," + blockedDate.getEndingDate() + "\n");
            }
            
        } catch (IOException e){
            throw new IOException("Chyba pri zapisu do souboru: " + e.getMessage());
        }
    }

    /**
     * Prida rezervaci do Listu reservations.
     * @param reservation
     * @return 
     */
    public boolean addReservation(Reservation reservation) {
        reservations.add(reservation);
        return true;
    }
    
    /**
     * Prida zablokovany termin do Listu blockedDates.
     * @param blockedDate
     * @return 
     */
    public boolean addBlockedDate(BlockedDate blockedDate) {
        blockedDates.add(blockedDate);
        return true;
    }

    /**
     * Odstrani rezervaci z Listu reservations.
     * @param reservation
     * @return 
     */
    public boolean removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        return true;
    }
    
    /**
     * Odstrani zablokovany termin z Listu blockedDates.
     * @param blockedDate
     * @return 
     */
    public boolean removeBlockedDate(BlockedDate blockedDate) {
        blockedDates.remove(blockedDate);
        return true;
    }

    /**
     * Vytvori rezervaci, prida ji do Listu reservations, setridi jej a zapise do souboru.
     * @param reservationNumber
     * @param arrivalDate
     * @param departureDate
     * @param numberOfGuests
     * @param firstName
     * @param lastName
     * @param idNumber
     * @param birthDate
     * @param email
     * @param phoneNumber
     * @param price
     * @param note
     * @return
     * @throws IOException 
     */
    public boolean createReservation(int reservationNumber, int arrivalDate, int departureDate, int numberOfGuests, String firstName, String lastName, String idNumber, int birthDate, String email, String phoneNumber, float price, String note) throws IOException{

        Reservation newReservation = new Reservation(reservationNumber, arrivalDate, departureDate,
                numberOfGuests, firstName, lastName, idNumber, birthDate, email, phoneNumber, price, note);
        addReservation(newReservation);

        sortReservations();
        saveAndOverwriteReservations();

        return true;
    }
    
    /**
     * Vytvori zablokovany termin, prida jej do Listu blockedDates, setridi a zapise do souboru.
     * @param from
     * @param to
     * @return
     * @throws IOException 
     */
    public boolean createBlockedDate(int from, int to) throws IOException{
        BlockedDate newBlockedDate = new BlockedDate(from, to);
        addBlockedDate(newBlockedDate);
        
        sortBlockedDates();
        saveAndOverwriteBlockedDates();
        
        return true;
    }

    /**
     * Vraci true, pokud se v zadanem rozmezi nenachazi zadna jina rezervace a zda je zadany termin k dispozici, tj. neni zablokovan.
     * @param yourArrival
     * @param yourDeparture
     * @return
     * @throws IllegalArgumentException 
     */
    public boolean isBookable(int yourArrival, int yourDeparture) throws IllegalArgumentException {

        if (yourArrival == yourDeparture) {
            throw new IllegalArgumentException("Arrival and departure date can not match.");
        } else if (yourArrival > yourDeparture) {
            throw new IllegalArgumentException("Departure date can not be less than arrival date.");
        }
        
        sortBlockedDates();
        sortReservations();
        
        for (BlockedDate blockedDate : blockedDates) {
            
            int blockedDateStart = blockedDate.getStartingDate();
            int blockedDateEnd = blockedDate.getEndingDate();
            
            if ((yourArrival == blockedDateStart) || (yourArrival > blockedDateStart & yourArrival < blockedDateEnd) || (yourArrival < blockedDateStart & yourDeparture > blockedDateStart)) {
                throw new IllegalArgumentException("Toto datum neni dostupne pro rezervace.");
            }
        }
        
        for (Reservation reservation : reservations) {

            int existingArrival = reservation.getArrivalDate();
            int existingDeparture = reservation.getDepartureDate();

            if (yourArrival < existingArrival & yourDeparture <= existingArrival) {
                return true;
            } else if ((yourArrival == existingArrival) || (yourArrival > existingArrival & yourArrival < existingDeparture) || (yourArrival < existingArrival & yourDeparture > existingArrival)) {
                throw new IllegalArgumentException("Zadany termin koliduje s existujici rezervaci.");
            }
        }
        
        return true;
    }
    
    /**
     * Vraci true, pokud zadany termin nekoliduje s existujici rezervaci nebo zablokovanym terminem a je tak dostupny pro zablokovani.
     * @param from
     * @param to
     * @return
     * @throws IllegalArgumentException 
     */
    public boolean ableToBlock(int from, int to) throws IllegalArgumentException {
        
        sortBlockedDates();
        sortReservations();
        
        for (BlockedDate blockedDate : blockedDates) {
            
            int blockedDateStart = blockedDate.getStartingDate();
            int blockedDateEnd = blockedDate.getEndingDate();
            
            if ((from == blockedDateStart) || (from > blockedDateStart & from < blockedDateEnd) || (from < blockedDateStart & to > blockedDateStart)) {
                throw new IllegalArgumentException("Toto datum je jiz zablokovane terminem od " + DateTools.formatExcelDate(blockedDate.getStartingDate()) + " do " + DateTools.formatExcelDate(blockedDate.getEndingDate()) + ".");
            }
        }
        
        for (Reservation reservation : reservations) {

            int existingArrival = reservation.getArrivalDate();
            int existingDeparture = reservation.getDepartureDate();

            if (from < existingArrival & to <= existingArrival) {
                return true;
            } else if ((from == existingArrival) || (from > existingArrival & from < existingDeparture) || (from < existingArrival & to > existingArrival)) {
                throw new IllegalArgumentException("V tomto terminu jiz probiha rezervace " + reservation.getReservationNumber() + ".");
            }
        }
        
        return true;
    }
    
    /**
     * Vraci list rezervaci.
     * @return 
     */
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    /**
     * Vraci list zablokovanych terminu.
     * @return 
     */
    public List<BlockedDate> getBlockedDates() {
        return blockedDates;
    }

    /**
     * Vyhleda a vrati rezervaci z Listu rezervaci dle zadaneho cisla rezervace.
     * @param reservationNumber
     * @return
     * @throws IllegalArgumentException 
     */
    public Reservation getReservationByReservationNumber(int reservationNumber) throws IllegalArgumentException {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationNumber() == reservationNumber) {
                return reservation;
            }
        }
        throw new IllegalArgumentException("Reservation does not exist");
    }
    
    /**
     * Vytvori novy list rezervaci, do ktereho nasype rezervace, ktere splnuji vstupni kriteria. Vrati null, pokud nenajde zadnou rezervaci.
     * @param name
     * @param surname
     * @param ID
     * @param email
     * @return 
     */
    public List<Reservation> getUserReservationsByCriteria(String name, String surname, String ID, String email) {

        List<Reservation> userReservations = new ArrayList();

        for (Reservation reservation : reservations) {
            System.out.println(reservation.getReservationNumber());
            if (reservation.getFirstName().equals(name) & reservation.getLastName().equals(surname) & reservation.getIdNumber().equals(ID) & reservation.getEmail().equals(email)) {
//                System.out.println("DEBUG: Tato rezervace splnuje kriteria.");
                userReservations.add(reservation);
            }
        }

        if (userReservations.isEmpty()) {
//            System.out.println("DEBUG: userReservations je prazdny");
            return null;
        }
//        System.out.println("DEBUG: userReservations je plny a funkce ho vraci do mainu");
        return userReservations;

    }
    
    /**
     * Vrati zablokovany termin, ktery splnuje zadane pocatecni datum.
     * @param from
     * @return
     * @throws IllegalArgumentException 
     */
    public BlockedDate getBlockedDateByStartingDate(int from) throws IllegalArgumentException {
        for (BlockedDate blockedDate : blockedDates) {
            if (blockedDate.getStartingDate() == from) {
                return blockedDate;
            }
        }
        throw new IllegalArgumentException("Nenalezena zadna blokace se zadanym pocatecnim datem.");
    }
    
    public String generateFileName() {
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        
        int year = dateNow.getYear();
        int month = dateNow.getMonthValue();
        int day = dateNow.getDayOfMonth();
        int seconds = timeNow.getSecond();
        int minutes = timeNow.getMinute();
        int hours = timeNow.getHour();
        
        return String.format("export_%04d%02d%02d%02d%02d%02d", year, month, day, seconds, minutes, hours);
    } 
    
    public String exportDataToTextFile(List<Reservation> reservationsToExport) throws IOException {
        String fileName = generateFileName() + ".txt";
        
        try(BufferedWriter wr = new BufferedWriter(new FileWriter(pathToDownloads + fileName))){
            for (Reservation reservation : reservationsToExport) {
                String[] data = reservation.toUserFriendlyString().split(",");
                
                for (String str : data) {
                    wr.write(str.trim());
                    wr.newLine();
                }
                wr.newLine();
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return fileName;
    }
    
    public String exportDataToBinaryFile(List<Reservation> reservationsToExport) throws IOException {
        String fileName = generateFileName() + ".dat";
        
        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(pathToDownloads + fileName))){
            for (Reservation reservation : reservationsToExport) {
                dos.writeUTF(reservation.toCsvData());
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return fileName;
    }
    
}
