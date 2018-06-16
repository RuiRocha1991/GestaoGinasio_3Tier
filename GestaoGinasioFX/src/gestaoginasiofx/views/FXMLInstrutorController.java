/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Colaborador;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLInstrutorController implements Initializable {
    private Colaborador colaboradorSelected;
    private static Colaborador COLABORADOR;
    
    @FXML private TextField txtNome;
    @FXML private TextField txtMenu;
    
    @FXML private Button btCloseSession;
    @FXML private Button btNotaAvaria;
    @FXML private Button btHorario;
    @FXML private Button btDadosPessoais;
    @FXML private Button btPlanosTreino;
    @FXML private Button btAvaliacaoFisica;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public static Colaborador getCt(){
        return FXMLInstrutorController.COLABORADOR;
    }
    
    public void setColaborador(Colaborador colaborador){
        this.colaboradorSelected=colaborador;
        COLABORADOR=colaborador;
        this.initializePane();
    }
    
    private void initializePane(){
        this.txtNome.textProperty().setValue(this.colaboradorSelected.getTipofuncionario()+": "+this.colaboradorSelected.getNome());
        this.txtMenu.setText("Menu: Utentes");
        if(this.colaboradorSelected.getTipofuncionario().equals("INSTRUTOR")){
            this.btHorario.setVisible(false);
            this.btPlanosTreino.setVisible(false);
            this.btAvaliacaoFisica.setVisible(false);
        }
    }
    
    @FXML
    private void switchCenter(ActionEvent event) throws IOException{
        switch(((Button)event.getSource()).getId()){
            case "btNotaAvaria":
                switchCenterPane("FXMLInstrutorNotasAvarias.fxml");
                this.txtMenu.setText("Menu: Notas Avaria");
                break;
            case "btHorario":
                switchCenterPane("FXMLInstrutorHorario.fxml");
                this.txtMenu.setText("Menu: Horário");
                break;
            case "btDadosPessoais":
                switchCenterPane("FXMLAdministradorDadosPessoais.fxml");
                this.txtMenu.setText("Menu: Dados Pessoais");
                break;    
            case "btPlanosTreino":
                switchCenterPane("FXMLPersonalTrainerPlanoTreino.fxml");
                this.txtMenu.setText("Menu: Planos Treino");
                break; 
            case "btAvaliacaoFisica":
                switchCenterPane("FXMLPersonalTrainerAvaliacaoFisica.fxml");
                this.txtMenu.setText("Menu: Avaliação Física");
                break;   
        }
    }
    
    private void switchCenterPane(String paneURL) {
        try{
            Pane newPane=FXMLLoader.load(getClass().getResource(paneURL));
            FXMLIniciarSessaoController.getRoot().setCenter(newPane);
        }catch(IOException a){
            a.printStackTrace();
        }
    }
    
    @FXML 
    private void closeSession(ActionEvent event){
        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        this.colaboradorSelected=null;
        COLABORADOR=null;
        try{
            Parent parent =loader.load();
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel");
        }
    }
    
}
