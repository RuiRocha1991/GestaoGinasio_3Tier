/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import com.sun.webkit.ContextMenu;
import gestaoginasiofx.FillListView;
import gestaoginasiofx.Toast;
import gestaoginasiohibernate.model.Categoriaequipamento;
import gestaoginasiohibernate.model.Equipamento;
import gestaoginasiohibernate.model.Espaco;
import gestaoginasiohibernate.model.Notaavaria;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import projetogestaoginasio.ShowMessage;
import services.EquipamentoService;
import services.NotaAvariaService;
import sun.plugin2.message.ShowDocumentMessage;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLInstrutorNotasAvariasController implements Initializable {
    private Categoriaequipamento categoriaSelected;
    private Espaco espacoSelected;
    private Equipamento equipamentoSelected;
    private ObservableList<Equipamento> equipamentoObservableList;
    private ObservableList<Equipamento> equipamentoObservableListFiltro;
    
    @FXML private TextField txtEquipamento;
    @FXML private TextField txtProcura;
    @FXML private DatePicker dpDate;
    @FXML private TextArea txtDescricao;
    @FXML private ImageView imSearch;
    
    @FXML private ListView lvCategoria;
    @FXML private ListView lvEspaco;
    
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
        this.dpDate.setValue(LocalDate.now());
        this.txtProcura.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue){
                if (newPropertyValue){
                    imSearch.setVisible(false);
                }else{
                    if(txtProcura.getText().length()<=0){
                        imSearch.setVisible(true);
                    }
                }
            }
        });
        this.fillTableAndList();
    }    
    
    @FXML 
    private void fillTableAndList(){
        this.lvCategoria.setCellFactory(FillListView.fillCategoriasEquiListView(this.lvCategoria));
        this.lvCategoria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.categoriaSelected=(Categoriaequipamento) newValue;
            this.filtrarTable();
        });
        this.lvEspaco.setCellFactory(FillListView.fillEspacoListView(this.lvEspaco));
        this.lvEspaco.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.espacoSelected=(Espaco) newValue;
            this.filtrarTable();
        });
        
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
        List<Equipamento> lista=EquipamentoService.getAllEquipamento();
        this.equipamentoObservableList=FXCollections.observableArrayList(lista);
        this.equipamentoObservableListFiltro=FXCollections.observableArrayList(lista);
        this.setTable();
    }
    
    private void setTable(){
        this.tbEquipamentos.setItems(this.equipamentoObservableListFiltro);
        this.tbEquipamentos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.equipamentoSelected=(Equipamento) newValue;
             if(this.equipamentoSelected!=null)
                 this.txtEquipamento.textProperty().setValue(newValue.getDescricao());
        });
    }
    
    private void filtrarTable(){
        if(this.espacoSelected==null && this.categoriaSelected==null){
            this.equipamentoObservableListFiltro.setAll(this.equipamentoObservableList);
        }else{
            if(this.categoriaSelected!=null &&this.espacoSelected==null){
                this.equipamentoObservableListFiltro.setAll(this.categoriaSelected.getEquipamentos());
            }else{
                if(this.espacoSelected!=null && this.categoriaSelected==null){
                    this.equipamentoObservableListFiltro.setAll(this.espacoSelected.getEquipamentos());
                }else{
                    this.equipamentoObservableListFiltro.setAll(EquipamentoService.getEquipamentoEspacoAndCategoria(this.equipamentoObservableList, this.espacoSelected, this.categoriaSelected));
                }
            }
        }
    }
    
    @FXML
    private void limparSelecao(){
        this.txtDescricao.setText("");
        this.txtEquipamento.setText("");
        this.txtProcura.setText("");
        this.dpDate.setValue(LocalDate.now());
        this.lvCategoria.getSelectionModel().clearSelection();
        this.lvEspaco.getSelectionModel().clearSelection();
        this.tbEquipamentos.getSelectionModel().clearSelection();
        this.imSearch.setVisible(true);
        this.equipamentoSelected=null;
        this.espacoSelected=null;
        this.categoriaSelected=null;
        
    }
    
    @FXML
    private void searchToDescricao(){
        if(this.txtProcura.getText().length()>0){
            this.equipamentoObservableListFiltro.setAll(EquipamentoService.getEquipamentoToName(this.equipamentoObservableList, this.txtProcura.getText()));
        }else{
            this.equipamentoObservableListFiltro.setAll(this.equipamentoObservableList);
        }
    }
    
    @FXML
    private void saveNotaAvaria(ActionEvent event){
         Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        if(this.equipamentoSelected!=null){
            if(ShowMessage.showConfirmation("Criar nota avaria", "Tem a certeza que pretende criar uma nota de avaria para o equipamento selecionado?")){
                if(this.txtDescricao.getText().length()>0){
                    NotaAvariaService.createNotaAvaria(this.equipamentoSelected, Date.valueOf(this.dpDate.getValue()),
                                                        this.txtDescricao.getText());
                    this.equipamentoObservableList.set(this.equipamentoObservableList.indexOf(this.equipamentoSelected), this.equipamentoSelected);
                    Toast.makeText(stage, "Teste", 3500, 500, 500);
                    this.limparSelecao();
                }
            }else{
                return;
            }
        }else{
            ShowMessage.showError("Erro a Inserir", "Não tem equipamento selecionado.");
        }
    }
    
}
