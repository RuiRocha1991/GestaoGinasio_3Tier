/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import gestaoginasiofx.FillComboBox;
import gestaoginasiohibernate.model.Categoriaequipamento;
import gestaoginasiohibernate.model.Equipamento;
import gestaoginasiohibernate.model.Espaco;
import projetogestaoginasio.ShowMessage;
import services.EquipamentoService;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLGerirEquipamentoController implements Initializable {

    private Equipamento equipamento;
    private ObservableList<Equipamento> equipamentoObservableList;
    private Espaco espacoSelected;
    private Categoriaequipamento categoriaSelected;
    private List<Equipamento> listaEquip; 
    
    @FXML private TextField txtDescricao;
    @FXML private ComboBox cbCategoria;
    @FXML private ComboBox cbEspaco;
    @FXML private CheckBox ckAtivo;
    
    @FXML private Button btGravar;
    @FXML private Button btCancelar;
    
    @FXML private TableView<Equipamento> tbEquipamentos;
    @FXML private TableColumn<Equipamento, String> colDescricao;
    @FXML private TableColumn<Equipamento, String> colCategoria;
    @FXML private TableColumn<Equipamento, Boolean> colAtivo;
    @FXML private TableColumn<Equipamento, String> colEspaco;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.listaEquip=new ArrayList<>();
        this.cbEspaco.setConverter(FillComboBox.FillEspacoComboBox(this.cbEspaco));
        this.cbEspaco.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.espacoSelected= (Espaco) newValue;
        });
        this.cbCategoria.setConverter(FillComboBox.FillCategoriasEquipamentosComboBox(this.cbCategoria));
        this.cbCategoria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.categoriaSelected= (Categoriaequipamento) newValue;
        });
        this.initializeTable();
        
    }    
    
    public void setEquipamento(Equipamento equipamento){
        this.equipamento=equipamento;
    }
    
    private void initializeTable(){
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colCategoria.setCellValueFactory((param)->{
            Equipamento equip= param.getValue();
            SimpleStringProperty equipamento=null;
            ObservableValue<String>ov=null;
            equipamento= new SimpleStringProperty(equip.getCategoriaequipamento().getDesignacao());
            ov = equipamento;
            return ov;
        });
        this.colEspaco.setCellValueFactory((param)->{
            Equipamento equip= param.getValue();
            SimpleStringProperty equipamento=null;
            ObservableValue<String>ov=null;
            equipamento= new SimpleStringProperty(equip.getEspaco().getDescricao());
            ov = equipamento;
            return ov;
        });
        this.colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
    }
    
    private void setTable(){
        this.tbEquipamentos.setItems(this.equipamentoObservableList);
        
    }
    
    @FXML
    private void atualizarLista(){
        if(this.equipamento==null){
           if(ShowMessage.showConfirmation("Inserir Novo Equipamento", "Tem a certeza que pretende inserir novo Equipamento?")){
               if(this.categoriaSelected!=null && this.espacoSelected!=null && this.txtDescricao.getText().length()>0){
                    Equipamento equip=EquipamentoService.createEquipamento(this.espacoSelected, this.categoriaSelected, this.txtDescricao.getText(), (this.ckAtivo.isSelected()?'1':'0'));
                    this.listaEquip.add(equip);
                    this.equipamentoObservableList=FXCollections.observableArrayList(this.listaEquip);
                    this.setTable();
                    this.clearFields();
               }else{
                    this.verifyFillEmpty();
                    ShowMessage.showError("Erro a inserir", "Contem Campos vazios");
               }
           }else{
               return;
           }
        }else{
                EquipamentoService.updateEquipamento(this.equipamento, this.espacoSelected, categoriaSelected, this.txtDescricao.getText(), (this.ckAtivo.isSelected()?'1':'0'));
            Stage stage = (Stage) this.btGravar.getScene().getWindow();
            // do what you have to do
            stage.close();
        }
    }
    
    @FXML 
    private void closeWindow(){
        Stage stage = (Stage) this.btCancelar.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
    private void verifyFillEmpty(){
        if(this.categoriaSelected ==null){
            this.cbCategoria.setStyle("-fx-background-color: #ff5252;");
        }
        if(this.espacoSelected ==null){
            this.cbEspaco.setStyle("-fx-background-color: #ff5252;");
        }
        if(this.txtDescricao.getText().length()<=0){
            this.txtDescricao.setStyle("-fx-background-color: #ff5252;");
        }
    }
    
    @FXML
    private void clearBackground(){
        this.cbCategoria.setStyle("-fx-background-color: #fffff;");
        this.cbEspaco.setStyle("-fx-background-color: #fffff;");
        this.txtDescricao.setStyle("-fx-background-color: #fffff;");
    } 
    
    private void clearFields(){
        this.txtDescricao.setText("");
        this.cbCategoria.getSelectionModel().clearSelection();
        this.cbEspaco.getSelectionModel().clearSelection();
        this.ckAtivo.setSelected(false);
        
    }
    
}
