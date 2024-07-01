import bank.Transfer;
import bank.OutgoingTransfer;
import bank.IncomingTransfer;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {

    Transfer transfer;

    Transfer transfer2;


    @BeforeEach
    public void innitObjekt(){
        System.out.println("Öbjekt wird intiiert");
        transfer = new Transfer("1.1.2022", 500, "transfer", "Jens", "Ruben");
        transfer2 = new Transfer(transfer);
    }

    @Test
    public void testKonstruktor(){
        assertAll(
                () -> assertEquals(transfer.getDate(), "1.1.2022"),
                () -> assertEquals(transfer.getAmount(), 500),
                () -> assertEquals(transfer.getDescription(), "transfer"),
                () -> assertEquals(transfer.getSender(), "Jens"),
                () -> assertEquals(transfer.getRecipient(), "Ruben")
        );
    }

    @Test
    public void testCopyKonstruktor(){
        assertAll(
                () -> assertEquals(transfer,transfer2),
                () -> assertEquals(transfer.getDate(), transfer2.getDate()),
                () -> assertEquals(transfer.getAmount(), transfer2.getAmount()),
                () -> assertEquals(transfer.getDescription(), transfer2.getDescription()),
                () -> assertEquals(transfer.getSender(), transfer2.getSender()),
                () -> assertEquals(transfer.getRecipient(), transfer2.getRecipient())
        );
    }

    @Test
    public void testCalculate(){
        IncomingTransfer incomingTransfer = new IncomingTransfer("1.1.2022", 500, "incomingTransfer", "Jens", "Ruben");
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer("1.1.2022", 500, "outgoingTransfer", "Jens", "Ruben");

        assertAll(
                () -> assertEquals(incomingTransfer.calculate(), 500),
                () -> assertEquals(outgoingTransfer.calculate(), -500),
                () -> assertEquals(transfer.calculate(), 500)
        );

    }

    @Test
    public void testEquals(){
        Transfer transfer3 = new Transfer("4.4.2023", 1000, "TEST", "Ruben", "Jens");
        assertAll(
                () -> assertTrue(transfer.equals(transfer2)),
                () -> assertFalse(transfer.equals(transfer3))
        );
    }

    @Test
    public void testToString(){
        String transferTest = transfer.toString();

        String transferErwartet =
                    "Datum: " + "1.1.2022" + "\n" +
                    "Beschreibung: " + "transfer" + "\n" +
                    "Betrag: " + "500.0" + "\n" +
                    "Sender: " + "Jens" + "\n" +
                    "Empfänger: " + "Ruben" + "\n";

        assertAll(
                () -> assertEquals(transferTest,transferErwartet)
        );

    }

}
