/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import enumerados.EnumTipoEspaco;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import gestaoginasiofx.FillComboBox;
import gestaoginasiohibernate.model.Equipamento;
import gestaoginasiohibernate.model.Espaco;
import gestaoginasiohibernate.model.Espacocomum;
import gestaoginasiohibernate.model.Sala;
import projetogestaoginasio.ShowMessage;
import gestaoginasiobll.services.EspacoService;
import gestaoginasiofx.Notificacao;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLAdministradorEspacoController implements Initializable {
    private ObservableList<Espaco> observableListEspacos;
    private ObservableList<Equipamento> observableListEquipamentos;
    private Espaco selectedEspaco;
    
    @FXML private ComboBox cbTipoEspaco;
    @FXML private TextField txtDescricao; 
    
    @FXML private TableView<Espaco> tbEspacos;
    @FXML private TableColumn<Espaco, String> colDescicao;
    @FXML private TableColumn<Espaco, String> colTipoEspaco;
    @FXML private TableColumn<Espaco, String> colVagas;
    
    @FXML private TableView<Equipamento> tbEquipamento;
    @FXML private TableColumn<Equipamento, String> colDescicaoEqui;
    @FXML private TableColumn<Equipamento, String> colCategoria;
    @FXML private TableColumn<Equipamento, String> colAtivo;
   
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.start();
    }    
    
    private void start(){
        this.cbTipoEspaco.setItems(FillComboBox.fillTipoEspacoComboBox());
        this.initializeTableEquipamentos();
        this.initializeTableEspacos();
    }
    
    private void initializeTableEquipamentos(){
        this.colDescicaoEqui.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colCategoria.setCellValueFactory((TableColumn.CellDataFeatures<Equipamento, String> param) -> {
            Equipamento equi= param.getValue();
            ObservableValue<String>ov=null;
            ov=new SimpleStringProperty(equi.getCategoriaequipamento().getDesignacao());
            return ov;
        });
        this.colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
    }
    
    private void initializeTableEspacos(){
        this.colDescicao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colTipoEspaco.setCellValueFactory((TableColumn.CellDataFeatures<Espaco, String> param) -> {
            Espaco esp= param.getValue();
            ObservableValue<String>ov=null;
            if(esp.getEspacocomum()!=null){
                 ov=new SimpleStringProperty(EnumTipoEspaco.ESPAÇOCOMUM.toString());
            }else{
                ov=new SimpleStringProperty(EnumTipoEspaco.SALA.toString());
            }
            return ov;
        });
        this.colVagas.setCellValueFactory((TableColumn.CellDataFeatures<Espaco, String> param) -> {
            Espaco esp= param.getValue();
            ObservableValue<String>ov=null;
            if(esp.getEspacocomum()!=null){
                 ov=new SimpleStringProperty(String.valueOf(esp.getEquipamentos().size()));
            }else{
                ov=new SimpleStringProperty(String.valueOf(esp.getSala().getNumerovagas()));
            }
            return ov;
        });
        List aulasList= EspacoService.getAllEspacos();
        this.observableListEspacos=FXCollections.observableArrayList(aulasList);
        this.tbEspacos.setItems(this.observableListEspacos);
        this.tbEspacos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedEspaco=(Espaco)newValue;
            this.showEspaco(newValue);
           
        });
    }
    
    private void showEspaco(Espaco espaco){
        if(this.selectedEspaco!=null){
            this.txtDescricao.textProperty().setValue(espaco.getDescricao());
        if(espaco.getEspacocomum()!=null){
            this.cbTipoEspaco.getSelectionModel().select(0);
        }else{
            this.cbTipoEspaco.getSelectionModel().select(1);
        }
        this.observableListEquipamentos=FXCollections.observableArrayList(espaco.getEquipamentos());
        this.tbEquipamento.setItems(this.observableListEquipamentos);
        }
    }
    
    @FXML 
    private void clearField(){
        this.txtDescricao.textProperty().setValue("");
        this.cbTipoEspaco.getSelectionModel().clearSelection();
        this.tbEspacos.getSelectionModel().clearSelection();
        this.selectedEspaco=null;
        if(this.observableListEquipamentos!=null &&this.observableListEquipamentos.size()>0)
            this.observableListEquipamentos.clear();
    }
    
    @FXML 
    private void updateData(){
        if(this.txtDescricao.getText().length()>0 && !this.cbTipoEspaco.getSelectionModel().isEmpty()){
            if(this.selectedEspaco==null){
                if(ShowMessage.showConfirmation("Inserir Novo Espaço", "Tem a certeza que pretende criar um espaço novo?")){
                    this.createEspaco();
                    Notificacao.successNotification("Espaço", "Espaço criado com sucesso");
                }else{
                    return;
                }
            }else{
                if(ShowMessage.showConfirmation("Atualizar Espaço", "Tem a certeza que pretende atualizar o espaço?")){
                    this.updateEspaco();
                    Notificacao.successNotification("Espaço", "Espaço atualizado com sucesso");
                }else{
                    return;
                }
            }
        }else{
            if(this.txtDescricao.getText().length()<=0){
              this.txtDescricao.setStyle("-fx-background-color: #ff5252;");
            }
            if(this.cbTipoEspaco.getSelectionModel().isEmpty()){
                this.cbTipoEspaco.setStyle("-fx-background-color: #ff5252;");
            }
        }
    }
    
    private void createEspaco(){
        Espaco newEsp= EspacoService.createEspaco(this.txtDescricao.getText(), this.cbTipoEspaco.getVisibleRowCount());
        this.observableListEspacos.add(newEsp);
        this.clearField();
    }
    
    private void updateEspaco(){
        this.selectedEspaco.setDescricao(this.txtDescricao.getText());
        EspacoService.updateEspaco(this.selectedEspaco);
        this.observableListEspacos.add(this.observableListEspacos.indexOf(this.selectedEspaco), selectedEspaco);
        this.clearField();
    }
    
    @FXML
    private void clearBackground(){
        this.txtDescricao.setStyle("-fx-background-color: #fffff;");
        this.cbTipoEspaco.setStyle("-fx-background-color: #fffff;");
    }
}
