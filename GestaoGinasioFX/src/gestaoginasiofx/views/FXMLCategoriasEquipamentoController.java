/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.services.CategoriaEquipamentoService;
import gestaoginasiofx.Notificacao;
import gestaoginasiohibernate.model.Categoriaequipamento;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import projetogestaoginasio.ShowMessage;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLCategoriasEquipamentoController implements Initializable {

    private Categoriaequipamento categoriaSelected;
    private ObservableList<Categoriaequipamento> observableListCategoria;
    private ObservableList<Categoriaequipamento> observableListCategoriaFiltrada;
    
    @FXML private TextField txtCategoria;
    @FXML private TextField txtSearch;
    @FXML private Button btGravar;
    
    @FXML private TableView<Categoriaequipamento> tbCategorias;
    @FXML private TableColumn<Categoriaequipamento, String> colDescricao;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.observableListCategoriaFiltrada=FXCollections.observableArrayList(CategoriaEquipamentoService.getAllCategorias());
        this.observableListCategoria=FXCollections.observableArrayList(CategoriaEquipamentoService.getAllCategorias());
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        this.fillTable();
    }  
    
    @FXML
    private void fillTable(){
        this.observableListCategoriaFiltrada.clear();
        if(this.txtSearch.getText().length()>0){
            this.observableListCategoriaFiltrada=FXCollections.observableArrayList(
                    CategoriaEquipamentoService.getCategoriasFiltrada(observableListCategoria, this.txtSearch.getText()));
        }else{
            this.observableListCategoriaFiltrada.setAll(this.observableListCategoria);
        }
        this.tbCategorias.setItems(this.observableListCategoriaFiltrada);
        this.tbCategorias.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.categoriaSelected=newValue;
            this.showCategoria();
        });
        
    }
    
    private void showCategoria(){
        if(this.categoriaSelected!=null){
            this.txtCategoria.textProperty().setValue(this.categoriaSelected.getDesignacao());
        }
    }
    
    @FXML
    private void clearFields(){
        this.txtCategoria.setText("");
        this.tbCategorias.getSelectionModel().clearSelection();
        this.categoriaSelected=null;
        this.txtSearch.setText("");
        this.fillTable();
    }
    
    @FXML
    private void updateCategoria(){
        if(this.categoriaSelected==null){
            if(ShowMessage.showConfirmation("Inserir Nova Categoria", "Tem a certeza que pretende inserir nova categoria?")){
                if(this.txtCategoria.getText().length()>0){
                    this.observableListCategoria.add(CategoriaEquipamentoService.createCategoria(this.txtCategoria.getText()));
                    Notificacao.successNotification("Categoria Equipamento", "Adicionada Categoria com sucesso.");
                    this.clearFields();
                    this.fillTable();
                }else{
                    ShowMessage.showError("Campo Vazio", "O campo categoria não pode estar vazio.");
                }
            }else{
                return;
            }
        }else{
            if(ShowMessage.showConfirmation("Atualizar Categoria", "Tem a certeza que pretende atualizar categoria?")){
                if(this.txtCategoria.getText().length()>0){
                    CategoriaEquipamentoService.updateCategoria(this.categoriaSelected,this.txtCategoria.getText());
                    Notificacao.successNotification("Categoria Equipamento", "Atualizada Categoria com sucesso.");
                    this.clearFields();
                    this.fillTable();
                }else{
                    ShowMessage.showError("Campo Vazio", "O campo categoria não pode estar vazio.");
                }
            }else{
                return;
            }
        }
        
    }
    
}
