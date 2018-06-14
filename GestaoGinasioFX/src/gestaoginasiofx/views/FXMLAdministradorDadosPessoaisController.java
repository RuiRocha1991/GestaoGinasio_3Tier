/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import gestaoginasiofx.FillComboBox;
import gestaoginasiohibernate.model.Colaborador;
import projetogestaoginasio.ShowMessage;
import services.ColaboradorService;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLAdministradorDadosPessoaisController implements Initializable {
    @FXML private TextField txtTipoFuncionario;
    @FXML private TextField txtNome;
    @FXML private TextField txtUtilizador;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtNovaSenha;
    @FXML private PasswordField txtSenhaConfirma;

    private Colaborador colaborador;

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       this.started();
    }    
    
    private void started() {
       if(FXMLAdministradorController.getCt()!=null){
           this.colaborador=FXMLAdministradorController.getCt();
       }else{
           if(FXMLInstrutorController.getCt()!=null){
                this.colaborador=FXMLInstrutorController.getCt();
           }
       }
       this.fillFields();
    }
    
    private void fillFields(){
        this.txtNome.textProperty().setValue(this.colaborador.getNome());  
        this.txtTipoFuncionario.textProperty().setValue(this.colaborador.getTipofuncionario());
        this.txtUtilizador.textProperty().setValue(this.colaborador.getUtilizador());
            
    }
    
    @FXML
    private void updateColaborador(){
        if(this.txtSenha.getText().equals(this.colaborador.getSenha())&& !this.txtSenha.getText().equals("")&& this.txtNovaSenha.getText().equals(this.txtSenhaConfirma.getText())){
            if(ShowMessage.showConfirmation("Confirmação de alteração", "Tem a certeza que pretende salvar?")){
                    this.colaborador.setSenha(this.txtNovaSenha.getText());
                    ColaboradorService.updateObjectColaborador(this.colaborador);
                    this.txtSenha.setText("");
                    this.txtNovaSenha.setText("");
                    this.txtSenhaConfirma.setText("");
                }else{
                    return;
                }
        }else{
            this.txtSenha.setStyle("-fx-background-color: #f44336;");
            this.txtSenhaConfirma.setStyle("-fx-background-color:#f44336;");
            this.txtNovaSenha.setStyle("-fx-background-color:#f44336;");
            ShowMessage.showError("Senha Inválida", "Os campos Senha e Confirmar Senha invalidos");
            this.txtSenhaConfirma.setText("");
            this.txtSenha.setText("");
            this.txtNovaSenha.setText("");
        }
    }
    
}
