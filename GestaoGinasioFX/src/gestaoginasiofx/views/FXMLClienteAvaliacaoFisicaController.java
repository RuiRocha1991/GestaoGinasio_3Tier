/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Avaliacaofisica;
import gestaoginasiohibernate.model.Contrato;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;



/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLClienteAvaliacaoFisicaController implements Initializable {

    @FXML private Button btCancelar;
    @FXML private Button btNovaAvaliacao;
    @FXML private TextArea txtObservacoes;
    
    @FXML private TableView<Avaliacaofisica> tblAvaliacaoFisica;
    @FXML private TableColumn<Avaliacaofisica, Date> colDate;
    @FXML private TableColumn<Avaliacaofisica, String> colPersonalTrainer;
    @FXML private TableColumn<Avaliacaofisica, String> colPeso;
    @FXML private TableColumn<Avaliacaofisica, Integer> colAltura;
    @FXML private TableColumn<Avaliacaofisica, String> colMG;
    @FXML private TableColumn<Avaliacaofisica, String> colDCT;
    @FXML private TableColumn<Avaliacaofisica, String> colDCS;
    @FXML private TableColumn<Avaliacaofisica, String> colDCB;
    @FXML private TableColumn<Avaliacaofisica, String> colDCAM;
    @FXML private TableColumn<Avaliacaofisica, String> colDCSI;
    @FXML private TableColumn<Avaliacaofisica, String> colDCTo;
    @FXML private TableColumn<Avaliacaofisica, String> colDCC;
    @FXML private TableColumn<Avaliacaofisica, String> colDCA;
    @FXML private TableColumn<Avaliacaofisica, String> colDCPM;
    
    private ObservableList<Avaliacaofisica> avaliacaoObservableList;
    private Contrato contrato;
    
    private Avaliacaofisica selectedAvaliacao;

   
    
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
        this.initializaTable();
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
    private void novaAvaliacaoFisica(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteAvaliacaoFisicaNova.fxml"));
            Parent parent =loader.load();
            FXMLClienteAvaliacaoFisicaNovaController controller= loader.getController();
            controller.setContrato(this.contrato);
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    private void initializaTable() {
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        this.colPersonalTrainer.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avaliacaofisica, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Avaliacaofisica, String> param) {
                Avaliacaofisica avaliacao= param.getValue();
                SimpleStringProperty mensalidade=null;
                ObservableValue<String>ov=null;
                ov=new SimpleStringProperty(avaliacao.getPersonaltrainer().getProfessor().getColaborador().getNome());
                return ov;
            }
        });
        this.colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        this.colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        this.colMG.setCellValueFactory(new PropertyValueFactory<>("massagorda"));
        this.colDCT.setCellValueFactory(new PropertyValueFactory<>("dct"));
        this.colDCS.setCellValueFactory(new PropertyValueFactory<>("dcs"));
        this.colDCB.setCellValueFactory(new PropertyValueFactory<>("dcb"));
        this.colDCAM.setCellValueFactory(new PropertyValueFactory<>("dcam"));
        this.colDCSI.setCellValueFactory(new PropertyValueFactory<>("dcsi"));
        this.colDCTo.setCellValueFactory(new PropertyValueFactory<>("dcto"));
        this.colDCC.setCellValueFactory(new PropertyValueFactory<>("dcc"));
        this.colDCA.setCellValueFactory(new PropertyValueFactory<>("dca"));
        this.colDCPM.setCellValueFactory(new PropertyValueFactory<>("dcpm"));
        Set<Avaliacaofisica> listAvaliacao=(Set<Avaliacaofisica>)this.contrato.getAvaliacaofisicas();
        this.avaliacaoObservableList=FXCollections.observableArrayList(listAvaliacao);
        this.tblAvaliacaoFisica.setItems(avaliacaoObservableList);
        this.tblAvaliacaoFisica.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedAvaliacao=newValue;
            showDescricaoAvaliacao(newValue);
        });
    }
     private void showDescricaoAvaliacao(Avaliacaofisica avaliacao){
           this.txtObservacoes.textProperty().setValue(avaliacao.getDescricao());
       }

    
}
