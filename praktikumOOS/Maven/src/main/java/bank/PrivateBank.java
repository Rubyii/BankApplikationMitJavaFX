package bank;

import bank.exceptions.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import com.google.gson.*;

/**
 * Die Klasse simuliert eine Bank die Konten und Transaktionen verwalten und verarbeiten kann.
 */
public class PrivateBank implements Bank{
    public String name;
    public double incomingInterest;
    public double outgoingInterest;
    public Map<String, List<Transaction>> accountsToTransactions = new HashMap();

    public String directoryName = "";


    /**
     * Konstruktor
     * @param name Name der PrivatenBank
     * @param incomingInterest  Einzahlzins
     * @param outgoingInterest  Auszahlzins
     */

    public PrivateBank(String name, double incomingInterest, double outgoingInterest) throws TransactionAttributeException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * @param name Name der privaten Bank
     * @param incomingInterest Einzahlzins Wertebereich(0-1)
     * @param outgoingInterest Einzahlzins Wertebereich(0-1)
     * @param directoryName Speicherort der Json dateien
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName) throws TransactionAttributeException {
        this(name, incomingInterest, outgoingInterest);
        setDirectoryName(directoryName);
    }


    /**
     * Copy-Konstruktor
     * @param p Objekt vom Typ PrivatBank
     */
    public PrivateBank(PrivateBank p) throws TransactionAttributeException {
        this(p.name,p.incomingInterest,p.outgoingInterest, p.directoryName);
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
     * DirectoryNameSetter
     * @param directoryName DirectoryName
     */
    public void setDirectoryName(String directoryName) {this.directoryName = directoryName;}

    /**
     * directoryNameGetter
     * @return Gibt die directoryName zurueck
     */
    public String getdirectoryName(){ return this.directoryName; }



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
            PrivateBank privatebank = (PrivateBank) obj;
            if (((PrivateBank) obj).accountsToTransactions.size() != this.accountsToTransactions.size()) return false;

            if ( !(((PrivateBank) obj).accountsToTransactions.equals(this.accountsToTransactions))) return false;

            return Double.compare(incomingInterest, this.incomingInterest) == 0 &&
                    Double.compare(outgoingInterest, this.outgoingInterest) == 0 &&
                    name.compareTo(privatebank.name) == 0 &&
                    directoryName.compareTo(privatebank.directoryName) == 0;
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
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        if (this.accountsToTransactions.containsKey(account)) {
            System.out.println("Dieses Konto exestiert schon");
            throw new AccountAlreadyExistsException("Dieses Konto exestiert schon");
        } else {
            List<Transaction> list = new ArrayList<>();
            this.accountsToTransactions.put(account, list);
            this.writeAccounts(account);
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
    public  void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {

        if (this.accountsToTransactions.containsKey(account)) {
            System.out.println("Dieses Konto exestiert schon");
            throw new AccountAlreadyExistsException("Dieses Konto exestiert schon");
        }else{
            this.accountsToTransactions.put(account, new ArrayList<>());
            for (Transaction transaction : transactions) {
                if (this.accountsToTransactions.get(account).contains(transaction)) {
                    throw new TransactionAlreadyExistException(transaction + "Diese Transaction ist doppelt");
                }
                if (transaction.getClass() == Payment.class) {

                    if (this.getIncomingInterest() <= 0 || this.getIncomingInterest() > 1)
                        throw new TransactionAttributeException("IncomingInterest liegt nicht zwischen 0 und 1");

                    if (this.getOutgoingInterest() <= 0 || this.getOutgoingInterest() > 1)
                        throw new TransactionAttributeException("OutgoingInterest liegt nicht zwischen 0 und 1");

                    ((Payment) transaction).setIncomingInterest(this.getIncomingInterest());
                    ((Payment) transaction).setOutgoingInterest(this.getOutgoingInterest());

                    this.accountsToTransactions.get(account).add(transaction);
                } else {
                    if (transaction.getClass() == Transfer.class)
                        if (transaction.amount <= 0)
                            throw new TransactionAttributeException("Der Betrag ist negativ.");

                    this.accountsToTransactions.get(account).add(transaction);
                }
            }
            this.writeAccounts(account);
        }
    }


    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if(!this.accountsToTransactions.containsKey(account)){
            System.out.println("Konto exestiert nicht");
            throw new AccountDoesNotExistException("Konto exestiert nicht");
        }else {
            this.accountsToTransactions.remove(account);
            System.out.println(this.getAllAccounts());

            String[] pathnames;
            File f = new File(this.directoryName);
            pathnames = f.list();

            for (String pathname : pathnames){
                if (pathname.contains(account + ".json")){
                    File deleteF = new File(directoryName + pathname);
                    if (deleteF.delete()){
                        System.out.println("Konto " + account + " wurde erfolgreich gelöscht");
                    }else {
                        System.out.println("Konto wurde nicht gelöscht");
                    }
                }
            }


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
    public  void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

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
                    this.writeAccounts(account);
                } else {
                    if (transaction.getClass() == OutgoingTransfer.class || transaction.getClass() == IncomingTransfer.class)
                        if (transaction.amount <= 0)
                            throw new TransactionAttributeException("Der Betrag ist negativ.");

                    this.accountsToTransactions.get(account).add(transaction);
                    this.writeAccounts(account);
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
    public  void removeTransaction(String account, Transaction transaction) throws TransactionDoesNotExistException, AccountDoesNotExistException, IOException {

            if (!this.accountsToTransactions.containsKey(account)){
                System.out.println("Konto exestiert nicht");
                throw new AccountDoesNotExistException("Dieses Konto exestiert nicht");
            }
            else if(!this.accountsToTransactions.get(account).contains(transaction)){
                System.out.println("Entfernen der Überweisung ist fehlgeschlagen, da diese Überweisung nicht exestiert");
                throw new TransactionDoesNotExistException("Diese Überweisung exestiert nicht");
            }else {
                this.accountsToTransactions.get(account).remove(transaction);
                this.writeAccounts(account);
            }

    }

    /**
     * Überprüft, ob eine Überweisung auf einem Konto liegt
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
        for (Transaction t : getTransactions(account)){
            summe += t.calculate();
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
        Set<String> keys = this.accountsToTransactions.keySet();

        return new ArrayList<String>(keys);
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

    /**
     * CUSTOM Payment Serializer
     */

    public class PaymentSerializer implements JsonSerializer<Payment> {

        /**
         *
         * @param src Objekt des Types Payment
         * @param typeOfSrc
         * @param context
         * @return JsonObject, das Payment serialisiert
         * Wandelt das Objekt Payment in JSON Format um
         * Wie folgt:
         * {
         *     "Classname": "Payment",
         *     "Instance":{
         *         "incomingInterest": 0.5,
         *         "outgoingInterest": 0.5,
         *         "date": "1.1.2022",
         *         "description": "Einzahlung",
         *         "amount" 500
         *     }
         * }
         */


        @Override
        public JsonElement serialize(Payment src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonPayment = new JsonObject();
            jsonPayment.addProperty("CLASSNAME", src.getClass().getSimpleName());
            jsonPayment.add("INSTANCE", new Gson().toJsonTree(src));

            return jsonPayment;
        }

    }

    /**
     * CUSTOM IncomingTransfer Serializer
     */

    public class IncomingTransferSerializer implements JsonSerializer<IncomingTransfer> {

        /**
         *
         * @param src Objekt des Types IncomingTransfer
         * @param typeOfSrc
         * @param context
         * @return JsonObject, das IncomingTransfer serialisiert
         * Wandelt das Objekt Payment in JSON Format um
         * Wie folgt:
         * {
         *     "Classname": "IncomingTransfer",
         *     "Instance":{
         *         "sender": "Jens",
         *         "recipient": "Ruben",
         *         "date": "1.1.2022",
         *         "description": "Erhalten von Jens",
         *         "amount" 500
         *     }
         * }
         */


        @Override
        public JsonElement serialize(IncomingTransfer src, Type typeOfSrc, JsonSerializationContext context) {

            JsonObject jsonIncomingTransfer = new JsonObject();
            jsonIncomingTransfer.addProperty("CLASSNAME", src.getClass().getSimpleName());
            jsonIncomingTransfer.add("INSTANCE", new Gson().toJsonTree(src));

            return jsonIncomingTransfer;
        }
    }

    /**
     * CUSTOM OutgoingTransfer Serializer
     */

    public class OutgoingTransferSerializer implements JsonSerializer<OutgoingTransfer> {

        /**
         *
         * @param src Objekt des Types OutgoingTransfer
         * @param typeOfSrc
         * @param context
         * @return JsonObject, das OutgoingTransfer serialisiert
         * Wandelt das Objekt Payment in JSON Format um
         * Wie folgt:
         * {
         *     "Classname": "OutgoingTransfer",
         *     "Instance":{
         *         "sender": "Jens",
         *         "recipient": "Ruben",
         *         "date": "1.1.2022",
         *         "description": "Gesendet an Ruben",
         *         "amount" 500
         *     }
         * }
         */



        @Override
        public JsonElement serialize(OutgoingTransfer src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonOutgoingTransfer = new JsonObject();
            jsonOutgoingTransfer.addProperty("CLASSNAME", src.getClass().getSimpleName());
            jsonOutgoingTransfer.add("INSTANCE", new Gson().toJsonTree(src));

            return jsonOutgoingTransfer;
        }
    }



    /**
     * Custom Payment Deserializer
     */

    public class PaymentDeserializer implements JsonDeserializer<Payment>{
        /**
         *
         * @param json JsonElement, das gelesen wird
         * @param typeofT Typ des Objektes, das deserialisiert wird
         * @param context
         * @return Payment Objekt
         */

        @Override
        public Payment deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context){
            JsonObject jsonObjectPayment = json.getAsJsonObject();



            Payment payment = new Payment(
                    jsonObjectPayment.get("date").getAsString(),
                    jsonObjectPayment.get("amount").getAsDouble(),
                    jsonObjectPayment.get("description").getAsString(),
                    jsonObjectPayment.get("incomingInterest").getAsDouble(),
                    jsonObjectPayment.get("outgoingInterest").getAsDouble()
            );

            return payment;
        }
    }

    /**
     * Custom IncomingTransfer Deserializer
     */

    public class IncomingTransferDeserializer implements JsonDeserializer<IncomingTransfer>{
        /**
         *
         * @param json JsonElement, das gelesen wird
         * @param typeofT Typ des Objektes, das deserialisiert wird
         * @param context
         * @return IncomingTransfer Objekt
         */

        @Override
        public IncomingTransfer deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context){
            JsonObject jsonObjectIncomingTransfer = json.getAsJsonObject();

            IncomingTransfer incomingTransfer = new IncomingTransfer(
                    jsonObjectIncomingTransfer.get("date").getAsString(),
                    jsonObjectIncomingTransfer.get("amount").getAsDouble(),
                    jsonObjectIncomingTransfer.get("description").getAsString(),
                    jsonObjectIncomingTransfer.get("sender").getAsString(),
                    jsonObjectIncomingTransfer.get("recipient").getAsString()
            );

            return incomingTransfer;
        }
    }

    /**
     * Custom OutgoingTransfer Deserializer
     */

    public class OutgoingTransferDeserializer implements JsonDeserializer<OutgoingTransfer>{
        /**
         *
         * @param json JsonElement, das gelesen wird
         * @param typeofT Typ des Objektes, das deserialisiert wird
         * @param context
         * @return IncomingTransfer Objekt
         */

        @Override
        public OutgoingTransfer deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context){
            JsonObject jsonObjectOutgoingTransfer = json.getAsJsonObject();

            OutgoingTransfer outgoingTransfer = new OutgoingTransfer(
                    jsonObjectOutgoingTransfer.get("date").getAsString(),
                    jsonObjectOutgoingTransfer.get("amount").getAsDouble(),
                    jsonObjectOutgoingTransfer.get("description").getAsString(),
                    jsonObjectOutgoingTransfer.get("sender").getAsString(),
                    jsonObjectOutgoingTransfer.get("recipient").getAsString()
            );

            return outgoingTransfer;
        }
    }

    /**
     * serialisiert und speichert das Konto ab
     * @param account Kontoname
     */
    public void writeAccounts(String account) throws IOException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        //Custom Serializer hinzufuegen
        gsonBuilder.registerTypeAdapter(IncomingTransfer.class, new IncomingTransferSerializer());
        gsonBuilder.registerTypeAdapter(OutgoingTransfer.class, new OutgoingTransferSerializer());
        gsonBuilder.registerTypeAdapter(Payment.class, new PaymentSerializer());
        Gson customGson = gsonBuilder.create();

        //Konvertiert das Objekt in JSON String format
        String kontoJson = customGson.toJson(this.accountsToTransactions.get(account));

        //System.out.println(kontoJson);

        //Pfad abspeichern
        BufferedWriter bw = new BufferedWriter(new FileWriter(directoryName + account + ".json"));
        bw.write(kontoJson);
        bw.flush();
        bw.close();
    }

    /**
     * Liest alle vorhandenen Konten aus dem Ordner aus und stellt sie zu Verfuegung
     */
    public void readAccounts() throws IOException, ClassNotFoundException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //Custom Serializer hinzufuegen
        gsonBuilder.registerTypeAdapter(IncomingTransfer.class, new IncomingTransferDeserializer());
        gsonBuilder.registerTypeAdapter(OutgoingTransfer.class, new OutgoingTransferDeserializer());
        gsonBuilder.registerTypeAdapter(Payment.class, new PaymentDeserializer());
        Gson customGson = gsonBuilder.create();

        //Erstellt Liste aller Dateien im ausgewaehlten Ordner
        String[] pathnames;
        File f = new File(directoryName);
        pathnames = f.list();

        //Geht alle Dateien durch
        for (String pathname : pathnames){
            String kontoname = pathname;

            //Erstellt einen Kontonamen fuer Dateien mit Endung ".json"
            if (pathname.endsWith(".json")){
                kontoname = kontoname != null && !kontoname.isEmpty() ? kontoname.substring(0, kontoname.length()-5) : "Unbekannt";
            }else continue;

            //Datei einlesen und TypeCast JsonArray
            Object object = JsonParser.parseReader(new FileReader(directoryName + pathname));
            JsonArray ja = (JsonArray) object;
            int JsonArraySize = ja.size();

            //Konto Erstellen falls nicht exestiert
            if (!this.accountsToTransactions.containsKey(kontoname))
                this.createAccount(kontoname);


            //JSON Array durchgehen
            for (int i = 0; i < JsonArraySize; i++){
                JsonObject jo = ja.get(i).getAsJsonObject();
                String klassenname = jo.get("CLASSNAME").getAsString();
                JsonObject joInstance = jo.get("INSTANCE").getAsJsonObject();

                //Klassen Unterscheidung
                switch (klassenname) {
                    case "Payment" -> {
                        Payment payment = customGson.fromJson(joInstance, Payment.class);
                        if (!this.accountsToTransactions.get(kontoname).contains(payment)) {
                            this.addTransaction(kontoname, payment);
                        }
                    }
                    case "OutgoingTransfer" -> {
                        OutgoingTransfer outgoingTransfer = customGson.fromJson(joInstance, OutgoingTransfer.class);
                        if (!this.accountsToTransactions.get(kontoname).contains(outgoingTransfer)) {
                            this.addTransaction(kontoname, outgoingTransfer);
                        }
                    }
                    case "IncomingTransfer" -> {
                        IncomingTransfer incomingTransfer = customGson.fromJson(joInstance, IncomingTransfer.class);
                        if (!this.accountsToTransactions.get(kontoname).contains(incomingTransfer)) {
                            this.addTransaction(kontoname, incomingTransfer);
                        }
                    }
                }
            }

        }

    }

}

