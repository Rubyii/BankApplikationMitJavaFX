package bank;

public class exceptions {

    /**
     * Konto exestiert nicht
     */
    public static class AccountDoesNotExistException extends Exception{
        /**
         * Gibt Fehler Nachricht aus
         * @param ausgabe Fehlernachricht
         */
        public AccountDoesNotExistException(String ausgabe){
            super(ausgabe);
        }
    }

    /**
     * Konto exestiert schon
     */
    public static class AccountAlreadyExistsException extends Exception{
        /**
         * Gibt Fehler Nachricht aus
         * @param ausgabe Fehlernachricht
         */
        public AccountAlreadyExistsException(String ausgabe){
            super(ausgabe);

        }
    }

    /**
     * Überweisung exestiert nicht
     */
    public static class TransactionAlreadyExistException extends Exception{
        /**
         * Gibt Fehler Nachricht aus
         * @param ausgabe Fehlernachricht
         */
        public TransactionAlreadyExistException(String ausgabe ){
            super(ausgabe);
        }
    }

    /**
     * Überweisung exestiert schon
     */
    public static class TransactionDoesNotExistException extends Exception{
        /**
         * Gibt Fehler Nachricht aus
         * @param ausgabe Fehlernachricht
         */
        public TransactionDoesNotExistException(String ausgabe){ super(ausgabe); }
    }

    /**
     * Transaktion attribut exestiert schon
     */
    public static class TransactionAttributeException extends Exception{
        /**
         * Gibt Fehler Nachricht aus
         * @param ausgabe Fehlernachricht
         */
        public TransactionAttributeException(String ausgabe){ super(ausgabe); }
    }
}
