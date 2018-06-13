/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import enumerados.EnumTipoFuncionario;
import gestaoginasiohibernate.model.Colaborador;
import gestaoginasiohibernate.model.Utente;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import projetogestaoginasio.ShowMessage;
import services.LoginService;
import services.exception.PagamentoEmAtrasoException;
import services.exception.UtilizadorInvalidoException;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLIniciarSessaoController implements Initializable {
    @FXML private Button btIniciarSessao;
    @FXML private Button btCancelar;
    @FXML private TextField txtUtilizador;
    @FXML private TextField txtSenha;
    
    private static BorderPane ROOT;
    public static BorderPane getRoot(){
        return FXMLIniciarSessaoController.ROOT;
    };
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.txtSenha.textProperty().setValue("12");
        this.txtUtilizador.textProperty().setValue("Admin");
    }    
    
//    private Object verificaInicioSessao(){
//        Object object=null;
//        List<Utente> utentes = HibernateGenericLib.executeHQLQueryLogin("from Utente where Email=? and Senha ?",this.txtUtilizador.getText(),this.txtSenha.getText());
//        if(utentes.size()>0){
//            object= utentes.get(0);
//        }
//        
//        return object;
//    }
    
    @FXML
    private void iniciarSessao(ActionEvent event) {
        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        String user= this.txtUtilizador.getText();
        String senha = this.txtSenha.getText();
        try{
            Utente utente= LoginService.getUtenteLogin(user, senha);
            if(utente!=null){
                this.iniciarSessaoUtente( stage,utente);
            }else{
               Colaborador colaborador=LoginService.getColaboradorLogin(user, senha);
               if(colaborador!=null){
                   this.iniciarSessaoColaborador(stage, colaborador);
               }
        }
        }catch(PagamentoEmAtrasoException e){
            ShowMessage.showError("Pagamentos em atraso", "Contem pagamentos em atraso");
        }catch(UtilizadorInvalidoException e){
             ShowMessage.showError("Utilizador não exite ou dados estão incorretos", "Erro de Inicio de Sessão");
        }
        
    }
    
    
    
    private void iniciarSessaoUtente(Stage stage, Utente utente){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteInicial.fxml"));
            Parent parent =loader.load();
            FXMLClienteInicialController controller= loader.getController();
            controller.setUtente(utente);
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    private void iniciarSessaoColaborador(Stage stage, Colaborador colaborador){
        try{
           if(colaborador.getTipofuncionario().equals("ADMINISTRADOR")){
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLAdministrador.fxml"));
                    Pane centerPane=FXMLLoader.load(getClass().getResource("FXMLAdministradorUtente.fxml"));
                    Parent parent =loader.load();
                    FXMLAdministradorController controllerAdmin=loader.getController();
                    controllerAdmin.setColaborador(colaborador);
                    ((BorderPane)parent).setCenter(centerPane);
                    FXMLIniciarSessaoController.ROOT=(BorderPane)parent;
                    Scene scene= new Scene(parent);
                    stage.setScene(scene);
                    stage.show();
           }
           if(colaborador.getTipofuncionario().equals(EnumTipoFuncionario.INSTRUTOR)){
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLAdministrador.fxml"));
                    Pane centerPane=FXMLLoader.load(getClass().getResource("FXMLAdministradorUtente.fxml"));
                    Parent parent =loader.load();
                    FXMLAdministradorController controllerAdmin=loader.getController();
                    controllerAdmin.setColaborador(colaborador);
                    ((BorderPane)parent).setCenter(centerPane);
                    FXMLIniciarSessaoController.ROOT=(BorderPane)parent;
                    Scene scene= new Scene(parent);
                    stage.setScene(scene);
                    stage.show();
           }
           if(colaborador.getTipofuncionario().equals(EnumTipoFuncionario.PERSONALTRAINER)){
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLAdministrador.fxml"));
                    Pane centerPane=FXMLLoader.load(getClass().getResource("FXMLAdministradorUtente.fxml"));
                    Parent parent =loader.load();
                    FXMLAdministradorController controllerAdmin=loader.getController();
                    controllerAdmin.setColaborador(colaborador);
                    ((BorderPane)parent).setCenter(centerPane);
                    FXMLIniciarSessaoController.ROOT=(BorderPane)parent;
                    Scene scene= new Scene(parent);
                    stage.setScene(scene);
                    stage.show();
           }
           if(colaborador.getTipofuncionario().equals(EnumTipoFuncionario.PROFESSOR)){
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLAdministrador.fxml"));
                    Pane centerPane=FXMLLoader.load(getClass().getResource("FXMLAdministradorUtente.fxml"));
                    Parent parent =loader.load();
                    FXMLAdministradorController controllerAdmin=loader.getController();
                    controllerAdmin.setColaborador(colaborador);
                    ((BorderPane)parent).setCenter(centerPane);
                    FXMLIniciarSessaoController.ROOT=(BorderPane)parent;
                    Scene scene= new Scene(parent);
                    stage.setScene(scene);
                    stage.show();
           }
           if(colaborador.getTipofuncionario().equals("RECECIONISTA")){
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLRecessionista.fxml"));
                    Pane centerPane=FXMLLoader.load(getClass().getResource("FXMLAdministradorUtente.fxml"));
                    Parent parent =loader.load();
                    //FXMLRecessionistaController controller=loader.getController();
                    //controller.setColaborador(colaborador);
                    ((BorderPane)parent).setCenter(centerPane);
                    FXMLIniciarSessaoController.ROOT=(BorderPane)parent;
                    Scene scene= new Scene(parent);
                    stage.setScene(scene);
                    stage.show();
           }
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
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
