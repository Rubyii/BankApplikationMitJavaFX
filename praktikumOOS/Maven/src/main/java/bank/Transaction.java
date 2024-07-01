package bank;

/**
 * Beschreibt allgemeines Verhalten bei transaktionen
 */
public abstract class Transaction implements CalculateBill {

    protected String date;
    protected String description;
    protected double amount;

    /**
     * DefaultKonstruktor
     */
    public Transaction(){}

    /**
     * StandardKonstruktor
     * @param date  Datum der Zahlung in DD.MM.YY
     * @param description   Beschreibung der Zahlung
     * @param amount    Betrag der Zahlung
     */
    public Transaction(String date, double amount, String description) {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * To-String überschreibung
     * @return gibt alle Daten aus von Transaction
     */
    @Override
    public String toString(){
        return ("Datum: " + this.date + "\n" +
                "Beschreibung: " + this.description +"\n" +
                "Betrag: " + this.calculate() + "\n");
    }

    /**
     * Verlgeicht Objekte
     * @param obj Vergleichsobjekt
     * @return gibt true oder false aus
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        Transaction transaction = (Transaction) obj;
        return date.compareTo(transaction.date) == 0 &&
                description.compareTo(transaction.description) == 0 &&
                Double.compare(amount, transaction.amount) == 0;
    }


    /**
     * alle Getter und Setter
     */
    public void setDate(String date){ this.date = date; }
    public String getDate(){ return this.date; }
    public void setDescription(String description){ this.description = description; }
    public String getDescription(){ return this.description; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getAmount(){ return this.amount; }
}
