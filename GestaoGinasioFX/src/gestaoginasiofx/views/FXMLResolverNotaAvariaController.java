/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Notaavaria;
import hibernate.HibernateGenericLib;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import gestaoginasiobll.ConvertType;
import projetogestaoginasio.ShowMessage;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.services.NotaAvariaService;
import gestaoginasiofx.Notificacao;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLResolverNotaAvariaController implements Initializable {

    private Notaavaria notaSelected;
    
    @FXML private TextField txtDataAvaria;
    @FXML private TextField txtEquipamento;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtEspaco;
    @FXML private TextField txtValor;
    
    @FXML private TextArea txtObs;
    
    @FXML private DatePicker dpDate;
    
    @FXML private Button btSave;
    @FXML private Button btCancel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setNotaAvaria(Notaavaria nota){
        this.notaSelected=nota;
        this.txtCategoria.textProperty().setValue(this.notaSelected.getEquipamento().getCategoriaequipamento().getDesignacao());
        this.txtDataAvaria.textProperty().setValue(LocalDate.parse(this.notaSelected.getDatanota().toString()).toString());
        this.txtDescricao.textProperty().setValue(this.notaSelected.getDescricao());
        this.txtEquipamento.textProperty().setValue(this.notaSelected.getEquipamento().getDescricao());
        this.txtEspaco.textProperty().setValue(this.notaSelected.getEquipamento().getEspaco().getDescricao());
        this.dpDate.setValue(LocalDate.now());
    }
    
    @FXML
    private void saveChange(ActionEvent event) throws Throwable{
        if(ShowMessage.showConfirmation("Atualizar Nota Avaria", "Tem a certeza que pretende atualizar a nota de avaria?")){
            if(this.txtValor.getText().length()>0 && this.txtObs.getText().length()>0){
                try{
                    NotaAvariaService.updateNotaAvaria(this.notaSelected, this.dpDate.getValue(), this.txtValor.getText(), this.txtObs.getText());
                    Notificacao.successNotification("Nota Avaria", "Atualizado com Sucesso");
                    // get a handle to the stage
                    Stage stage = (Stage) this.btSave.getScene().getWindow();
                    // do what you have to do
                    stage.close();
                }catch(NumericException e){
                    ShowMessage.showError("Valor Inválido", "Valor introduzido não está valido, ex: 14.5");
                }
            }else{
                this.verifyFieldsEmpty();
                ShowMessage.showError("Campos vazios", "Para poder continuar tem que preencher todos os campos.");
            }
        }else{
            return;
        }
    }

    private void verifyFieldsEmpty() {
        if(this.txtObs.getText().length()<=0){
            this.txtObs.setStyle("-fx-background-color: #ff5252;");
        }
        if(this.txtValor.getText().length()<=0){
            this.txtValor.setStyle("-fx-background-color: #ff5252;");
        }
    }
    
    @FXML
    private void clearBackground(){
        this.txtValor.setStyle("-fx-background-color: #fffff;");
        this.txtObs.setStyle("-fx-background-color: #fffff;");
    }
    
    @FXML 
    private void CloseWindowAndCancel(){
        // get a handle to the stage
        Stage stage = (Stage) this.btCancel.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
