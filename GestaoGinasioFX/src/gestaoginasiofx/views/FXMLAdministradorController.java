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
public class FXMLAdministradorController implements Initializable {
    @FXML private Button btCloseSession;
    @FXML private Button btUtente;
    @FXML private Button btTipoContrato;
    @FXML private Button btEquipamento;
    @FXML private Button btAula;
    @FXML private Button btEspaco;
    @FXML private Button btFuncionario;
    @FXML private Button btDadosPessoais;
    @FXML private TextField txtNome;
    @FXML private TextField txtMenu;
    private static Colaborador COLABORADOR;
    private Colaborador colaborador;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }      
    
   public static Colaborador getCt(){
        return FXMLAdministradorController.COLABORADOR;
    }
    
    public void setColaborador(Colaborador colaborador){
        this.colaborador= colaborador;
        this.COLABORADOR=this.colaborador;
        this.txtNome.setText("Administrador: "+this.colaborador.getNome());
        this.txtMenu.setText("Menu: Utentes");
    }

    public Colaborador getColaborador(){
        return this.colaborador;
    }
    
    
    @FXML 
    private void closeSession(ActionEvent event){
        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        this.colaborador=null;
        FXMLAdministradorController.COLABORADOR=null;
        try{
            Parent parent =loader.load();
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel");
        }
    }
    
    
   @FXML
    private void switchCenter(ActionEvent event) throws IOException{
      
        switch(((Button)event.getSource()).getId()){
            case "btUtente":
                switchCenterPane("FXMLAdministradorUtente.fxml");
                this.txtMenu.setText("Menu: Utentes");
                break;
            case "btTipoContrato":
                switchCenterPane("FXMLAdministradorTipoContrato.fxml");
                this.txtMenu.setText("Menu: Tipos de Contrato");
                break;
            case "btEquipamento":
                switchCenterPane("FXMLAdministradorEquipamento.fxml");
                this.txtMenu.setText("Menu: Equipamentos");
                break;
            case "btAula":
                switchCenterPane("FXMLAdministradorAulasGrupo.fxml");
                this.txtMenu.setText("Menu: Aulas");
                break;
            case "btEspaco":
                switchCenterPane("FXMLAdministradorEspaco.fxml");
                this.txtMenu.setText("Menu: Espaços");
                break;
            case "btFuncionario":
                switchCenterPane("FXMLAdministradorFuncionarios.fxml");
                this.txtMenu.setText("Menu: Funcionários");
                break;
            case "btDadosPessoais":
                switchCenterPane("FXMLAdministradorDadosPessoais.fxml");
                this.txtMenu.setText("Menu: Dados Pessoais");
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
    
    
    
    
   
}
