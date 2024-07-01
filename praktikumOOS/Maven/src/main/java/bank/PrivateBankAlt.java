package bank;

import bank.exceptions.*;

import java.util.*;

/**
 * Die Klasse simuliert eine Bank die Konten und Transaktionen verwalten und verarbeiten kann.
 */
public class PrivateBankAlt implements Bank{
    /**
     * @param name Name der privaten Bank
     * @param incomingInterest Einzahlzins Wertebereich(0-1)
     * @param outgoingInterest Einzahlzins Wertebereich(0-1)
     * @param accountsToTransactions Konten werden mit Transaktionen verbunden
     *                               Konten->Liste[Transaktionen]
     */
    public String name;
    public double incomingInterest;
    public double outgoingInterest;
    public Map<String, List<Transaction>> accountsToTransactions = new HashMap();


    /**
     * Konstruktor
     * @param name Name der PrivatenBank
     * @param incomingInterest  Einzahlzins
     * @param outgoingInterest  Auszahlzins
     */

    public PrivateBankAlt(String name, double incomingInterest, double outgoingInterest) throws TransactionAttributeException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }


    /**
     * Copy-Konstruktor
     * @param p Objekt vom Typ PrivatBank
     */
    public PrivateBankAlt(PrivateBankAlt p) throws TransactionAttributeException {
        this(p.name,p.incomingInterest,p.outgoingInterest);
    }

    /**
     * NameSetter
     * @param name Name der privaten Bank
     */
    public void setName(String name){ this.name = name; }

    /**
     * NameGetter
     * @return Gibt den Namen der privaten Bank zurück
     */
    public String getName(){ return this.name;}

    /**
     * incomingInterestSetter
     * @param incomingInterest Einzahlzins
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if(incomingInterest < 0 || incomingInterest > 1)
        {
            System.out.println("IncomingInterest liegt nicht zwischen 0 und 1");
            throw new TransactionAttributeException("IncomingInterest liegt nicht zwischen 0 und 1");
        }else
        {
            this.incomingInterest = incomingInterest;
        }
    }

    /**
     * incomingInterestGetter
     * @return Gibt die Einzahlzinsen zurück
     */
    public double getIncomingInterest(){ return this.incomingInterest; }

    /**
     * outgoingInterestSetter
     * @param outgoingInterest Auszahlzins
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if(outgoingInterest < 0 || outgoingInterest > 1)
        {
            System.out.println("OutgoingInterest liegt nicht zwischen 0 und 1");
            throw new TransactionAttributeException("IncomingInterest liegt nicht zwischen 0 und 1");
        }else
        {
            this.outgoingInterest = outgoingInterest;
        }
    }

    /**
     * outgoingInterestGetter
     * @return Gibt die Auszahlzinszurück
     */
    public double getOutgoingInterest(){ return this.outgoingInterest; }


    /**
     * toString überschreibung
     * @return  Gibt alle Klassenattribute aus
     */
    @Override
    public String toString(){
        String ausgabe = (
                            "Name: "+ this.name + "\n" +
                            "Einzahlzins: " + this.incomingInterest + "\n" +
                            "Auszahlzins: " + this.outgoingInterest + "\n\n"
                        );
        for (String i : this.accountsToTransactions.keySet()){
            //System.out.println("Konto: " + i);
            ausgabe = ausgabe.concat("Konto :" + i + "\n\n");
            for (Transaction t : this.accountsToTransactions.get(i)){
                //t.toString();
                ausgabe = ausgabe.concat(t.toString() + "\n");
            }
        }
        return ausgabe;
    }

    /**
     * equals Überschreibung
     * @return bool
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null || obj.getClass() == this.getClass()) {
            PrivateBankAlt privatebank = (PrivateBankAlt) obj;
            if (((PrivateBankAlt) obj).accountsToTransactions.size() != this.accountsToTransactions.size()) return false;

            if (((PrivateBankAlt) obj).accountsToTransactions.equals(this.accountsToTransactions)) return false;

            return Double.compare(incomingInterest, this.incomingInterest) == 0 &&
                    Double.compare(outgoingInterest, this.outgoingInterest) == 0 &&
                    name.compareTo(privatebank.name) == 0;
        }
        else {
            return false;
        }
    }

    /**
     * Soll ein neues Konto erstellen
     *
     * @param account Kontoname
     * @throws AccountAlreadyExistsException Konto exestiert schon
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (this.accountsToTransactions.containsKey(account)) {
            System.out.println("Dieses Konto exestiert schon");
            throw new AccountAlreadyExistsException("Dieses Konto exestiert schon");
        } else {
            List<Transaction> list = new ArrayList<>();
            this.accountsToTransactions.put(account, list);
            //this.writeAccounts(account);
        }
    }

    /**
     * Soll ein neues Konto erstellen
     *
     * @param account Kontoname
     * @param transactions Liste der Überweisungen
     * @throws AccountAlreadyExistsException Konto exestiert schon
     */
    @Override
    public  void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {

        if (this.accountsToTransactions.containsKey(account)) {
            System.out.println("Dieses Konto exestiert schon");
            throw new AccountAlreadyExistsException("Dieses Konto exestiert schon");
        }else{
            this.accountsToTransactions.put(account, new ArrayList<Transaction>());
            for (int i = 0; i < transactions.size(); i++){
                Transaction transaction = transactions.get(i);
                if(this.accountsToTransactions.get(account).contains(transaction))
                {
                    throw new TransactionAlreadyExistException(transaction + "Diese Transaction ist doppelt");
                }
                if (transaction.getClass() == Payment.class){

                    if (this.getIncomingInterest() <= 0 || this.getIncomingInterest() > 1)
                        throw new TransactionAttributeException("IncomingInterest liegt nicht zwischen 0 und 1");

                    if (this.getOutgoingInterest() <= 0 || this.getOutgoingInterest() > 1)
                        throw new TransactionAttributeException("OutgoingInterest liegt nicht zwischen 0 und 1");

                    ((Payment) transaction).setIncomingInterest(this.getIncomingInterest());
                    ((Payment) transaction).setOutgoingInterest(this.getOutgoingInterest());

                    this.accountsToTransactions.get(account).add(transaction);
                }else {
                    if (transaction.getClass() == Transfer.class)
                        if (((Transfer) transaction).amount <= 0)
                            throw new TransactionAttributeException("Der Betrag ist negativ.");

                    this.accountsToTransactions.get(account).add(transaction);
                }
            }
            //this.writeAccounts(account);
        }
    }


    public void deleteAccount(String account) throws  AccountDoesNotExistException{
        if(!this.accountsToTransactions.containsKey(account)){
            System.out.println("Konto exestiert nicht");
            throw new AccountDoesNotExistException("Konto exestiert nicht");
        }else {
            this.accountsToTransactions.remove(account);
            System.out.println("Konto " + account + " wurde erfolgreich gelöscht");
        }
    }

    /**
     * Fügt einen Konto eine neue Überweisung hinzu
     *
     * @param account     Konto, die der Überweisung hinzugefügt wird
     * @param transaction Die Überweisung die hinzugefügt wird
     * @throws TransactionAlreadyExistException Überweisung exestiert schon
     * @throws AccountDoesNotExistException Konto exestiert noch nicht
     */
    @Override
    public  void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {

            if (!this.accountsToTransactions.containsKey(account)){
                System.out.println("Konto exestiert nicht");
                throw new AccountDoesNotExistException("Dieses Konto exestiert nicht");
            }else if(this.accountsToTransactions.get(account).contains(transaction)){
                System.out.println("Überweisung exestiert schon");
                throw new TransactionAlreadyExistException("Diese Überweisung exestiert schon");
            }else {
                if (transaction.getClass() == Payment.class) {

                    if (this.getIncomingInterest() <= 0 || this.getIncomingInterest() > 1)
                        throw new TransactionAttributeException("IncomingInterest liegt nicht zwischen 0 und 1");

                    if (this.getOutgoingInterest() <= 0 || this.getOutgoingInterest() > 1)
                        throw new TransactionAttributeException("OutgoingInterest liegt nicht zwischen 0 und 1");

                    String date = transaction.getDate();
                    double amount = transaction.getAmount();
                    String description = transaction.getDescription();
                    double incomingInterest = this.getIncomingInterest();
                    double outgoingInterest = this.getOutgoingInterest();

                    Payment payment = new Payment(date, amount, description, incomingInterest, outgoingInterest);

                    if(this.accountsToTransactions.get(account).contains(payment)){
                        System.out.println("Überweisung exestiert schon");
                        throw new TransactionAlreadyExistException("Diese Überweisung exestiert schon");
                    }

                    this.accountsToTransactions.get(account).add(payment);
                    //this.writeAccounts(account);
                } else {
                    if (transaction.getClass() == OutgoingTransfer.class || transaction.getClass() == IncomingTransfer.class)
                        if (((Transfer) transaction).amount <= 0)
                            throw new TransactionAttributeException("Der Betrag ist negativ.");

                    this.accountsToTransactions.get(account).add(transaction);
                    //this.writeAccounts(account);
                }
            }

    }

    /**
     * Entfernt eine Überweisung vom Konto
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is added to the account
     * @throws TransactionDoesNotExistException Überweisung exestiert nicht
     */
    @Override
    public  void removeTransaction(String account, Transaction transaction) throws TransactionDoesNotExistException, AccountDoesNotExistException {

            if (!this.accountsToTransactions.containsKey(account)){
                System.out.println("Konto exestiert nicht");
                throw new AccountDoesNotExistException("Dieses Konto exestiert nicht");
            }
            else if(!this.accountsToTransactions.get(account).contains(transaction)){
                System.out.println("Entfernen der Überweisung ist fehlgeschlagen, da diese Überweisung nicht exestiert");
                throw new TransactionDoesNotExistException("Diese Überweisung exestiert nicht");
            }else {
                this.accountsToTransactions.get(account).remove(transaction);
                //this.writeAccounts(account);
            }

    }

    /**
     * Überprüft ob eine Überweisung auf einem Konto liegt
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction which is added to the account
     * @return true or false
     */
    @Override
    public  boolean containsTransaction(String account, Transaction transaction){
        return this.accountsToTransactions.get(account).contains(transaction);
    }

    /**
     * Berechnet den Kontostand
     *
     * @param account the selected account
     * @return Kontostand als double
     */
    @Override
    public double getAccountBalance(String account){
        double summe = 0;
        for (Transaction t : this.getTransactions(account)){

            if(t instanceof Payment) summe += t.calculate();

            else if(t instanceof Transfer){

                if (((Transfer) t).getSender().equals(account)) summe -= t.getAmount();

                else summe += t.getAmount();
            }
            else System.out.println("Error");
        }
        return summe;
    }

    /**
     * Gibt eine Liste von Überweisungen aus für ein spezifisches Konto
     *
     * @param account the selected account
     * @return Liste der Überweisungen
     */
    @Override
    public  List<Transaction> getTransactions(String account){
        return this.accountsToTransactions.get(account);
    }

    public List<String> getAllAccounts(){
        List<String> accountlist = new ArrayList<String>();
        Set<String> keys = this.accountsToTransactions.keySet();
        for (String key : keys){
            accountlist.add(key);
        }

        return accountlist;
    }

    /**
     * Gibt die Überweisung als sortierte Liste aus | Absteigen oder Aufsteigend
     * benutzt Selection Sort
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted ascending or descending
     * @return Die sortierte Liste
     */
    @Override
    public  List<Transaction> getTransactionsSorted(String account, boolean asc){
        List<Transaction> ordered = new ArrayList<>(this.accountsToTransactions.get(account));

        if (asc)
            ordered.sort((t1,t2)->Double.compare(t1.calculate(),t2.calculate()));
        if (!asc)
            ordered.sort((t2,t1)->Double.compare(t1.calculate(),t2.calculate()));

        return ordered;
        /**
       if (asc){
            int n = ordered.size();
            for (int j = 1; j < n;j++){
                double key = ordered.get(j).calculate();
                Transaction save = ordered.get(j);
                int i = j-1;
                while ((i >-1)&&(ordered.get(i).calculate() > key)){
                    ordered.set(i+1, ordered.get(i));
                    i--;
                }
                ordered.set(i+1, save);
            }
            return ordered;
        }else {
            int n = ordered.size();
            for (int j = 1; j < n;j++){
                double key = ordered.get(j).calculate();
                Transaction save = ordered.get(j);
                int i = j-1;
                while ((i >-1)&&(ordered.get(i).calculate() < key)){
                    ordered.set(i+1, ordered.get(i));
                    i--;
                }
                ordered.set(i+1, save);
            }
            return ordered;
        }
         **/
    }




    /**
     * Gibt entweder nur Positive oder Negative Überweisungen aus
     *
     * @param account  the selected account
     * @param positive selects if positive  or negative transactions are listed
     * @return Liste von Überweisungen sortiert nach negativ doer positiv
     */
    @Override
    public  List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> ordered = new ArrayList<>(this.accountsToTransactions.get(account));
        double order = 0;
        if (positive) {
            for (int i = 0; i < ordered.size(); i++) {
                Transaction transaction = ordered.get(i);

                if (transaction.calculate() < 0) {
                    ordered.remove(i);
                    i--;
                }
            }
            return ordered;
        } else {
            for (int i = 0; i < ordered.size(); i++) {
                Transaction transaction = ordered.get(i);

                if (transaction.calculate() > 0) {
                    ordered.remove(i);
                    i--;
                }
            }
            return ordered;
        }
    }

}


