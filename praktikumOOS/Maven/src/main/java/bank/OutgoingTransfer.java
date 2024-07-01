package bank;

public class OutgoingTransfer extends Transfer implements CalculateBill{
    /**
     * DefaultKonstruktor
     */
    public OutgoingTransfer(){}

    /**
     * StandardKonstruktor
     * @param date  Datum der Zahlung in DD.MM.YY
     * @param description   Beschreibung der Zahlung
     * @param amount    Betrag der Zahlung
     */
    public OutgoingTransfer(String date,  double amount, String description)
    {
        super(date, amount, description);
        //this.amount = amount;
    }

    /**
     * OutgoingTransfer mit Zinswerten
     * @param date  Datum der Zahlung in DD.MM.YY
     * @param description   Beschreibung der Zahlung
     * @param amount    Betrag der Zahlung
     * @param sender  Sender der Überweisung
     * @param recipient  Empfänger der Überweisung
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient)
    {
        super(date, amount, description);
        setSender(sender);
        setRecipient(recipient);
        //this.amount = amount;
    }

    /**
     * Copy Konstruktor
     * @param p Objekt des Typs Payment als instanziierungs Variable
     */
    public OutgoingTransfer(Transfer p)
    {
        this(p.date, p.amount, p.description, p.sender, p.recipient);
       // this.amount = amount;
    }

    /**
     * Alle Getter und Setter
     */
    public void setSender(String sender){ this.sender = sender; }
    public String getSender(){ return this.sender; }
    public void setRecipient(String recipient){ this.recipient = recipient; }
    public String getRecipient(){ return this.recipient; }

    /**
     * To-String überschreibung
     * @return gibt alle Daten aus von Transfer
     */
    @Override
    public String toString(){
        return super.toString();
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
                    recipient.compareTo(transfer.recipient) == 0;
        }
        else {
            return false;
        }
    }

    /**
     * Gibt amount der Sendung aus
     * @return negativ da man Geld sendet
     */
    @Override
    public double calculate(){
        return -this.amount;
    }
}
