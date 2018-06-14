/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import gestaoginasiofx.FillListView;
import gestaoginasiohibernate.model.Categoriaequipamento;
import gestaoginasiohibernate.model.Equipamento;
import gestaoginasiohibernate.model.Espaco;
import gestaoginasiohibernate.model.Notaavaria;
import services.EquipamentoService;
import services.NotaAvariaService;



/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLAdministradorEquipamentoController implements Initializable {

    private ObservableList<Equipamento> equipamentoObservableList;
    private ObservableList<Equipamento> equipamentoObservableListFiltro; 
    
    private ObservableList<Notaavaria> notasAvariaObservableList;
    private ObservableList<Notaavaria> notasAvariaObservableListFiltro;
    
    private Categoriaequipamento selectedCategoria;
    private Espaco selectedEspaco;
    
    private Equipamento selectedEquipamento;
    private Notaavaria selectedNotaAvaria;
    
    @FXML private ListView<Categoriaequipamento> lvCategorias; 
    @FXML private ListView<Espaco> lvEspacos; 
    @FXML private TextField txtTotDespesasEspaco;
    @FXML private TextField txtTotDespesasCategoria;
    
    @FXML private RadioButton rbAtivoNota;
    @FXML private RadioButton rbDesativoNota;
    @FXML private RadioButton rbTodosNota;
    @FXML private ToggleGroup toggleGroupNota;
    
    @FXML private Button btLimparSelecao;
    @FXML private Button btGerirEquipamentos;
    @FXML private Button btResolverNotaAvaria;
    
    @FXML private TableView<Equipamento> tbEquipamentos;
    @FXML private TableColumn<Equipamento, String> colDescricao;
    @FXML private TableColumn<Equipamento, String> colCategoria;
    @FXML private TableColumn<Equipamento, String> colAtivo;
    @FXML private TableColumn<Equipamento, String> colTotalDespesas;

    @FXML private TableView<Notaavaria> tbNotasAvaria; 
    @FXML private TableColumn<Notaavaria, String> colNotaData;
    @FXML private TableColumn<Notaavaria, String> colNotaEquipamento;
    @FXML private TableColumn<Notaavaria, String> colNotaCategoria;
    @FXML private TableColumn<Notaavaria, String> colNotaDescricao;
    @FXML private TableColumn<Notaavaria, String> colNotaEstado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.btResolverNotaAvaria.setDisable(true);
        this.toggleGroupNota= new ToggleGroup();
        this.rbAtivoNota.setToggleGroup(this.toggleGroupNota);
        this.rbDesativoNota.setToggleGroup(this.toggleGroupNota);
        this.rbTodosNota.setToggleGroup(this.toggleGroupNota);
        this.rbAtivoNota.setSelected(true);
        this.lvCategorias.setCellFactory(FillListView.fillCategoriasEquiListView(this.lvCategorias));
        this.lvCategorias.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedCategoria=(Categoriaequipamento) newValue;
            this.filtrarTableEquipamento();
        });
        this.lvEspacos.setCellFactory(FillListView.fillEspacoListView(this.lvEspacos));
         this.lvEspacos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedEspaco=(Espaco) newValue;
            this.filtrarTableEquipamento();
        });
        this.initializaTables();
    }  
    
    private void initializaTables(){
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colCategoria.setCellValueFactory((param)->{
            Equipamento categoria= param.getValue();
            SimpleStringProperty categoriaName=null;
            ObservableValue<String>ov=null;
            categoriaName= new SimpleStringProperty(categoria.getCategoriaequipamento().getDesignacao());
            ov = categoriaName;
            return ov;
        });
        this.colAtivo.setCellValueFactory((param)->{
            Equipamento equip= param.getValue();
            SimpleStringProperty ativo=null;
            ObservableValue<String>ov=null;
            if(equip.getAtivo()=='1'){
                ativo= new SimpleStringProperty("Ativo");
                ov = ativo;
            }else{
                ativo= new SimpleStringProperty("Desativo");
                ov = ativo;
            }
            return ov;
        });
        this.colTotalDespesas.setCellValueFactory((param)->{
            Equipamento equip= param.getValue();
            SimpleStringProperty total=null;
            ObservableValue<String>ov=null;
            int tot=0;
            if(equip.getNotaavarias()!=null && equip.getNotaavarias().size()>0){
                for(Notaavaria nota:(Set<Notaavaria>) equip.getNotaavarias()){
                    if(nota.getValor()!=null){
                        int valor=nota.getValor().intValue();
                        tot= tot+valor;
                    }
                }
            }
            total= new SimpleStringProperty(String.valueOf(tot)+"€");
            ov = total;
            return ov;
        });
        List<Equipamento> listEquipamento= EquipamentoService.getAllEquipamento();
        this.equipamentoObservableList=FXCollections.observableArrayList(listEquipamento);
        this.equipamentoObservableListFiltro=FXCollections.observableArrayList(listEquipamento);
        this.colNotaData.setCellValueFactory(new PropertyValueFactory<>("datanota"));
        this.colNotaCategoria.setCellValueFactory((param)->{
            Notaavaria nota= param.getValue();
            SimpleStringProperty notaCategoria=null;
            ObservableValue<String>ov=null;
            notaCategoria= new SimpleStringProperty(nota.getEquipamento().getCategoriaequipamento().getDesignacao());
            ov = notaCategoria;
            return ov;
        });
        this.colNotaEquipamento.setCellValueFactory((param)->{
            Notaavaria nota= param.getValue();
            SimpleStringProperty equip=null;
            ObservableValue<String>ov=null;
            equip= new SimpleStringProperty(nota.getEquipamento().getDescricao());
            ov = equip;
            return ov;
        });
        this.colNotaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colNotaEstado.setCellValueFactory((param)->{
            Notaavaria nota= param.getValue();
            SimpleStringProperty estado=null;
            ObservableValue<String>ov=null;
            if(nota.getEstado()=='1'){
                estado= new SimpleStringProperty("Por Resolver");
            }else{
                estado= new SimpleStringProperty("Resolvida");
            }
            ov = estado;
            return ov;
        });
        List<Notaavaria> listNotaAvaria= NotaAvariaService.getAllNotasAvaria();
        this.notasAvariaObservableList=FXCollections.observableArrayList(listNotaAvaria);
        
        this.notasAvariaObservableListFiltro=FXCollections.observableArrayList(listNotaAvaria);
        this.fillTableEquipamentos();
        this.fillTableNotasAvaria();
    }
    
    private void fillTableEquipamentos(){
        this.tbEquipamentos.setItems(this.equipamentoObservableListFiltro);
        this.tbEquipamentos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedEquipamento=newValue;
        });
    }
    
    private void fillTableNotasAvaria(){
        this.tbNotasAvaria.setItems(this.notasAvariaObservableListFiltro);
        this.tbNotasAvaria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedNotaAvaria=newValue;
            this.btResolverNotaAvaria.setDisable(false);
        });
    }
    
    private void filtrarTableEquipamento(){
        this.equipamentoObservableListFiltro.clear();
        //this.notasAvariaObservableListFiltro.clear();
        if(this.selectedCategoria==null && this.selectedEspaco==null){
           this.equipamentoObservableListFiltro.setAll(this.equipamentoObservableList);
        }else{
            if(this.selectedCategoria!=null && this.selectedEspaco==null){
                for(Equipamento equi: this.equipamentoObservableList){
                    if(equi.getCategoriaequipamento().equals(this.selectedCategoria)){
                        this.equipamentoObservableListFiltro.add(equi);
                    }
                }
            }
            if(this.selectedEspaco!=null && this.selectedCategoria==null){
                for(Equipamento equi: this.equipamentoObservableList){
                    if(equi.getEspaco().equals(this.selectedEspaco)){
                        this.equipamentoObservableListFiltro.add(equi);
                    }
                }
            }
            if(this.selectedCategoria!=null && this.selectedEspaco!=null){
                for(Equipamento equi: this.equipamentoObservableList){
                    if(equi.getEspaco().equals(this.selectedEspaco) && equi.getCategoriaequipamento().equals(this.selectedCategoria)){
                        this.equipamentoObservableListFiltro.add(equi);
                    }
                }
            }
        }
        this.fillTableEquipamentos();
    }
    
    private void filtrarTableNotaAvaria(){
        this.notasAvariaObservableListFiltro.clear();
        if(this.rbTodosNota.isSelected()){
            this.notasAvariaObservableListFiltro.setAll(this.notasAvariaObservableList);
        }
        if(this.rbAtivoNota.isSelected()){
            for(Notaavaria nota: this.notasAvariaObservableList){
                if(nota.getEstado()=='1'){
                    this.notasAvariaObservableListFiltro.add(nota);
                }
            }
        }
        if(this.rbDesativoNota.isSelected()){
            for(Notaavaria nota: this.notasAvariaObservableList){
                if(nota.getEstado()=='0'){
                    this.notasAvariaObservableListFiltro.add(nota);
                }
            }
        }
        this.fillTableNotasAvaria();
    }
    
    @FXML 
    private void tbTodosSelected(){
        this.filtrarTableNotaAvaria();
    }
    
    @FXML 
    private void tbAtivoSelected(){
        this.filtrarTableNotaAvaria();
    }
    @FXML 
    private void tbDesativoSelected(){
        this.filtrarTableNotaAvaria();
    }
    
    @FXML 
    private void gerirEquipamentos(ActionEvent event){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FXMLGerirEquipamento.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Gerir Equipamentos");
            stage.setScene(scene);
            if(this.selectedEquipamento!=null){
                FXMLGerirEquipamentoController controller= fxmlLoader.getController();
                controller.setEquipamento(this.selectedEquipamento);
            }           
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.btGerirEquipamentos.getScene().getWindow());
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void gerirNotasAvaria(ActionEvent event){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FXMLResolverNotaAvaria.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Atualizar Nota Avaria");
            stage.setScene(scene);
            FXMLResolverNotaAvariaController controller= fxmlLoader.getController();
            controller.setNotaAvaria(this.selectedNotaAvaria);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.btGerirEquipamentos.getScene().getWindow());
            stage.showAndWait();
            this.filtrarTableEquipamento();
            this.filtrarTableNotaAvaria();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    private void ClearFields(){
        this.selectedCategoria=null;
        this.selectedEquipamento=null;
        this.selectedEspaco=null;
        this.selectedNotaAvaria=null;
        this.rbAtivoNota.setSelected(true);
        this.tbEquipamentos.getSelectionModel().clearSelection();
        this.tbNotasAvaria.getSelectionModel().clearSelection();
        this.lvCategorias.getSelectionModel().clearSelection();
        this.lvEspacos.getSelectionModel().clearSelection();
        this.filtrarTableEquipamento();
        this.filtrarTableNotaAvaria();
        this.btResolverNotaAvaria.setDisable(true);
    }
    
}
