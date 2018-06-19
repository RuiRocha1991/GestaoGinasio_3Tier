/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.exception.PassInvalidaException;
import gestaoginasiobll.services.ContratoService;
import gestaoginasiofx.FillComboBox;
import gestaoginasiofx.Notificacao;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Tipocontrato;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projetogestaoginasio.ShowMessage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXMLRecessionistaEditClientesController implements Initializable {

    private Contrato contrato;
    private Tipocontrato tipoContrato;
    
    @FXML private Button btGravar;
    @FXML private Button btLimparSelecao;
    
    @FXML private TextField txtNome;
    @FXML private TextField txtNIF;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtMensalidadeAtual;
    @FXML private TextField txtMensalidade;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtNovaSenha;
    
    @FXML private ComboBox cbTipoContrato;
    @FXML private DatePicker dpDate;
    @FXML private CheckBox ckAtivo;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       this.cbTipoContrato.setConverter(FillComboBox.fillTipoContratoComboBox(this.cbTipoContrato));
        this.cbTipoContrato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.tipoContrato=(Tipocontrato) newValue;
            if(this.tipoContrato!=null)
                this.txtMensalidade.textProperty().setValue(String.valueOf(this.tipoContrato.getValor()));
        });
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
        this.showContrato();
    }
    
    private void showContrato(){
        this.txtEmail.textProperty().setValue(this.contrato.getUtente().getEmail());
        this.txtMensalidadeAtual.textProperty().setValue(String.valueOf(this.contrato.getValormensalidade()));
        this.txtNIF.textProperty().setValue(String.valueOf(this.contrato.getUtente().getNif()));
        this.txtNome.textProperty().setValue(this.contrato.getUtente().getNome());
        this.txtTelefone.textProperty().setValue(this.contrato.getUtente().getContacto());
        this.dpDate.setValue(LocalDate.parse(this.contrato.getUtente().getDatanascimento().toString()));
        if(this.contrato.getAtivo()=='1'){
            this.ckAtivo.setSelected(true);
        }else{
            this.ckAtivo.setSelected(false);
        }
    }
    
    @FXML 
    private void saveChange(){
        if(this.contrato.getAtivo()=='1' && !this.ckAtivo.isSelected()){
            if(ShowMessage.showConfirmation("Desativar Contrato", "Tem a certeza que pretende desativar o contrato?")){
                ContratoService.desativarContrato(this.contrato);
                Notificacao.successNotification("Contrato", "Desativado com Sucesso.");
            }else{
                return;
            }
        }
        if(this.tipoContrato!=null){
            if(ShowMessage.showConfirmation("Alterar tipo de contrato", "Tem a certeza que pretende alterar tipo de contrato?")){
                ContratoService.alterarTipoContrato(this.contrato, this.tipoContrato);
                Notificacao.successNotification("Contrato", "Novo Contrato criado.");
                this.OpenWindowPagamento();
            }else{
                return;
            }
        }
        if(this.txtSenha.getText().equals(this.txtNovaSenha.getText()) && this.txtSenha.getText().length()>0){
            if(ShowMessage.showConfirmation("Alterar Senha", "Tem a certeza que pretende alterar Senha de acesso?")){
                try {
                    ContratoService.alterarSenhaContrato(this.contrato, this.txtSenha.getText());
                    Notificacao.successNotification("Senha", "Alterada a Senha com sucesso");
                } catch (PassInvalidaException ex) {
                     ShowMessage.showError("Senha Inválida", "A senha não é segura(A-Z,a-z,0-9,@#$%^&+=-_)");
                }
            }else{
                return;
            }
        }
    }
    
    @FXML 
    private void limparSelecao(){
        this.tipoContrato=null;
        this.txtMensalidade.setText("");
        this.cbTipoContrato.getSelectionModel().clearSelection();
    }

    
    @FXML 
    private void OpenWindowPagamento() {
        Parent root;
        try {
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
                stage.initOwner(this.btGravar.getScene().getWindow());
                stage.showAndWait();
           }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
