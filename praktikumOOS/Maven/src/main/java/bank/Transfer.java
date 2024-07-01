package bank;

/**
 * Die Klasse simuliert eine Überweisung
 */
public class Transfer extends Transaction{

    protected String sender;
    protected String recipient;

    /**
     * DefaultKonstruktor
     */
    public Transfer(){}

    /**
     * StandardKonstruktor
     * @param date  Datum der Zahlung in DD.MM.YY
     * @param description   Beschreibung der Zahlung
     * @param amount    Betrag der Zahlung
     */
    public Transfer(String date, double amount , String description)
    {
        super(date, amount, description);
        setSender("kein Sender");
        setRecipient("kein Empfänger");
    }

    /**
     *TransferKonstruktor mit Sender/Empfaenger
     * @param date  Datum der Zahlung in DD.MM.YY
     * @param description   Beschreibung der Zahlung
     * @param amount    Betrag der Zahlung
     * @param sender  Sender der Überweisung
     * @param recipient  Empfänger der Überweisung
     */
    public Transfer(String date, double amount, String description, String sender, String recipient)
    {
        super(date,amount,description);
        setAmount(amount);
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     * Copy Konstruktor
     * @param p Objekt des Typs Payment als instanziierungs Variable
     */
    public Transfer(Transfer p)
    {
        this(p.date, p.amount, p.description, p.sender, p.recipient);
        setAmount(p.amount);
    }

    /**
     * Alle Getter und Setter
     */
    public void setSender(String sender){ this.sender = sender; }
    public String getSender(){ return this.sender; }
    public void setRecipient(String recipient){ this.recipient = recipient; }
    public String getRecipient(){ return this.recipient; }

    public void setAmount(double amount) {
        if (amount >= 0){
            this.amount = amount;
        }else  {
            System.out.println("Der Betrag ist negativ.");
            this.amount = 0;
            return;
        }
    }


    /**
     * To-String überschreibung
     * @return gibt alle Daten aus von Transfer
     */
    @Override
    public String toString(){
        return (
                super.toString() +
                "Sender: " + this.sender + "\n" +
                "Empfänger: " + this.recipient + "\n"
                );
    }

    /**
     * Verlgeicht Objekte
     * @param obj Vergleichsobjekt
     * @return gibt true oder false aus
     */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {

            Transfer transfer = (Transfer) obj;
            return sender.compareTo(transfer.sender) == 0 &&
                    recipient.compareTo(transfer.recipient) == 0 &&
                    description.compareTo(transfer.description) == 0;
        }
        else {
            return false;
        }
    }

    /**
     * Bei der Überweisung gibt es keine Zinsen,
     * also auch keine Berechnung
     * @return Anfangsbetrag
     */
    @Override
    public double calculate() {
        return amount;
    }


}
