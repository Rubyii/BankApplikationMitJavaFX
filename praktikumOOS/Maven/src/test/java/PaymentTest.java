import bank.Payment;

import org.junit.jupiter.api.*;

import static  org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    Payment positivPayment;

    Payment negativPayment;

    Payment positivPayment2;

    Payment negativPayment2;

    @BeforeEach
    public void innitObjekt(){
        System.out.println("Öbjekt wird intiiert");
        positivPayment = new Payment("1.1.2022", 500, "positivPayment", 0.5, 0.1);
        negativPayment = new Payment("2.2.2022", -500, "negativPayment", 0.5, 0.5);

        positivPayment2 = new Payment(positivPayment);
        negativPayment2 = new Payment("2.2.2022", -500, "negativPayment", 0.5, 0.5);
    }

    @Test
    public void testKonstruktor(){
        assertAll(
                () -> assertEquals(positivPayment.getDate(), "1.1.2022"),
                () -> assertEquals(positivPayment.getAmount(), 500),
                () -> assertEquals(positivPayment.getDescription(),"positivPayment"),
                () -> assertEquals(positivPayment.getIncomingInterest(), 0.5),
                () -> assertEquals(positivPayment.getOutgoingInterest(), 0.1),
                () -> assertNotEquals(negativPayment, positivPayment2)
        );
    }

    @Test
    public void testCopyKonstruktor(){
        assertEquals(positivPayment,positivPayment2);
    }

    @Test
    public void testCalculate(){
        assertAll(
                () -> assertEquals(positivPayment.calculate(), 250.0),
                () -> assertEquals(negativPayment.calculate(), -750)
        );
    }

    @Test
    public void testEquals(){
        assertAll(
                () -> assertTrue(positivPayment.equals(positivPayment2)),
                () -> assertTrue(negativPayment.equals(negativPayment2)),
                () -> assertFalse(positivPayment.equals(negativPayment)),
                () -> assertFalse(negativPayment.equals(positivPayment2))
        );
    }

    @Test
    public void testToString(){
        String positivTest = positivPayment.toString();
        String negativTest = negativPayment.toString();

        String positivErwartet =
                "Datum: " + "1.1.2022" + "\n" +
                "Beschreibung: " + "positivPayment" + "\n" +
                "Betrag: " + "250.0" + "\n" +
                "Einzahlzinsen: " + "0.5" + "\n" +
                "Auszahlzinsen: " + "0.1" + "\n";

        String negativErwartet =
                "Datum: " + "2.2.2022" + "\n" +
                "Beschreibung: " + "negativPayment" + "\n" +
                "Betrag: " + "-750.0" + "\n" +
                "Einzahlzinsen: " + "0.5" + "\n" +
                "Auszahlzinsen: " + "0.5" + "\n";

        assertAll(
                () -> assertEquals(positivTest,positivErwartet),
                () -> assertEquals(negativTest, negativErwartet)
        );

    }

}
