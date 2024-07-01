import bank.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.*;

import static  org.junit.jupiter.api.Assertions.*;

public class PrivateBankTest {
    PrivateBank bankAachen;

    Payment payment;

    Payment negativPayment;

    IncomingTransfer incomingTransfer;

    OutgoingTransfer outgoingTransfer;

    List<Transaction> list;

    @BeforeEach
    public void innitObjekt() throws exceptions.AccountAlreadyExistsException, IOException, exceptions.TransactionAlreadyExistException, exceptions.AccountDoesNotExistException, exceptions.TransactionAttributeException {
        System.out.println("Objekt wird intiiert");
        bankAachen = new PrivateBank("BankAachen", 0.5, 0.5,"C:/Users/Ruben/Nextcloud/UNI/5.Semster auch mal zusammen/OOS/RubensPraktikumsOrdner/praktikumOOS/Maven/src/main/resources/");
        payment = new Payment("1.1.2022", 1000, "payment", 0.1, 0.2);
        negativPayment = new Payment("1.1.2022", -1000, "negativPayment", 0.1, 0.2);
        incomingTransfer = new IncomingTransfer("1.1.2022", 500, "incomingTransfer","Jens", "Ruben");
        outgoingTransfer = new OutgoingTransfer("2.2.2022", 600, "OutgoingTransfer", "Ruben", "Jens");

        list = new ArrayList<>();
        list.add(negativPayment);
        list.add(incomingTransfer);
        list.add(outgoingTransfer);
        bankAachen.accountsToTransactions.clear();
        bankAachen.createAccount("Ruben", list);
        bankAachen.createAccount("Test");

        bankAachen.addTransaction("Ruben", payment);
        list.add(payment);
    }

    @AfterEach
    public void afterObjekt(){
        String[] pathnames;
        File f = new File(bankAachen.directoryName);
        pathnames = f.list();

        for (String pathname : pathnames){
            if (pathname.endsWith(".json")){
                File deleteF = new File(bankAachen.directoryName + pathname);
                deleteF.delete();
            }else continue;
        }
        bankAachen.accountsToTransactions.clear();
    }


    @Test
    public void testKonstruktor(){
        assertAll(
                () -> assertEquals("BankAachen", bankAachen.getName()),
                () -> assertEquals(0.5, bankAachen.getIncomingInterest()),
                () -> assertEquals(0.5, bankAachen.getOutgoingInterest()),
                () -> assertEquals("C:/Users/Ruben/Nextcloud/UNI/5.Semster auch mal zusammen/OOS/RubensPraktikumsOrdner/praktikumOOS/Maven/src/main/resources/", bankAachen.getdirectoryName())
        );
    }

    @Test
    public void testToString() throws exceptions.TransactionAttributeException {
        PrivateBank bankTest = new PrivateBank("bankString", 0.5, 0.5, "TEST");
        String bankString = bankTest.toString();

        String bankStringErwartet =
                "Name: " + "bankString" + "\n" +
                "Einzahlzins: " + "0.5" + "\n" +
                "Auszahlzins: " + "0.5" + "\n\n";


        assertEquals(bankString, bankStringErwartet);

    }

    @Test
    public void testEquals() throws exceptions.TransactionAttributeException {
        bankAachen.accountsToTransactions.clear();
        PrivateBank bankAachenCopy = new PrivateBank("BankAachen", 0.5, 0.5,"C:/Users/Ruben/Nextcloud/UNI/5.Semster auch mal zusammen/OOS/RubensPraktikumsOrdner/praktikumOOS/Maven/src/main/resources/");
        PrivateBank bankAachenTrueCopy = new PrivateBank(bankAachen);

        assertAll(
                () -> assertTrue(bankAachen.equals(bankAachenCopy)),
                () -> assertTrue(bankAachen.equals(bankAachenTrueCopy))
        );

    }

    @Test
    public void testCreateAccount() throws exceptions.AccountAlreadyExistsException, IOException {
        assertThrows(exceptions.AccountAlreadyExistsException.class, () -> bankAachen.createAccount("Ruben"));
        assertDoesNotThrow(()->bankAachen.createAccount("Jens"));
        assertTrue(bankAachen.accountsToTransactions.containsKey("Ruben"));
    }

