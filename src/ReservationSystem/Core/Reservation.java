package ReservationSystem.Core;

import ReservationSystem.Tools.DateTools;
import java.util.Locale;

public class Reservation {

    private int reservationNumber;
    private int arrivalDate;
    private int departureDate;
    private int numberOfGuests;
    private String firstName;
    private String lastName;
    private String idNumber;
    private int birthDate;
    private String email;
    private String phoneNumber;
    private float price;
    private String note;

    public Reservation(int reservationNumber, int arrivalDate, int departureDate, int numberOfGuests,
            String firstName, String lastName, String idNumber, int birthDate,
            String email, String phoneNumber, float price, String note) {
        this.reservationNumber = reservationNumber;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.numberOfGuests = numberOfGuests;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.price = (numberOfGuests * 600) * (departureDate - arrivalDate);
        this.note = note;
    }

    /**
     * Vraci cislo rezervace.
     *
     * @return
     */
    public int getReservationNumber() {
        return reservationNumber;
    }

    /**
     * Vraci datum prijezdu v Excel formatu.
     *
     * @return
     */
    public int getArrivalDate() {
        return arrivalDate;
    }

    /**
     * Vraci datum odjezdu v Excel formatu.
     *
     * @return
     */
    public int getDepartureDate() {
        return departureDate;
    }

    /**
     * Vraci pocet hostu.
     * @return 
     */
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    /**
     * Vraci krestni jmeno.
     * @return 
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Vraci prijmeni.
     * @return 
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Vraci cislo zadaneho dokladu.
     * @return 
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * Vraci datum narozeni v Excel formatu.
     * @return 
     */
    public int getBirthDate() {
        return birthDate;
    }

    /**
     * Vraci email.
     * @return 
     */
    public String getEmail() {
        return email;
    }

    /**
     * Vraci telefonni cislo.
     * @return 
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Vraci cenu za rezervaci.
     * @return 
     */
    public float getPrice() {
        return price;
    }

    /**
     * Vraci poznamku pro rezervaci.
     * @return 
     */
    public String getNote() {
        return note;
    }

    /**
     * Nastavi telefonni cislo.
     * @param phoneNumber
     * @return 
     */
    public boolean setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return true;
    }

    /**
     * Nastavi poznamku pro rezervaci.
     * @param note
     * @return 
     */
    public boolean setNote(String note) {
        this.note = note;
        return true;
    }

    /**
     * Zformatuje data o rezervaci do vystupniho Stringu pro zapis do CSV souboru.
     * @return 
     */
    public String toCsvData() {
        return String.format(Locale.US, "%d,%d,%d,%d,%s,%s,%s,%d,%s,%s,%f,%s",
                reservationNumber, arrivalDate, departureDate, numberOfGuests, firstName, lastName, idNumber, birthDate, email, phoneNumber, price, note);
    }

    /**
     * Zformatuje data o rezervaci do vystupniho Stringu do uzivatelem citelneho formatu, datumy se z Excel formatu preformatuji do normalniho data.
     * @return 
     */
    public String toUserFriendlyString() {
        return String.format(Locale.US, "Cislo rezervace: %d, datum prijezdu: %s, datum odjezdu: %s, pocet hostu: %d, JMENO: %s, PRIJMENI: %s, cislo OP: %s, datum narozeni: %s, email: %s,"
                + " telefonni cislo: %s, cena: %.2f CZK, poznamka: %s",
                reservationNumber, DateTools.formatExcelDate(arrivalDate), DateTools.formatExcelDate(departureDate), numberOfGuests, firstName, lastName, idNumber,
                DateTools.formatExcelDate(birthDate), email, phoneNumber, price, note);
    }
    
    /**
     * Vraci String s informacemi o rezervaci.
     * @return 
     */
    @Override
    public String toString() {
        return "Cislo rezervace: " + reservationNumber
                + ", datum prijezdu: " + arrivalDate
                + ", datum odjezdu: " + departureDate
                + ", pocet hostu: " + numberOfGuests
                + ", jmeno: " + firstName
                + ", prijmeni: " + lastName
                + ", cislo OP: " + idNumber
                + ", datum narozeni: " + birthDate
                + ", email: " + email
                + ", telefon: " + phoneNumber
                + ", cena: " + price
                + ", pozn.: " + note;
    }
}
