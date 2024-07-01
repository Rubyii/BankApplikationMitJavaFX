package ui;
import bank.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class mainpageController {

    //Singleton
    private static final mainpageController singleton = new mainpageController();
    public static mainpageController getInstance(){
        return singleton;
    }

    static String Pfad = "src/main/resources/";
    static PrivateBank privateBank;
    static String ausgewaehlt = "";
    static SceneController sceneController = new SceneController();

    static {
        try {
            privateBank = new PrivateBank("Sparkasse", 0.5, 0.5, Pfad);
        } catch (exceptions.TransactionAttributeException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Parent root ;
    @FXML
    private ListView listViewKonto;

    public List<String> getListeKontoNamen(){
        List<String> listeKontennamen = privateBank.getAllAccounts();
        return listeKontennamen;
    }

    public void listviewKontoHinzufuegen(String _element){
        listViewKonto.getItems().add(_element);
    }

    public void AlertError(String title, String header, String contenttext){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contenttext);
        Optional<ButtonType> resultAlert = alert.showAndWait();
        if (resultAlert.get() == ButtonType.OK) {
            return;
        }
    }

    public void tryCreateaccount(Optional<String> _result){
        try {
            privateBank.createAccount(_result.get());

            listviewKontoHinzufuegen(_result.get());

        }catch (exceptions.AccountAlreadyExistsException | IOException ex) {
            String title = "Fehler bei der Eingabe";
            String header = "Der Account existiert bereits!";
            String text = "Bitte geben Sie einen anderen Namen ein";

            AlertError(title,header,text);
        }
    }

    @FXML
    public void kontohinzufuegen(javafx.event.ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Hinzufügen");
        dialog.setHeaderText("Account anlegen in "+ privateBank.getName());
        dialog.setContentText("Bitte geben Sie den Namen des Accounts ein: ");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (dialog.getEditor().getText().isEmpty()) {
                String title = "Fehler bei der Eingabe";
                String header = "Der Accountname ist nicht gültig";
                String text = "Bitte geben Sie einen gültigen Namen ein";

                AlertError(title,header,text);
            }else {
                tryCreateaccount(result);
            }
        }
    }

    public void initialize() throws exceptions.TransactionAttributeException, exceptions.TransactionAlreadyExistException, exceptions.AccountAlreadyExistsException, exceptions.AccountDoesNotExistException, IOException, ClassNotFoundException {

        //Konton lesen
        privateBank.readAccounts();

        for (String acc : getListeKontoNamen()) {
            listviewKontoHinzufuegen(acc);
        }

        listViewKonto.setCellFactory(listView -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem auswaehlenKnopf = new MenuItem("Auswählen");

            auswaehlenKnopf.setOnAction(event -> {
                ausgewaehlt = (String) listViewKonto.getSelectionModel().getSelectedItem();

                //SceneController
                try {
                    sceneController.switchToScene_AccountView(event, root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


            MenuItem loeschenKnopf = new MenuItem("Löschen");
            loeschenKnopf.setOnAction(event -> {
                ausgewaehlt = (String) listViewKonto.getSelectionModel().getSelectedItem();

                ButtonType ok = new ButtonType("Ja", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Nein", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Test",ok,cancel);
                alert.setTitle("Löschen");
                alert.setHeaderText("Account löschen");
                alert.setContentText("Wollen sie den Account "+ ausgewaehlt +" löschen?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(cancel) == ok) {
                    try {
                        privateBank.deleteAccount(ausgewaehlt);
                    } catch (exceptions.AccountDoesNotExistException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    listViewKonto.getItems().remove(cell.getItem());
                }
            });


            contextMenu.getItems().addAll(auswaehlenKnopf, loeschenKnopf);
            cell.setContextMenu(contextMenu);
            cell.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }
}
