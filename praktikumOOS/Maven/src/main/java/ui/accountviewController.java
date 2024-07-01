package ui;
import bank.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class accountviewController {


    @FXML
    Text kontoNameText = new Text();
    @FXML
    Text betragText = new Text();
    @FXML
    MenuButton transaktionHinzufuegenKnopf = new MenuButton();
    @FXML
    MenuItem auszahlung = new MenuItem("Zahlung");
    @FXML
    MenuItem transfer = new MenuItem("Transfer");
    @FXML
    MenuButton sortierenMenu = new MenuButton();
    @FXML
    MenuItem aufsteigendSortiert = new MenuItem();
    @FXML
    MenuItem absteigendSortiert = new MenuItem();
    @FXML
    MenuItem positivSortiert = new MenuItem();
    @FXML
    MenuItem negativSortiert = new MenuItem();
    @FXML
    ListView listViewTransaktionen = new ListView<>();
    @FXML
    Button zurueckKnopf = new Button();

    private mainpageController mainpageControllerSingleton = mainpageController.getInstance();

    private String kontoName = mainpageControllerSingleton.ausgewaehlt;
    private PrivateBank privateBankAccountView = mainpageControllerSingleton.privateBank;
    private String transactionAusgewaehlt;
    private List<Transaction> sortedList = new ArrayList<>();
    private List<Transaction> listTransaction = new ArrayList<>(getListeTransaktionen());

    public List<Transaction> getListeTransaktionen(){
        List<Transaction> listeTransaktionen = privateBankAccountView.getTransactions(kontoName);

        return listeTransaktionen;
    }

    public void setBetragText(){
        double betrag = privateBankAccountView.getAccountBalance(kontoName);
        betragText.setText(String.valueOf(betrag) + "€");
    }

    public void listViewTransaktionenHinzufuegen(Transaction _transaction){
        String transkationenToString = _transaction.toString();
        listViewTransaktionen.getItems().add(transkationenToString);
    }

    public void AlertError(String _title, String _header, String _content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(_title);
        alert.setHeaderText(_header);
        alert.setContentText(_content);
        Optional<ButtonType> resultAlert = alert.showAndWait();
        if (resultAlert.get() == ButtonType.OK) {
            return;
        }
    }

    public void initialize(){
        kontoNameText.setText(kontoName);
        setBetragText();


        for (Transaction transaction :listTransaction) {
            listViewTransaktionenHinzufuegen(transaction);
        }


        auszahlung.setOnAction(event -> {
            System.out.println("Zahlung");
            //Custom Dialog
            Dialog dialog = new Dialog<>();
            dialog.setTitle("Zahlung");

            // Set the button types.
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Abrrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField date = new TextField();
            date.setPromptText("Datum");
            TextField description = new TextField();
            description.setPromptText("Beschreibung");
            TextField amount = new TextField();
            amount.setPromptText("Betrag");

            gridPane.add(date, 0, 0);
            gridPane.add(amount, 1, 0);
            gridPane.add(description, 2, 0);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(() -> date.requestFocus());

            Optional result = dialog.showAndWait();

            if (result.orElse(cancelButton) == okButton) {{
                System.out.println(
                        "Datum = " + date.getText() +
                        "Betrag = " + amount.getText() +
                        "Beschreibung = " + description.getText() );
                //trycatch einzelne Variabeln checken und Fehler anzeigen
                Payment payment = new Payment();
                payment.setOutgoingInterest(privateBankAccountView.getOutgoingInterest());
                payment.setIncomingInterest(privateBankAccountView.getIncomingInterest());

                if (date.getText().isEmpty() || amount.getText().isEmpty() || description.getText().isEmpty()){
                    String title = "Fehler bei der Eingabe";
                    String header = "Die Eingabe ist nicht gültig";
                    String content = "Bitte füllen sie alle Eingaben aus";

                    AlertError(title,header,content);
                    return;
                }

                try {
                    payment.setAmount(Double.parseDouble(amount.getText()));
                } catch(NumberFormatException e){
                    String title = "Fehler bei der Eingabe";
                    String header = "Der Betrag ist keine Zahl";
                    String content = "Bitte geben Sie einen gültigen Betrag ein";

                    AlertError(title,header,content);
                    return;
                }

                payment.setDate(date.getText());
                payment.setDescription(description.getText());

                try {
                    privateBankAccountView.addTransaction(kontoName, payment);
                    setBetragText();
                    listViewTransaktionen.getItems().add(payment.toString());
                } catch (exceptions.TransactionAlreadyExistException e) {
                    String title = "Fehler bei der Eingabe";
                    String header = "Die Transaktion exestiert schon";
                    String content = "Bitte geben Sie eine gültige Transaktion ein";

                    AlertError(title, header, content);

                } catch (exceptions.AccountDoesNotExistException e) {
                    throw new RuntimeException(e);
                } catch (exceptions.TransactionAttributeException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }}

        });

        transfer.setOnAction(event -> {
            System.out.println("transfer");

            //Custom Dialog
            Dialog dialog = new Dialog<>();
            dialog.setTitle("Auszahlung");

            // Set the button types.
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Abrrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField sender = new TextField();
            sender.setPromptText("Sender");

            TextField recipient = new TextField();
            recipient.setPromptText("Empfänger");

            TextField date = new TextField();
            date.setPromptText("Datum");

            TextField description = new TextField();
            description.setPromptText("Beschreibung");

            TextField amount = new TextField();
            amount.setPromptText("Betrag");

            gridPane.add(sender, 1, 0);
            gridPane.add(recipient, 2, 0);
            gridPane.add(date, 1, 1);
            gridPane.add(amount, 2, 1);
            gridPane.add(description, 3, 1);
            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(() -> date.requestFocus());

            Optional result = dialog.showAndWait();

            if (result.orElse(cancelButton) == okButton) {
                System.out.println(
                        "Sender = " + sender.getText() +
                        "Empfänger = " + recipient.getText() +
                        "Datum = " + date.getText() +
                        "Betrag = " + amount.getText() +
                        "Beschreibgung = " + description.getText() );
                //trycatch einzelne Variabeln checken und Fehler anzeigen
                //Outgoing und Incoming beachten Sender und kontoname vergleichen


                if (sender.getText().isEmpty()||recipient.getText().isEmpty()||date.getText().isEmpty() ||
                        amount.getText().isEmpty() || description.getText().isEmpty()){

                    String title = "Fehler bei der Eingabe";
                    String header = "Die Eingabe ist nicht gültig";
                    String content = "Bitte füllen sie alle Eingaben aus";

                    AlertError(title, header, content);
                    return;
                }

                try {
                    Double.parseDouble(amount.getText());
                    if (Double.parseDouble(amount.getText()) <= 0){

                        String title = "Fehler bei der Eingabe";
                        String header = "Der Betrag ist negativ";
                        String content = "Bitte geben Sie einen gültigen Betrag ein";

                        AlertError(title,header,content);
                        return;
                    }
                } catch(NumberFormatException e){

                    String title = "Fehler bei der Eingabe";
                    String header = "Der Betrag ist keine Zahl";
                    String content = "Bitte geben Sie einen gültigen Betrag ein";

                    AlertError(title,header,content);
                    return;
                }

                if (sender.getText().equals(recipient.getText())){
                    String title = "Fehler bei der Eingabe";
                    String header = "Der Sender und Empfänger ist gleich";
                    String content = "Bitte geben Sie den Kontonamen entweder im Sender oder Empfänger ein";

                    AlertError(title,header,content);
                    return;
                }

                Transfer transfer1 = new Transfer();

                if (sender.getText().equals(kontoName)){
                    OutgoingTransfer outgoingTransfer =  new OutgoingTransfer();
                    transfer1 = outgoingTransfer;
                    System.out.println(transfer1.getClass().toString());
                }else if (recipient.getText().equals(kontoName)){
                    IncomingTransfer incomingTransfer = new IncomingTransfer();
                    transfer1 = incomingTransfer;
                    System.out.println(transfer1.getClass().toString());
                }else {
                    String title = "Fehler bei der Eingabe";
                    String header = "Der Sender oder Empfänger ist falsch";
                    String content = "Bitte geben Sie den Kontoname als Sender oder Empfänger ein";

                    AlertError(title,header,content);
                    return;
                }

                transfer1.setSender(sender.getText());
                transfer1.setRecipient(recipient.getText());
                transfer1.setDate(date.getText());
                transfer1.setAmount(Double.parseDouble(amount.getText()));
                transfer1.setDescription(description.getText());

                try {
                    privateBankAccountView.addTransaction(kontoName, transfer1);
                    setBetragText();
                    listViewTransaktionen.getItems().add(transfer1.toString());
                } catch (exceptions.TransactionAlreadyExistException e) {
                    String title = "Fehler bei der Eingabe";
                    String header = "Die Transaktion exestiert schon";
                    String content = "Bitte geben Sie eine gültige Transaktion ein";

                    AlertError(title,header,content);
                    return;
                } catch (exceptions.AccountDoesNotExistException e) {
                    throw new RuntimeException(e);
                } catch (exceptions.TransactionAttributeException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        zurueckKnopf.setOnAction(event -> {
            try {
                mainpageControllerSingleton.sceneController.switchToScene_MainView(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        aufsteigendSortiert.setOnAction(event -> {
            sortedList = privateBankAccountView.getTransactionsSorted(kontoName,true);

            listViewTransaktionen.getItems().clear();

            for (Transaction transaction : sortedList) {
                listViewTransaktionenHinzufuegen(transaction);
            }
        });

        absteigendSortiert.setOnAction(event -> {
            sortedList = privateBankAccountView.getTransactionsSorted(kontoName,false);

            listViewTransaktionen.getItems().clear();

            for (Transaction transaction : sortedList) {
                listViewTransaktionenHinzufuegen(transaction);
            }

        });

        positivSortiert.setOnAction(event -> {
            sortedList = privateBankAccountView.getTransactionsByType(kontoName,true);

            listViewTransaktionen.getItems().clear();

            for (Transaction transaction : sortedList) {
                listViewTransaktionenHinzufuegen(transaction);
            }

        });

        negativSortiert.setOnAction(event -> {
            sortedList = privateBankAccountView.getTransactionsByType(kontoName,false);

            listViewTransaktionen.getItems().clear();

            for (Transaction transaction : sortedList) {
               listViewTransaktionenHinzufuegen(transaction);
            }

        });

        listViewTransaktionen.setCellFactory(listView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem loeschenKnopf = new MenuItem("Löschen");
            loeschenKnopf.setOnAction(event -> {
                transactionAusgewaehlt = (String) listViewTransaktionen.getSelectionModel().getSelectedItem();

                ButtonType ok = new ButtonType("Ja", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Nein", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Test",ok,cancel);
                alert.setTitle("Löschen");
                alert.setHeaderText("Transaktion löschen");
                alert.setContentText("Wollen sie die Transaktion löschen?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(cancel) == ok) {
                    try {
                        for (Transaction transaction : listTransaction){
                            if (transaction.toString().equals(transactionAusgewaehlt)){
                                privateBankAccountView.removeTransaction(kontoName,transaction);
                                setBetragText();
                                System.out.println("GELOOOOEOSCHT");
                                break;
                            }
                        }
                    } catch (exceptions.AccountDoesNotExistException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (exceptions.TransactionDoesNotExistException e) {
                        throw new RuntimeException(e);
                    }

                    listViewTransaktionen.getItems().remove(cell.getItem());
                }
            });


            contextMenu.getItems().addAll(loeschenKnopf);

            cell.setContextMenu(contextMenu);

            cell.textProperty().bind(cell.itemProperty());

            return cell;
        });


    }

}
