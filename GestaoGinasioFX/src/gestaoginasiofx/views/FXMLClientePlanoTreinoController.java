/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import gestaoginasiofx.FillListView;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Linhaplanotreino;
import gestaoginasiohibernate.model.Planotreino;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLClientePlanoTreinoController implements Initializable {
    private Contrato contrato;
    @FXML private ListView lvDiasSemana;
    @FXML private ListView lvPlanoTreino;
    @FXML private TextArea txtDescriçaoPlano;
    @FXML private TextArea txtDescriçaoDia;
    @FXML private TextField txtPersonalTrainer;
    @FXML private TextField txtData;
    @FXML private Button btNovoPlano;
    @FXML private Button btCancelar;
    
    private ObservableList<Planotreino> planoObservable; 
    private ObservableList<Linhaplanotreino> linhaPlanoObservable; 
    private Planotreino planoSelected;
    private Linhaplanotreino linhaPlanoSelected;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setContrato(Contrato contrato){
        this.contrato=contrato;
        this.preencheLista();
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
    private void openNovoPlanoTreino(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClientePlanoTreinoNovo.fxml"));
            Parent parent =loader.load();
            FXMLClientePlanoTreinoNovoController controller= loader.getController();
            controller.setContrato(this.contrato);
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("erro sair panel plano treino");
        }
    }
    
    private void preencheLista(){
         List<Planotreino> listPlanoTreino= (List<Planotreino>) this.contrato.getPlanotreinos().stream().collect(Collectors.toList());
        this.planoObservable=FXCollections.observableList(listPlanoTreino);
        this.fillFields();
    }
    
    private void fillFields(){
       this.lvPlanoTreino.setItems(this.planoObservable);
       this.lvPlanoTreino.setCellFactory(FillListView.fillPlanoTreinoListView(this.lvPlanoTreino, this.planoObservable));
       this.lvPlanoTreino.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.planoSelected=(Planotreino) newValue;
            this.showPlano();
        });
    }
    
    private void showPlano(){
        this.txtDescriçaoPlano.textProperty().setValue(this.planoSelected.getDescricao());
        LocalDate dat=LocalDate.parse(this.planoSelected.getData().toString());
        this.txtData.textProperty().setValue(dat.toString());
        this.txtPersonalTrainer.textProperty().setValue(this.planoSelected.getPersonaltrainer().getProfessor().getColaborador().getNome());
        List<Linhaplanotreino> listLinhaPlanoTreino= (List<Linhaplanotreino>) this.planoSelected.getLinhaplanotreinos().stream().collect(Collectors.toList());
        this.linhaPlanoObservable=FXCollections.observableList(listLinhaPlanoTreino);
        this.lvDiasSemana.setItems(this.linhaPlanoObservable);
        this.lvDiasSemana.setCellFactory(FillListView.fillLinhaPlanoTreinoListView(this.lvDiasSemana, this.linhaPlanoObservable));
        this.lvDiasSemana.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.linhaPlanoSelected=(Linhaplanotreino) newValue;
            this.showLinhaPlano();
        });
    }
    
    private void showLinhaPlano(){
        this.txtDescriçaoDia.textProperty().setValue(this.linhaPlanoSelected.getDescricao());
    }
    
    
}
