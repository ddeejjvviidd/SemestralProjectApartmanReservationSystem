package ReservationSystem.UI;

import ReservationSystem.Tools.ConsoleTools;
import ReservationSystem.Tools.DateTools;
import ReservationSystem.Core.Reservation;
import ReservationSystem.Core.ReservationData;
import java.io.IOException;

import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RESYapp {

    private static ReservationData database = new ReservationData();
    public static Scanner sc = new Scanner(System.in);
    private static int endOfBooking = DateTools.toExcelFormat(31, 12, 2024);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static boolean isLoggedAsAdmin = false;
    private static boolean isLoggedAsUser = false;

    private static String uName = "";
    private static String uSurname = "";
    private static String uID = "";
    private static String uEmail = "";

    private static List<Reservation> userReservations;

    public static void main(String[] args) {

        boolean exit = false;

        try {
            database.loadReservations();
            database.sortReservations();
        } catch (Exception e) {
            System.out.println("ERROR: Chyba pri cteni ze souboru: " + e.getMessage());
        }

        while (!exit) {
            printMainMenu(); //vypise do konzole menu

            if (sc.hasNextInt()) {

                int choice = sc.nextInt();
                sc.nextLine();
                System.out.println(""); // Prazdny radek pro oddeleni mezi volbami

                switch (choice) {
                    case 1:
                        System.out.println("Apartman v Zatacce");
                        System.out.println("apartman@vzatacce.cz");
                        System.out.println("+420 700 800 700");
                        System.out.println("Okruzni 84, 511 01 Turnov");
                        System.out.println("Poskytujeme k pronajmu nove vybaveny apartman 2+kk.");
                        System.out.println("K dispozici pro 1-4 lidi.");
                        System.out.println("Cena za kazdou osobu v apartmanu za jednu noc je 600,-.");
                        System.out.println("Plati se hotove na miste pri predavani klicu, karty nebereme.");
                        break;
                    case 2:
                        availabilityCheck();
                        break;
                    case 3:
                        createReservation();
                        break;
                    case 4:
                        if (!isLoggedAsUser) {
                            if (userAuth()) {
                                myReservationMenu();
                            }
                        } else {
                            myReservationMenu();
                        }
                        break;
                    case 5:
                        if (!isLoggedAsAdmin) {
                            if (adminLogin()) {
                                adminMenu();
                            }
                        } else {
                            adminMenu();
                        }
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("Neplatna volba. Zvolte znovu.");
                        break;
                }
            } else {
                System.out.println(""); // Prazdny radek pro oddeleni mezi volbami
                String invalidInput = sc.nextLine();
                System.out.println("Neplatna volba, zvolte znovu.");
            }

            System.out.println(""); // Prazdny radek pro oddeleni mezi volbami
        }

        //konec programu
        try {
            database.saveAndOverwriteReservations();
        } catch (Exception e) {
            System.out.println("Chyba pri ukladani rezervaci: " + e.getMessage());
        }

    }

    //vypisy menu --------------------------------------------------------------
    
    public static void printMainMenu() {
        System.out.println("==== Rezervacni system ubytovani ====");
        if (isLoggedAsAdmin) {
            System.out.println("Jste prihlasen jako administrator");
        } else if (isLoggedAsUser) {
            System.out.printf("Overeny uzivatel: %s %s\n", uName, uSurname);
        }
        System.out.println("1. Informace o ubytovani");
        System.out.println("2. Zjistit dostupnost terminu");
        System.out.println("3. Vytvorit rezervaci");
        System.out.println("4. Moje rezervace");
        System.out.println("5. Administrator");
        System.out.println("0. Konec");
        System.out.println("=====================================");
        ConsoleTools.input();
    }

    public static void printMyReservationMenu() {
        System.out.println("=========== Moje rezervace ==========");
        if (isLoggedAsUser) {
            System.out.printf("Overeny uzivatel: %s %s\n", uName, uSurname);
        }
        System.out.println("1. Vypsat informace o vsech mych rezervacich");
        System.out.println("2. Vytvorit export rezervaci");
        System.out.println("3. Zmenit telefonni cislo");
        System.out.println("4. Zmenit poznamku");
        System.out.println("5. Zrusit rezervaci");
        System.out.println("6. Odhlasit se");
        System.out.println("0. Zpet do hlavniho menu");
        System.out.println("=====================================");
        ConsoleTools.input();
    }

    public static void printAdminMenu() {
        System.out.println("============= Admin menu ============");
        System.out.println("1. Vypsat vsechny rezervace");
        System.out.println("2. Vypsat vsechny rezervace se surovymi daty");
        System.out.println("3. Vypsat vsechny rezervace ve formatu dat v CSV");
        System.out.println("4. Smazat rezervaci");
        System.out.println("5. Odhlasit se");
        System.out.println("0. Zpet do hlavniho menu");
        System.out.println("=====================================");
        ConsoleTools.input();
    }
    
    //logika menu --------------------------------------------------------------

    public static boolean userAuth() {
        String name;
        String surname;
        String email;
        String idNumber;

        //nacteni jmena
        name = enterName();
        if (name.equals("")) {
            return false;
        }

        //nacteni prijmeni
        surname = enterSurname();
        if (surname.equals("")) {
            return false;
        }

        //nacteni ID
        idNumber = enterID();
        if (idNumber.equals("")) {
            return false;
        }

        //nacteni emailu
        email = enterEmail();
        if (email.equals("")) {
            return false;
        }

        database.sortReservations();
        userReservations = database.getUserReservationsByCriteria(name, surname, idNumber, email);
        
        if (userReservations != null) {
            isLoggedAsAdmin = false; //zrusi pripadne prihlaseni admina
            isLoggedAsUser = true; //potvrdi prihlaseni uzivatele
            uName = name;
            uSurname = surname;
            uID = idNumber;
            uEmail = email;
            System.out.println("Rezervace nalezeny. Overeni uspesne.");
            System.out.println("");
            return true;
        } else {
            System.out.println("Nenalezeny zadne rezervace.");
            System.out.println("");
            return false;
        }
    }

    public static boolean myReservationMenu() {
        try {
            boolean exit = false;
            int volba;
            while (!exit) {
                printMyReservationMenu();

                if (sc.hasNextInt()) {
                    Reservation userReservation;
                    int choice = sc.nextInt();
                    sc.nextLine();
                    System.out.println(""); // Prazdny radek pro oddeleni mezi volbami

                    switch (choice) {
                        case 1:
                            //info
                            for (Reservation reservation : userReservations) {
                                System.out.println(reservation.toUserFriendlyString());
                            }
                            break;
                        case 2:
                            //export
                            break;
                        case 3:
                            //editace telefonniho cisla
                            userReservation = validateUserReservation();
                            if (userReservation == null) {
                                break;
                            }
                            
                            String phoneNumber = enterPhoneNumber();
                            if (phoneNumber.equals("")) {
                                break;
                            }
                            
                            if (userReservation.setPhoneNumber(phoneNumber)) {
                                System.out.println("Nove cislo bylo nastaveno.");
                                try {
                                    database.saveAndOverwriteReservations();
                                } catch (Exception e) {
                                    System.out.println("Chyba pri ukladani rezervaci: " + e.getMessage());
                                }
                                database.saveAndOverwriteReservations();
                            } else {
                                System.out.println("Doslo k chybe.");
                            }
                            userReservation = null;
                            break;
                        case 4:
                            //zmena poznamky
                            userReservation = validateUserReservation();
                            if (userReservation == null) {
                                break;
                            }
                            
                            String note = enterNote();
                            
                            if (userReservation.setNote(note)) {
                                System.out.println("Poznamka byla zmenena.");
                                try {
                                    database.saveAndOverwriteReservations();
                                } catch (Exception e) {
                                    System.out.println("Chyba pri ukladani rezervaci: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Doslo k chybe.");
                            }
                            userReservation = null;
                            break;
                        case 5:
                            //zruseni rezervace
                            userReservation = validateUserReservation();
                            if (userReservation == null) {
                                break;
                            }
                            
                            int today = DateTools.toExcelFormat(LocalDate.now());
                            
                            if (userReservation.getArrivalDate() < today & userReservation.getDepartureDate() < today) {
                                System.out.println("Nelze smazat jiz probehlou rezervaci.");
                            } else if (userReservation.getArrivalDate() == today) {
                                System.out.println("Rezervaci lze zrusit nejdele den predem.");
                            } else if (userReservation.getArrivalDate() < today & userReservation.getDepartureDate() <= today) {
                                System.out.println("Nelze zrusit probihajici rezervaci.");
                            } else {
                                database.removeReservation(userReservation);
                                userReservations.remove(userReservation);
                                userReservation = null;
                                
                                if (userReservations.isEmpty()) {
                                    System.out.println("Smazali jste vasi posledni rezervaci, system Vas odhlasi.");
                                    userLogOff();
                                    exit = true;
                                    break;
                                }
                                database.saveAndOverwriteReservations();
                                System.out.println("Rezervace byla smazana.");
                            }
                            break;
                        case 6:
                            userLogOff();
                            exit = true;
                            break;
                        case 0:
                            exit = true;
                            break;
                        default:

                            break;
                    }

                } else {
                    System.out.println(""); // Prazdny radek pro oddeleni mezi volbami
                    String invalidInput = sc.nextLine();
                    System.out.println("Neplatna volba, zvolte znovu.");
                }

                System.out.println(""); // Prazdny radek pro oddeleni mezi volbami
            }
        } catch (Exception e) {
            System.out.println("Doslo k chybe: " + e.getMessage());
        }
        return true;
    }
    
    public static Reservation validateUserReservation() {
        Reservation target;
        int reservationNumber = enterNumberOfReservation();
        if (reservationNumber == -1) {
            return null;
        }

        target = null;
        for (Reservation reservation : userReservations) {
            if (reservation.getReservationNumber() == reservationNumber) {
                target = reservation;
                break;
            }
        }

        if (target == null) {
            System.out.println("Takova rezervace neexistuje nebo nemate pravo k jeji editaci.");
            return null;
        }
        
        return target;
    }

    public static boolean userLogOff() {
        userReservations = null;
        uName = null;
        uSurname = null;
        uID = null;
        uEmail = null;
        isLoggedAsUser = false;
        isLoggedAsAdmin = false;
        
        return true;
    }
    
    public static boolean adminLogin() {
        //nejvic lidl prihlasovani na svete
        String passwd = "2023semestral";
        String input = "";

        System.out.println("Zadejte heslo:");
        ConsoleTools.input();
        input = sc.nextLine();

        if (input.equals(passwd)) {
            System.out.println("Prihlaseni probehlo uspesne.");
            System.out.println("");
            userLogOff();
            isLoggedAsAdmin = true;
            return true;
        } else {
            System.out.println("Nespravne heslo.");
            return false;
        }
    }

    public static boolean adminMenu() {
        try {
            boolean exit = false;
            int volba;
            while (!exit) {
                printAdminMenu();

                if (sc.hasNextInt()) {
                    int choice = sc.nextInt();
                    sc.nextLine();
                    System.out.println(""); // Prazdny radek pro oddeleni mezi volbami

                    switch (choice) {
                        case 1:
                            for (Reservation reservation : database.getReservations()) {
                                System.out.println(reservation.toUserFriendlyString());
                            }
                            break;
                        case 2:
                            for (Reservation reservation : database.getReservations()) {
                                System.out.println(reservation.toString());
                            }
                            break;
                        case 3:
                            for (Reservation reservation : database.getReservations()) {
                                System.out.println(reservation.toCsvData());
                            }
                            break;
                        case 4:
                            Reservation reservation;
                            System.out.println("Zadejte cislo rezervace ke smazani:");
                            ConsoleTools.input();

                            if (!sc.hasNextInt()) {
                                System.out.println("Neplatne cislo rezervace.");
                                return false;
                            }

                            int reservationNumber = sc.nextInt();
                            sc.nextLine();

                            database.sortReservations();

                            try {
                                reservation = database.getReservationByReservationNumber(reservationNumber);
                            } catch (Exception e) {
                                System.out.println("Doslo k chybe: " + e.getMessage());
                                break;
                            }

                            database.removeReservation(reservation);
                            database.saveAndOverwriteReservations();
                            System.out.println("Rezervace byla uspesne smazana.");

                            break;
                        case 5:
                            isLoggedAsAdmin = false;
                            exit = true;
                            break;
                        case 0:
                            exit = true;
                            break;
                        default:

                            break;
                    }

                } else {
                    System.out.println(""); // Prazdny radek pro oddeleni mezi volbami
                    String invalidInput = sc.nextLine();
                    System.out.println("Neplatna volba, zvolte znovu.");
                }

                System.out.println(""); // Prazdny radek pro oddeleni mezi volbami
            }
        } catch (Exception e) {
            System.out.println("Doslo k chybe: " + e.getMessage());
        }
        return true;
    }

    public static boolean availabilityCheck() {

        int from = enterDateOfArrival();
        if (from < 0) {
            return false;
        }
        int to = enterDateOfDeparture();
        if (to < 0) {
            return false;
        }
        database.sortReservations();

        try {
            if (!database.isBookable(from, to)) {
                System.out.println("Zadany termin neni volny");
                return false;
            }
            System.out.println("Zadany termin je volny");
            return true;
        } catch (Exception e) {
            System.out.println("Doslo k chybe: " + e.getMessage());
            return false;
        }

    }

    public static boolean createReservation() {
        try {
            int arrival, departure, reservationNumber, numberOfGuests, birthDate;
            int price = 0;
            String rNumber, firstName, lastName, idNumber, email, phoneNumber, note = "";

            //nacteni dat prijezdu
            arrival = enterDateOfArrival();
            if (arrival < 0) {
                return false;
            }
            departure = enterDateOfDeparture();
            if (departure < 0) {
                return false;
            }

            if (!database.isBookable(arrival, departure)) {
                System.out.println("Zadany termin neni volny");
                return false;
            }

            //vytvoreni cisla rezervace
            LocalDate localDate = DateTools.toLocalDateFormat(arrival);
            rNumber = String.format("%04d%02d%02d", localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            reservationNumber = Integer.parseInt(rNumber);

            //nacteni poctu hostu
            numberOfGuests = enterNumberOfGuests();
            if (numberOfGuests < 0) {
                return false;
            }

            //nacteni jmena
            firstName = enterName();
            if (firstName.equals("")) {
                return false;
            }

            //nacteni prijmeni
            lastName = enterSurname();
            if (lastName.equals("")) {
                return false;
            }

            //nacteni ID
            idNumber = enterID();
            if (idNumber.equals("")) {
                return false;
            }

            //nacteni data narozeni
            birthDate = enterBirthDate();
            if (birthDate < 0) {
                return false;
            }

            //nacteni emailu
            email = enterEmail();
            if (email.equals("")) {
                return false;
            }

            //nacteni telefonniho cisla
            phoneNumber = enterPhoneNumber();
            if (phoneNumber.equals("")) {
                return false;
            }

            //nacteni poznamky
            note = enterNote();

            database.createReservation(reservationNumber, arrival, departure, numberOfGuests, firstName, lastName, idNumber, birthDate, email, phoneNumber, price, note);

            System.out.printf("Rezervace cislo %d byla vytvorena.\n", reservationNumber);

            if (!isLoggedAsAdmin) {
                isLoggedAsUser = true;
//                userReservations = database.getReservationByReservationNumber(reservationNumber);
            }

            database.saveAndOverwriteReservations();
        } catch (Exception e) {
            System.out.println("Doslo k chybe: " + e.getMessage());
        }
        return true;
    }

    //nacitani dat od uzivatele ------------------------------------------------
    
    public static int enterNumberOfReservation() {
        int reservationNumber = 0;
        
        System.out.println("Zadejte cislo rezervace:");
        ConsoleTools.input();
        if (!sc.hasNextInt() || reservationNumber < 0) {
            System.out.println("Neocekavany znak.");
            return -1;
        }
        
        reservationNumber = sc.nextInt();
        sc.nextLine();
        
        return reservationNumber;
    }
    
    public static int enterDateOfArrival() {
        String input = "";
        int from;
        int today = DateTools.toExcelFormat(LocalDate.now());

        System.out.println("Zadejte datum OD (dd.MM.yyyy):");
        ConsoleTools.input();
        input = sc.nextLine().trim();

        String[] inputFrom = input.split("\\.");
        if (inputFrom.length != 3) {
            System.out.println("Spatne zadane datum.");
            return -1;
        }
        try {
            from = DateTools.toExcelFormat(Integer.parseInt(inputFrom[0]), Integer.parseInt(inputFrom[1]), Integer.parseInt(inputFrom[2]));
        } catch (Exception e) {
            System.out.println("Doslo k chybe: " + e.getMessage());
            return -1;
        }
        if (from < today) {
            System.out.println("Datum prijezdu nemuze byt v minulosti.");
            return -1;
        } else if (from > endOfBooking) {
            System.out.println("Rezervovat lze pouze do " + DateTools.toLocalDateFormat(endOfBooking).format(formatter));
            return -1;
        }
        return from;
    }

    public static int enterDateOfDeparture() {
        String input = "";
        int to;
        int today = DateTools.toExcelFormat(LocalDate.now());

        System.out.println("Zadejte datum DO (dd.MM.yyyy):");
        ConsoleTools.input();
        input = sc.nextLine().trim();

        String[] inputTo = input.split("\\.");
        if (inputTo.length != 3) {
            System.out.println("Spatne zadane datum.");
            return -1;
        }
        try {
            to = DateTools.toExcelFormat(Integer.parseInt(inputTo[0]), Integer.parseInt(inputTo[1]), Integer.parseInt(inputTo[2]));
        } catch (Exception e) {
            System.out.println("Doslo k chybe: " + e.getMessage());
            return -1;
        }
        if (to < today) {
            System.out.println("Datum odjezdu nemuze byt v minulosti.");
            return -1;
        } else if (to > endOfBooking) {
            System.out.println("Rezervovat lze pouze do " + DateTools.toLocalDateFormat(endOfBooking).format(formatter));
            return -1;
        }
        return to;
    }

    public static int enterNumberOfGuests() {
        int noOfGuests;

        System.out.println("Zadejte pocet hostu (1-4):");
        ConsoleTools.input();
        if (!sc.hasNextInt()) {
            System.out.println("Neocekavany znak.");
            return -1;
        }

        noOfGuests = sc.nextInt();
        sc.nextLine();

        if (noOfGuests < 1 || noOfGuests > 4) {
            System.out.println("Neplatny pocet hostu.");
            return -1;
        }

        return noOfGuests;
    }

    public static String enterName() {
        String name = "";

        System.out.println("Zadejte svoje krestni jmeno:");
        ConsoleTools.input();
        name = sc.nextLine().trim();

        if (name.length() < 2) {
            System.out.println("Jmeno musi byt delsi nez dva znaky.");
            return "";
        }

        return name;
    }

    public static String enterSurname() {
        String surname = "";

        System.out.println("Zadejte svoje prijmeni:");
        ConsoleTools.input();
        surname = sc.nextLine().trim();

        if (surname.length() < 2) {
            System.out.println("Prijmeni musi byt delsi nez dva znaky.");
            return "";
        }

        return surname;
    }

    public static String enterID() {
        String idNumber = "";

        System.out.println("Zadejte ID dokladu (OP/pas/ridicak):");
        ConsoleTools.input();
        idNumber = sc.nextLine().trim();

        //zadna zeme na svete nema doklad o cisle delsim nez 11
        if (idNumber.length() > 11 || idNumber.length() < 1) {
            System.out.println("Neplatne ID dokladu.");
            return "";
        }

        return idNumber;
    }

    public static int enterBirthDate() {
        String input = "";
        int today = DateTools.toExcelFormat(LocalDate.now());
        int ageLimit = today - (18 * 365);
        int birthDate;

        System.out.println("Zadejte datum narozeni (dd.MM.yyyy):");
        ConsoleTools.input();
        input = sc.nextLine().trim();

        String[] inputFrom = input.split("\\.");
        if (inputFrom.length != 3) {
            System.out.println("Spatne zadane datum.");
            return -1;
        }
        try {
            birthDate = DateTools.toExcelFormat(Integer.parseInt(inputFrom[0]), Integer.parseInt(inputFrom[1]), Integer.parseInt(inputFrom[2]));
        } catch (Exception e) {
            System.out.println("Doslo k chybe: " + e.getMessage());
            return -1;
        }
        if (birthDate > today) {
            System.out.println("Nemohl jste se narodit v budoucnosti.");
            return -1;
        } else if (birthDate <= today & birthDate >= ageLimit) {
            System.out.println("Nesplnujete vekovy limit 18 let.");
            return -1;
        }

        return birthDate;
    }

    public static String enterEmail() {
        String email = "";

        System.out.println("Zadejte vas email:");
        ConsoleTools.input();
        email = sc.nextLine().trim();

        if (email.length() < 6 || !email.contains("@")) {
            System.out.println("Neplatny email.");
            return "";
        }

        return email;
    }

    public static String enterPhoneNumber() {
        String phoneNumber = "";

        System.out.println("Zadejte vase telefonni cislo:");
        ConsoleTools.input();
        phoneNumber = sc.nextLine().trim();

        if (phoneNumber.length() < 7) {
            System.out.println("Neplatne telefonni cislo.");
            return "";
        }

        return phoneNumber;
    }

    public static String enterNote() {
        String note = "";

        System.out.println("Zadejte volitelnou poznamku k vasi rezervaci:");
        ConsoleTools.input();
        note = sc.nextLine().trim();

        return note;
    }
}
