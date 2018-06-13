/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Tipocontrato;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import projetogestaoginasio.ConvertType;
import projetogestaoginasio.ShowMessage;
import projetogestaoginasio.ValidarStrings;
import services.TipoContratoService;
import services.exception.FieldsEmptyException;
import services.exception.NumericException;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLAdministradorTipoContratoController implements Initializable {
    private Tipocontrato tipoContratoSelected;
    private ObservableList<Tipocontrato> tipoContratobservableList;
    
    
    @FXML private TableView<Tipocontrato> tbTiposContrato;
    @FXML private TableColumn<Tipocontrato, String> colDescricao;
    @FXML private TableColumn<Tipocontrato, String> colMensalidade;
    @FXML private TableColumn<Tipocontrato, Boolean> colAtivo;
    
    @FXML private Button btGravar;
    @FXML private Button btLimpaSelecao;
    
    @FXML private CheckBox cbAtivo;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtMensalidade;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.initializaTable();
        this.txtMensalidade.setTooltip(new Tooltip("Exemplo: 30.45"));
    }    
    
    private void initializaTable() {
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colMensalidade.setCellValueFactory(new PropertyValueFactory<>("valor"));
        this.colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        List<Tipocontrato> listUtente= TipoContratoService.getAllTipoContratos();
        this.tipoContratobservableList=FXCollections.observableArrayList(listUtente);
        this.setTableList();
    }
     
    private void setTableList(){
        this.tbTiposContrato.setItems(this.tipoContratobservableList);
        this.tbTiposContrato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.tipoContratoSelected=newValue;
            showTipoContrato(newValue);
        });
    }
    
    private void showTipoContrato(Tipocontrato tContrato){
        if(this.tipoContratoSelected!=null){
            this.txtDescricao.textProperty().setValue(tContrato.getDescricao());
            this.txtMensalidade.textProperty().setValue(tContrato.getValor().toString());
            if(tContrato.getAtivo()){
                this.cbAtivo.setSelected(true);
            }else{
                this.cbAtivo.setSelected(false);
            }
        }
    }
    
    @FXML
    private void updateData(){
        try{
            if(this.txtDescricao.getText().length()>0 && this.txtMensalidade.getText().length()>0){
                if(this.tipoContratoSelected==null){
                    if(ShowMessage.showConfirmation("Criar novo Contrato", "Tem a certeza que pretende criar novo Tipo Contrato?")){
                        createTipoContrato();
                    }else{
                        return;
                    }
                }else{
                    if(ShowMessage.showConfirmation("Atualizar Contrato", "Tem a certeza que pretende atualizar Tipo Contrato?")){
                        updateDataTipoContrato();
                    }else{
                        return;
                    }
                }
                this.clearSelectionAndFields();
            }else{
                throw new FieldsEmptyException();
            }
        }catch(FieldsEmptyException e){
            this.checkFieldEmpty();
            ShowMessage.showError("Erro ao atualizar", "Contem campos vazios.");
        }catch(NumericException e){
            ShowMessage.showError("Erro valor Mensalidade", "Valor da mensalidade invalido.");
        }
    }
    
    private void createTipoContrato() throws NumericException{
        ValidarStrings va= new ValidarStrings();
        if(va.validarMensalidade(this.txtMensalidade.getText())){
           boolean ativo=false;
            if(this.cbAtivo.isSelected()){
                ativo=true;
            }else{
                ativo=false;
            }
            String descricao=this.txtDescricao.getText();
            BigDecimal valor=ConvertType.stringToBigDecimal(this.txtMensalidade.getText());
            Tipocontrato tipo=TipoContratoService.createTipoContrato(ativo, descricao, valor);
            this.tipoContratobservableList.add(tipo);
        }else{
            throw new NumericException();
        }
    }
    
    private void updateDataTipoContrato() throws NumericException{
        ValidarStrings va= new ValidarStrings();
        if(va.validarMensalidade(this.txtMensalidade.getText())){
            if(this.cbAtivo.isSelected()){
                this.tipoContratoSelected.setAtivo(true);
            }else{
                this.tipoContratoSelected.setAtivo(false);
            }
            this.tipoContratoSelected.setDescricao(this.txtDescricao.getText());
            this.tipoContratoSelected.setValor(ConvertType.stringToBigDecimal(this.txtMensalidade.getText()));
            TipoContratoService.updateTipoContrato(this.tipoContratoSelected);
            this.tipoContratobservableList.set(this.tipoContratobservableList.indexOf(this.tipoContratoSelected), this.tipoContratoSelected);
        }else{
            throw new NumericException();
        }
    }
    
    private void checkFieldEmpty(){
        if(this.txtDescricao.getText().length()<=0)
            this.txtDescricao.setStyle("-fx-background-color: #ff5252;");
        if(this.txtMensalidade.getText().length()<=0)
            this.txtMensalidade.setStyle("-fx-background-color: #ff5252;");
    }
    
    @FXML
    private void clearBackgroundField(){
        this.txtDescricao.setStyle("-fx-background-color: #fffff;");
        this.txtMensalidade.setStyle("-fx-background-color: #fffff;");
    }
    
    @FXML 
    private void clearSelectionAndFields(){
        this.tbTiposContrato.getSelectionModel().clearSelection();
        this.txtDescricao.setText("");
        this.txtMensalidade.setText("");
        this.cbAtivo.setSelected(false);
        this.tipoContratoSelected=null;
        this.setTableList();
    }
}
