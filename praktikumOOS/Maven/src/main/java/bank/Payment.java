package bank;

/**
 * Klasse soll Ein-/Auszahlung simulieren
 */
public class Payment extends Transaction {

    protected double incomingInterest = 0.0;
    protected double outgoingInterest = 0.0;


    /**
     * DefaultKonstroktur
     */
    public Payment(){}

    /**
     * StandardKonstruktor
     * @param date  Datum der Zahlung in DD.MM.YY
     * @param description   Beschreibung der Zahlung
     * @param amount    Betrag der Zahlung
     */
    public Payment(String date, double amount, String description)
    {
        super(date,amount,description);
    }

    /**
     *PaymentKonstruktor mit Zinswerten
     * @param date  Datum der Zahlung in DD.MM.YY
     * @param description   Beschreibung der Zahlung
     * @param amount    Betrag der Zahlung
     * @param incomingInterest  Einzahlzins
     * @param outgoingInterest  Auszahlzins
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest)
    {
        this(date,amount,description);

        setOutgoingInterest(outgoingInterest);
        setIncomingInterest(incomingInterest);
    }

    /**
     * Copy Konstruktor
     * @param p Objekt des Typs Payment als instanziierungs Variable
     */
    public Payment(Payment p)
    {
        this(p.date, p.amount, p.description, p.incomingInterest, p.outgoingInterest);

    }

    /**
     * Alle Getter und Setter
     */
    public void setIncomingInterest(double incomingInterest)
    {
        if(incomingInterest < 0 || incomingInterest > 1)
        {
            System.out.println("error");
        }else
        {
            this.incomingInterest = incomingInterest;
        }
    }
    public double getIncomingInterest(){ return this.incomingInterest; }

    public void setOutgoingInterest(double outgoingInterest)
    {
        if(outgoingInterest < 0 || outgoingInterest > 1)
        {
            System.out.println("error");
        }else
        {
            this.outgoingInterest = outgoingInterest;
        }
    }
    public double getOutgoingInterest(){ return this.outgoingInterest; }


    /**
     * To-String überschreibung
     * @return gibt alle Daten aus von Payment
     */
    @Override
    public String toString(){
        return (
                        super.toString() +
                        "Einzahlzinsen: " + this.incomingInterest + "\n" +
                        "Auszahlzinsen: " + this.outgoingInterest + "\n"
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

            Payment payment = (Payment) obj;
            return Double.compare(incomingInterest, payment.incomingInterest) == 0 &&
                    Double.compare(outgoingInterest, payment.outgoingInterest) == 0;
        }
        else {
            return false;
        }
    }

    /**
     * Rechnet den Einzahl-/Auszahlwert mit Zinsen
     * @return double Wert
     */
    @Override
    public double calculate() {
        if (amount > 0){
            double einzahlung = amount - (amount*incomingInterest);
            return (Math.round(einzahlung*100)/100);
        }
        else if(amount < 0){
            double auszahlung = amount + (amount*outgoingInterest);
            return (Math.round(auszahlung*100)/100);
        }
        else {
            return 0;
        }
    }

}
