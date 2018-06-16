/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Contrato;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projetogestaoginasio.ShowMessage;
import gestaoginasiobll.ValidarStrings;
import gestaoginasiobll.services.UtenteService;
import gestaoginasiobll.exception.CodPostInvalidException;
import gestaoginasiobll.exception.EmailInvalidException;
import gestaoginasiobll.exception.FieldsEmptyException;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.exception.PassInvalidaException;
import gestaoginasiobll.exception.PasswordInvalidException;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLClienteEditarDadosController implements Initializable {
    @FXML private TextField txtNif;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtMorada;
    @FXML private TextField txtLocalidade;
    @FXML private TextField txtCodPostal;
    @FXML private TextField txtTipoContrato;
    @FXML private TextField txtMensalidade;
    @FXML private TextField txtNomeCabecalho;
    @FXML private PasswordField txtNovaSenha;
    @FXML private PasswordField txtConfirmaSenha;
    @FXML private DatePicker dtData;
    @FXML private Button btGravar;
    @FXML private Button btCancelar;
    
    private Contrato contrato;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
        this.showDataUtente();
    }
  
    @FXML 
    private void comeBackInicial(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteInicial.fxml"));
            Parent parent =loader.load();
            FXMLClienteInicialController controller= loader.getController();
            controller.setUtente(this.contrato.getUtente());
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    private void showDataUtente(){
        this.txtNomeCabecalho.textProperty().setValue(this.contrato.getUtente().getNome());
        this.txtCodPostal.textProperty().setValue(this.contrato.getUtente().getCodpostal());
        this.txtEmail.textProperty().setValue(this.contrato.getUtente().getEmail());
        this.txtLocalidade.textProperty().setValue(this.contrato.getUtente().getLocalidade());
        this.txtMensalidade.textProperty().setValue(this.contrato.getValormensalidade().toString());
        this.txtMorada.textProperty().setValue(this.contrato.getUtente().getMorada());
        this.txtNif.textProperty().setValue(String.valueOf(this.contrato.getUtente().getNif()));
        this.txtNome.textProperty().setValue(this.contrato.getUtente().getNome());
        this.txtTelefone.textProperty().setValue(this.contrato.getUtente().getContacto());
        this.txtTipoContrato.textProperty().setValue(this.contrato.getTipocontrato().getDescricao());
              
        LocalDate dat=LocalDate.parse(this.contrato.getUtente().getDatanascimento().toString());
        this.dtData.setValue(dat);
    }
    
    
    
    @FXML
    private void updateDataUtente(ActionEvent event){
        
        if(ShowMessage.showConfirmation("Atualizar dados", "Pretende atualizar os seus dados?")){
            try{
                this.validStrings();
                if(!this.txtCodPostal.getText().equals("")&&!this.txtEmail.getText().equals("")
                    &&!this.txtLocalidade.getText().equals("")&&!this.txtMorada.getText().equals("")
                    &&!this.txtTelefone.getText().equals("")){
                    if(!this.txtNovaSenha.getText().equals("")|| !this.txtConfirmaSenha.getText().equals("")){
                        if(this.txtNovaSenha.getText().equals(this.txtConfirmaSenha.getText())){
                            UtenteService.updateDataAll(this.contrato.getUtente(),this.txtCodPostal.getText(),this.txtMorada.getText(), this.txtLocalidade.getText(), this.txtEmail.getText(), this.txtTelefone.getText(), Date.valueOf(this.dtData.getValue()), this.txtNovaSenha.getText());
                        }else{
                            throw new PasswordInvalidException();
                        }
                    }else{
                        UtenteService.updateData(this.contrato.getUtente(),this.txtCodPostal.getText(),this.txtMorada.getText(), this.txtLocalidade.getText(), this.txtEmail.getText(), this.txtTelefone.getText(), Date.valueOf(this.dtData.getValue()));
                    }
                }else{
                    this.checkFieldsEmpty();
                    throw new FieldsEmptyException();
                }
                this.comeBackInicial(event);
            }catch(FieldsEmptyException e){
                ShowMessage.showError("Erro campos vazio", "Contem campos vazios");
            }catch(PasswordInvalidException e){
                ShowMessage.showError("Erro na senha", "Campos Nova Senha e Confirmar Senha invalidos");
            }catch(PassInvalidaException e){
                ShowMessage.showError("Erro na senha", "Senha não compre com os requisitos [A-Z,0-9,-_,a-z]");
            }catch(EmailInvalidException e){
                ShowMessage.showError("Erro no Email", "Email incorreto, verifique novamente");
            }catch(NumericException e){
                ShowMessage.showError("Erro no Telemovel", "Telemovel so pode conter digitos");
            }catch(CodPostInvalidException e){
                ShowMessage.showError("Erro no Codigo Postal", "Código Postal inválido: 0000-000");
            }
            
        }else{
            return;
        }
    }
    
    private void validStrings() throws EmailInvalidException, CodPostInvalidException, PassInvalidaException, NumericException{
        ValidarStrings va= new ValidarStrings();
        if(!va.validarCodPostal(this.txtCodPostal.getText())){
            this.txtCodPostal.setStyle("-fx-background-color: #ff5252;");
            throw new CodPostInvalidException();
        }
        if(!va.validarNifTEL(this.txtTelefone.getText())){
            this.txtTelefone.setStyle("-fx-background-color: #ff5252;");
            throw new NumericException();
        }
        if(!va.validateEmail(this.txtEmail.getText())){
            this.txtEmail.setStyle("-fx-background-color: #ff5252;");
            throw new EmailInvalidException();
        }
        if(!this.txtNovaSenha.getText().equals("")){
            if(!va.validarSenha(this.txtNovaSenha.getText())){
                this.txtNovaSenha.setStyle("-fx-background-color: #ff5252;");
                this.txtConfirmaSenha.setStyle("-fx-background-color: #ff5252;");
                throw new PassInvalidaException();
            }
        }
    }
    
    private void checkFieldsEmpty(){
        if(this.txtCodPostal.getText().equals(""))
            this.txtCodPostal.setStyle("-fx-background-color: #ff5252;");
        if(this.txtEmail.getText().equals(""))
            this.txtEmail.setStyle("-fx-background-color: #ff5252;");
        if(this.txtLocalidade.getText().equals(""))
            this.txtLocalidade.setStyle("-fx-background-color: #ff5252;");
        if(this.txtMorada.getText().equals(""))
            this.txtMorada.setStyle("-fx-background-color: #ff5252;");
        if(this.txtTelefone.getText().equals(""))
            this.txtTelefone.setStyle("-fx-background-color: #ff5252;");
            
    }
}
