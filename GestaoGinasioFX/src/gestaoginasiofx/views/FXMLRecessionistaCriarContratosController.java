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


/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXMLRecessionistaCriarContratosController implements Initializable {
    private Tipocontrato tipoContrato;
    private Utente utente;
    
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
    
    
    
    
    
    
}
