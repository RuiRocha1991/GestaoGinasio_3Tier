/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.services.ContratoService;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import gestaoginasiofx.FillComboBox;
import gestaoginasiofx.Notificacao;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Tipocontrato;
import gestaoginasiohibernate.model.Utente;
import java.io.IOException;
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
public class FXMLRecessionistaCriarContratosController implements Initializable {
    private Tipocontrato tipoContrato;
    private Utente utente;
    private Contrato contrato;
    
    @FXML private Button btPagamento;
    @FXML private ComboBox cbTipoContrato;
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
                Tipocontrato tp =(Tipocontrato)this.cbTipoContrato.getValue();
                this.txtMensalidade.textProperty().setValue(String.valueOf(tp.getValor()));
            }
        });
        this.cbTipoContrato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.tipoContrato=(Tipocontrato) newValue;
            if(this.tipoContrato!=null)
                this.txtMensalidade.textProperty().setValue(String.valueOf(this.tipoContrato.getValor()));
        });
    }    

    public void setUtente(Utente utente) {
        this.utente = utente;
        this.initUtente();
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
            if(this.tipoContrato!=null){
               this.contrato= ContratoService.createContrato(this.utente, this.tipoContrato);
               Notificacao.successNotification("Criar Contrato", "Criado com sucesso");
            }
           if(this.contrato!=null ){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLRecessionistaPagamentos.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Receber Pagamento");
                stage.setScene(scene);
                FXMLRecessionistaPagamentosController controller= fxmlLoader.getController();
                controller.setContrato(this.contrato);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.btPagamento.getScene().getWindow());
                stage.showAndWait();
                this.initialize(null, null);
           }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
}
