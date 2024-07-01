import bank.*;
import ui.FxApplication;

import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String [] args) throws exceptions.AccountAlreadyExistsException, exceptions.TransactionAlreadyExistException, exceptions.TransactionAttributeException, exceptions.AccountDoesNotExistException, exceptions.TransactionDoesNotExistException, IOException {

    IncomingTransfer t1 = new IncomingTransfer("1.1.2021", 1000, "Erhalten von Jens", "Jens", "Ruben");
    OutgoingTransfer t2 = new OutgoingTransfer("1.1.2021",500 , "Gesendet an Jens", "Ruben" , "Jens");
    Payment p1 = new Payment("1.1.2021", 1000, "Einzahlung",  0.5, 0.1);
    Payment p2 = new Payment("1.1.2021", -500, "Auszahlung",  0.5, 0.1);
    Payment p2copy = new Payment(p2);

    Payment pFalsch = new Payment("5.5.2020", -300, "Auszahlung",  -0.1, 1.1);
    OutgoingTransfer tFalsch = new OutgoingTransfer("6.6.2020",-500 , "test", "test" , "test");

    List<Transaction> liste1 = new ArrayList<>();
    liste1.add(t1);
    liste1.add(t2);
    liste1.add(p1);

    PrivateBank bank = new PrivateBank("Bank",0.1,0.5);
    PrivateBankAlt bankAltRichtig = new PrivateBankAlt("Bank",0.5,0.5);

    bank.createAccount("Jens", liste1);

    bank.createAccount("Ruben");
    bank.addTransaction("Ruben", p2);

    System.out.println("true = " + bank.containsTransaction("Ruben", p2));
    System.out.println("false = " + bank.containsTransaction("Ruben", t2));
    System.out.println("false = " + bank.containsTransaction("Ruben", t1));


    System.out.println("Jens's Kontostand: ");
    System.out.println(bank.getAccountBalance("Jens") + "\n");

    System.out.println("---Liste von Überweisungen aufsteigend sortiert---");
    System.out.println(bank.getTransactionsSorted("Jens", true) + "\n");

    System.out.println("---Liste von Überweisungen absteigend sortiert---");
    System.out.println(bank.getTransactionsSorted("Jens", false) + "\n");

    System.out.println("---Liste von Überweisungen---");
    System.out.println(bank.getTransactions("Jens") + "\n");

    System.out.println("---Liste von Überweisungen Typ Negativ---");
    System.out.println(bank.getTransactionsByType("Jens", false) + "\n");

    System.out.println("---Liste von Überweisungen Typ Positiv---");
    System.out.println(bank.getTransactionsByType("Jens", true) + "\n");

    System.out.println("Ruben's Kontostand: ");
    System.out.println(bank.getAccountBalance("Ruben") + "\n\n");


    System.out.println("EXCEPTIONS:" + "\n");

    //PrivateBankAlt bankAltFalsch = new PrivateBankAlt("Bank",-0.1,1.5);
    //System.out.println("false == " + bank.equals(bankAltRichtig));
    //bank.deleteAccount("Jens");
    //bank.containsTransaction("Jens", t1);

    //bank.addTransaction("test", t1);
    //bank.createAccount("Ruben");
    //bank.addTransaction("Ruben", p2copy);
    //bank.removeTransaction("Ruben", t1);
    //bank.addTransaction("Ruben", tFalsch);
    //bank.addTransaction("Ruben", pFalsch);

    System.out.println("PRIVATEBANKALT:" + "\n");

    Payment p3 = new Payment("1.1.2019",100,"Einzahlung");
    Payment p4 = new Payment("2.2.2019",-100,"Auszahlung");
    Transfer t3 = new Transfer("3.3.2019", 100, "Senden", "test1", "test2");
    Transfer t4 = new Transfer("3.3.2019", 100, "Erhalten", "test2", "test1");
    List<Transaction> liste2 = new ArrayList<>();
    liste2.add(p3);
    liste2.add(p4);
    liste2.add(t3);
    liste2.add(t4);

    bankAltRichtig.createAccount("test1", liste2);

    System.out.println("-100 == " + bankAltRichtig.getAccountBalance("test1"));


    }
}
