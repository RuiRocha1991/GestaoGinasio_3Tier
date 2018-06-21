/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.exception.CodPostInvalidException;
import gestaoginasiobll.exception.EmailInvalidException;
import gestaoginasiobll.exception.EmailRepetidoException;
import gestaoginasiobll.exception.NIFRepetidoException;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.exception.PassInvalidaException;
import gestaoginasiobll.exception.PasswordInvalidException;
import gestaoginasiobll.services.UtenteService;
import java.net.URL;
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
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projetogestaoginasio.ShowMessage;


/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXMLRecessionistaCriarUtenteController implements Initializable {
    private Tipocontrato tipoContrato;
    private Contrato contrato; 
    
    @FXML private Button btCancelar;
    @FXML private Button btCriarUtente;
    @FXML private Button btConcluir;
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
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtSenhaValida;
    @FXML private DatePicker dpDateNascimento;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.btPagamento.setDisable(true);
        this.btConcluir.setDisable(true);
        this.cbTipoContrato.setConverter(FillComboBox.fillTipoContratoComboBox(this.cbTipoContrato));
        this.cbTipoContrato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.tipoContrato=(Tipocontrato) newValue;
            if(this.tipoContrato!=null)
                this.txtMensalidade.textProperty().setValue(String.valueOf(this.tipoContrato.getValor()));
        });
        
        this.txtCodPostal.setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.BACK_SPACE) {
                if(this.txtCodPostal.getText().length()==4){
                    this.txtCodPostal.setText(this.txtCodPostal.getText()+"-");
                    this.txtCodPostal.positionCaret(5);
                }
            }
        });
    }    
    
    @FXML
    private void createUtente(){
        if(this.txtCodPostal.getText().length()>0&& this.txtEmail.getText().length()>0&&this.txtLocalidade.getText().length()>0
                && this.txtMensalidade.getText().length()>0&& this.txtMorada.getText().length()>0&&
                this.txtNIF.getText().length()>0&& this.txtNome.getText().length()>0&& this.txtTelefone.getText().length()>0
                && this.txtSenha.getText().length()>0){
            if(ShowMessage.showConfirmation("Criar novo Utente", "Tem a certeza que pretende criar novo Utente?")){
                try {
                    UtenteService.validarNif(this.txtNIF.getText());
                    UtenteService.validarTelefone(this.txtTelefone.getText());
                    UtenteService.validarEmail(this.txtEmail.getText());
                    UtenteService.validarCodPostal(this.txtCodPostal.getText());
                    UtenteService.validarSenha(this.txtSenha.getText(), this.txtSenhaValida.getText());
                    this.contrato= UtenteService.createUtente(this.txtNome.getText(), this.txtNIF.getText()
                            , this.txtCodPostal.getText(), this.txtMorada.getText(), this.txtLocalidade.getText()
                            , this.txtEmail.getText(), this.txtTelefone.getText(), this.dpDateNascimento.getValue(), this.tipoContrato, this.txtSenha.getText());
                    Notificacao.successNotification("Criar Utente", "Criado Utente com Sucesso");
                    this.btCriarUtente.setDisable(true);
                    this.btPagamento.setDisable(false);
                } catch (NumericException ex) {
                    ShowMessage.showError("NIF inválido ou Telefone", "O Numero Identificação Fiscal ou Telefone não estão corretos.");
                } catch (NIFRepetidoException ex) {
                    ShowMessage.showError("NIF inválido", "O Numero Identificação Fiscal já se encontra Registado.");
                } catch (EmailInvalidException ex) {
                    ShowMessage.showError("Email inválido", "O Email não está correto.");
                } catch (CodPostInvalidException ex) {
                    ShowMessage.showError("Código Postal inválido", "O Código Postal não está correto.");
                } catch (PassInvalidaException ex) {
                    ShowMessage.showError("Senha Inválida", "A senha não é segura(A-Z,a-z,0-9,@#$%^&+=-_)");
                } catch (PasswordInvalidException ex) {
                    ShowMessage.showError("Erro campo Senha", "Campo Senha ou Confirmar Senha inválidos");
                } catch (EmailRepetidoException ex) {
                    ShowMessage.showError("Erro no email", "Email repetido");
                }
            }else{
                ShowMessage.showError("Campos Vazios", "Existem campos vazios, para concluir preencha todos os campos");
            }
        }
    }
    
    
    @FXML 
    private void OpenWindowPagamento(ActionEvent event) {
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
                stage.initOwner(this.btPagamento.getScene().getWindow());
                stage.showAndWait();
                this.initialize(null, null);
                this.btConcluir.setDisable(false);
                this.btPagamento.setDisable(true);
                this.btCriarUtente.setDisable(true);
                
           }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    private void closeWindow(){
        Stage stage = (Stage) this.btCancelar.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
     @FXML 
    private void ConcluirRegisto(){
        Notificacao.successNotification("Registo Utente", "Utente registado com Sucesso");
        Stage stage = (Stage) this.btConcluir.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
    
}