    @Test
    public void testCreateAccountMitList() throws exceptions.AccountAlreadyExistsException, IOException, exceptions.TransactionAlreadyExistException, exceptions.TransactionAttributeException {
        bankAachen.createAccount("Jens", list);
        assertThrows(exceptions.AccountAlreadyExistsException.class, () -> bankAachen.createAccount("Ruben"));
        assertThrows(exceptions.TransactionAlreadyExistException.class, () -> bankAachen.addTransaction("Jens",incomingTransfer));
        assertAll(
                () -> assertTrue(bankAachen.accountsToTransactions.containsKey("Jens")),
                () -> assertEquals(bankAachen.getTransactions("Jens"), list)
        );
    }

    @Test
    public void testAddTransactions() throws exceptions.TransactionAlreadyExistException, exceptions.AccountDoesNotExistException, IOException{
        payment.setIncomingInterest(bankAachen.getIncomingInterest());
        payment.setOutgoingInterest(bankAachen.getOutgoingInterest());

        assertThrows(exceptions.TransactionAlreadyExistException.class, () -> bankAachen.addTransaction("Ruben",payment));
        assertThrows(exceptions.AccountDoesNotExistException.class, () -> bankAachen.addTransaction("Buben", payment));
        Transaction trans = bankAachen.getTransactions("Ruben").get(3);
        assertAll(
                () -> assertEquals(trans.getDate(), payment.getDate()),
                () -> assertEquals(trans.getAmount(), payment.getAmount()),
                () -> assertEquals(trans.getDescription(), payment.getDescription()),
                () -> assertEquals(bankAachen.getIncomingInterest(), payment.getIncomingInterest()),
                () -> assertEquals(bankAachen.getOutgoingInterest(), payment.getOutgoingInterest())
        );
    }

    @Test
    public void testRemoveTransaction() throws exceptions.TransactionDoesNotExistException, IOException, exceptions.AccountDoesNotExistException {
        payment.setIncomingInterest(bankAachen.getIncomingInterest());
        payment.setOutgoingInterest(bankAachen.getOutgoingInterest());

        bankAachen.removeTransaction("Ruben", payment);
        assertThrows(exceptions.TransactionDoesNotExistException.class, () -> bankAachen.removeTransaction("Ruben", payment));
        assertFalse(bankAachen.containsTransaction("Ruben",payment));
    }

    @Test
    public void testContainsTransaction(){
        payment.setIncomingInterest(bankAachen.getIncomingInterest());
        payment.setOutgoingInterest(bankAachen.getOutgoingInterest());

        assertTrue(bankAachen.containsTransaction("Ruben",payment));
    }

    @Test
    public void testGetAccountBalance(){
        assertEquals(bankAachen.getAccountBalance("Ruben"), -1100);
    }

    @Test
    public void testGetTransactions(){
        payment.setIncomingInterest(bankAachen.getIncomingInterest());
        payment.setOutgoingInterest(bankAachen.getOutgoingInterest());

        assertEquals(bankAachen.getTransactions("Ruben"), list);
    }

    @Test
    public void testGetTransactionsorted(){
        payment.setIncomingInterest(bankAachen.getIncomingInterest());
        payment.setOutgoingInterest(bankAachen.getOutgoingInterest());

        list.sort((t1,t2) ->(int)(t1.calculate()*100 - t2.calculate()*100));

        assertEquals(bankAachen.getTransactionsSorted("Ruben", true), list);
        assertFalse(bankAachen.getTransactionsSorted(
                "Ruben", false).equals(list));
    }

    @Test
    public void testGetTransactionssortedByType(){
        payment.setIncomingInterest(bankAachen.getIncomingInterest());
        payment.setOutgoingInterest(bankAachen.getOutgoingInterest());

        List<Transaction> listeKopie = new ArrayList<>(list);
        listeKopie.remove(negativPayment);
        listeKopie.remove(outgoingTransfer);

        assertEquals(bankAachen.getTransactionsByType("Ruben", true), listeKopie);
    }
}
