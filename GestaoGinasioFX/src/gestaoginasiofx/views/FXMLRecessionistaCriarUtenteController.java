/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import gestaoginasiofx.FillComboBox;
import gestaoginasiohibernate.model.Tipocontrato;
import gestaoginasiohibernate.model.Utente;
import java.io.IOException;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXMLRecessionistaCriarUtenteController implements Initializable {
    private Tipocontrato tipoContrato;
    private Utente utente;
    
    @FXML private Button btPagamento;
    @FXML private ComboBox<Tipocontrato> cbTipoContrato;
    @FXML private TextField txtMensalidade;
    @FXML private TextField txtNome;
    @FXML private TextField txtNIF;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtMorada;
    @FXML private TextField txtLocalidade;
    @FXML private TextField txtCodPostal;
    @FXML private DatePicker dpDateNascimento;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.cbTipoContrato.setConverter(FillComboBox.fillTipoContratoComboBox(this.cbTipoContrato));
        this.cbTipoContrato.setOnMouseClicked((event)->{
            if(this.cbTipoContrato.getValue()!=null){
                this.tipoContrato=(Tipocontrato)this.cbTipoContrato.getValue();
                this.txtMensalidade.textProperty().setValue(String.valueOf(this.tipoContrato.getValor()));
            }
        });
        this.cbTipoContrato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.tipoContrato=(Tipocontrato) newValue;
            this.txtMensalidade.textProperty().setValue(String.valueOf(this.tipoContrato.getValor()));
        });
    }    

      
    private void initUtente(){
        this.txtCodPostal.textProperty().setValue(this.utente.getCodpostal());
        this.txtEmail.textProperty().setValue(this.utente.getEmail());
        this.txtLocalidade.textProperty().setValue(this.utente.getLocalidade());
        this.txtMorada.textProperty().setValue(this.utente.getLocalidade());
        this.txtNIF.textProperty().setValue(String.valueOf(this.utente.getNif()));
        this.txtNome.textProperty().setValue(this.utente.getNome());
        this.txtTelefone.textProperty().setValue(this.utente.getContacto());
        this.dpDateNascimento.setValue(LocalDate.parse(this.utente.getDatanascimento().toString()));
    }
    
    @FXML 
    private void OpenWindowPagamento(ActionEvent event) {
        Parent root;
        try {
           //if(this.selectedContrato!=null ){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLRecessionistaPagamentos.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("New Window");
                stage.setScene(scene);
                FXMLRecessionistaPagamentosController controller= fxmlLoader.getController();
                //controller.setUtente(this.selectedUtente);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.btPagamento.getScene().getWindow());
                stage.showAndWait();
                this.initialize(null, null);
           //}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
}
