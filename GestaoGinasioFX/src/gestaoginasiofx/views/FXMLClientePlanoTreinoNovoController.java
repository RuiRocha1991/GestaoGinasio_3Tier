/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import gestaoginasiofx.FillListView;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Personaltrainer;
import gestaoginasiohibernate.model.Planotreino;
import projetogestaoginasio.ShowMessage;
import services.PlanoTreinoService;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLClientePlanoTreinoNovoController implements Initializable {
    @FXML private ListView ltvPersonalTrainer;
    @FXML private TextArea txtDescricao;
    @FXML private Button btNovo;
    @FXML private Button btCancelar;
    
    private Contrato contrato;
    private Personaltrainer personalTrainer;
    
    
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
     public void setContrato(Contrato contrato) {
        this.contrato = contrato;
        this.fillList();
    }
    
    public void fillList(){
        this.ltvPersonalTrainer.setCellFactory(FillListView.fillPersonalTrainerListView(this.ltvPersonalTrainer));
        this.ltvPersonalTrainer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.personalTrainer=(Personaltrainer) newValue;
        });
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
    
    @FXML
    private void addNewPlanoTreino(ActionEvent event){
        if(ShowMessage.showConfirmation("Confirmação para inserir", "Tem a certeza que pretende pedir novo plano treino?")){
            if(this.personalTrainer!=null && this.txtDescricao.getText().length()>0){
                Planotreino newpt= new Planotreino();
                newpt.setContrato(this.contrato);
                newpt.setData(Date.valueOf(LocalDate.now().toString()));
                newpt.setPersonaltrainer(this.personalTrainer);
                newpt.setDescricao(this.txtDescricao.getText());
                this.personalTrainer.getPlanotreinos().add(newpt);
                this.contrato.getPlanotreinos().add(newpt);
                PlanoTreinoService.createPlanoTreino(newpt);
                this.cleanFills();
                this.comeBackInicial(event);
            }else{
                ShowMessage.showError("Existem campos em Branco", "O campo descrição está vazio, ou não selecionou o Personal Trainer");
               if(this.personalTrainer==null){
                   this.ltvPersonalTrainer.setStyle("-fx-background-color: #f44336;");
               }
               if(this.txtDescricao.getText().equals("")){
                   this.txtDescricao.setStyle("-fx-background-color: #f44336;");
               }
            }
        }else{
             return;
        }
    }
    
    private void cleanFills(){
        this.ltvPersonalTrainer.getSelectionModel().clearSelection();
        this.txtDescricao.setText("");
    }
    
}
