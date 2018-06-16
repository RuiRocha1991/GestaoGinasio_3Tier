/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Pagamento;
import gestaoginasiohibernate.model.Utente;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import gestaoginasiobll.ValidarStrings;
import gestaoginasiobll.services.UtenteService;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLAdministradorUtenteController implements Initializable {
    @FXML private Button btCriarContrato;
    @FXML private Button btPagamento;
    
    @FXML private RadioButton rbAtivos;
    @FXML private RadioButton rbDesativos;
    @FXML private RadioButton rbTodos;
    @FXML private ToggleGroup toggleGroup;
    
    @FXML private TableView<Utente> tbUtente;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colNif;
    @FXML private TableColumn<Utente, String> colEmail;
    @FXML private TableColumn<Utente, LocalDate> colDataNasc; 
    @FXML private TableColumn<Utente, String> colMensalidade;
    @FXML private TableColumn<Utente, String> colAtivo;
    
    @FXML private TableView<Pagamento> tbPagamento;
    @FXML private TableColumn<Pagamento, LocalDate> colData;
    @FXML private TableColumn<Pagamento, String> colMes;
    @FXML private TableColumn<Pagamento, String> colAno;
    @FXML private TableColumn<Pagamento, String> colTipoContrato; 
    @FXML private TableColumn<Pagamento, Integer> colValor;
    
    @FXML private TextField txtProcura;
    @FXML private TextField txtMorada;
    @FXML private TextField txtCodPostal;
    @FXML private TextField txtLocalidade;
    @FXML private TextField txtContacto;
    
    private ObservableList<Utente> utenteObservableList;
    private ObservableList<Utente> utenteObservableListFiltro;
    private ObservableList<Pagamento> pagamentoObservableList;
    
    private Utente selectedUtente;
    private Contrato selectedContrato;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.toggleGroup= new ToggleGroup();
        this.rbAtivos.setToggleGroup(this.toggleGroup);
        this.rbDesativos.setToggleGroup(this.toggleGroup);
        this.rbTodos.setToggleGroup(this.toggleGroup);
        this.rbTodos.setSelected(true);
        this.initializaTable();
        
    }    
 
    @FXML 
    private void rbDesativosSelected(){
        this.utenteObservableListFiltro.clear();
        if(this.rbDesativos.isSelected()){
            for(Utente ut: this.utenteObservableList){
               
                if(ut.getContratoAtivo()==null){
                    this.utenteObservableListFiltro.add(ut);
                }
            }
            this.setTableList();
        }
            
    }
    
    @FXML 
    private void rbAtivosSelected(){
        this.utenteObservableListFiltro.clear();
        if(this.rbAtivos.isSelected()){
            for(Utente ut: this.utenteObservableList){
               
                if(ut.getContratoAtivo()!=null){
                    this.utenteObservableListFiltro.add(ut);
                }
            }
            this.setTableList();
        }
            
    }
    
    @FXML 
    private void rbTodosSelected(){
        if(this.rbTodos.isSelected()){
            this.utenteObservableListFiltro.clear();
            this.utenteObservableListFiltro.setAll(this.utenteObservableList);
            this.setTableList();
        }
    }
    
    @FXML 
    private void pesquisa(){
        this.selectedUtente=null;
        this.selectedContrato=null;
        ValidarStrings va = new ValidarStrings();
        String procura="";
        if(this.txtProcura.getText().length()>0){
            this.utenteObservableListFiltro.clear();
            for(Utente c: this.utenteObservableList){
                String nomeLista= c.getNome().toLowerCase();
                if(va.validarNifTEL(this.txtProcura.getText())){
                    procura=String.valueOf(this.txtProcura.getText());
                }else{
                    procura=this.txtProcura.getText().toLowerCase();
                }
                String contactoLista= c.getContacto();
                String emailLista= c.getEmail().toLowerCase();
                String nifLista=String.valueOf(c.getNif());
                if(nomeLista.contains(procura) ||contactoLista.contains(procura)||
                        emailLista.contains(procura) ||nifLista.contains(procura)){
                    this.utenteObservableListFiltro.add(c);
                }
            }
            this.setTableList();
        }else{
            this.utenteObservableListFiltro.setAll(this.utenteObservableList);
            this.setTableList();
        }
    }
    
    @FXML 
    private void OpenWindowNewContrato(ActionEvent event) {
        Parent root;
        try {
           if(this.selectedUtente!=null){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLRecessionistaCriarContratos.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("New Window");
                stage.setScene(scene);
                FXMLRecessionistaCriarContratosController controller= fxmlLoader.getController();
                controller.setUtente(this.selectedUtente);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.btCriarContrato.getScene().getWindow());
                stage.showAndWait();
           }else{
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLRecessionistaCriarUtente.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("New Window");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.btCriarContrato.getScene().getWindow());
                stage.showAndWait();
           }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    private void OpenWindowPagamento(ActionEvent event) {
        Parent root;
        try {
           if(this.selectedContrato!=null ){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLRecessionistaPagamentos.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("New Window");
                stage.setScene(scene);
                FXMLRecessionistaPagamentosController controller= fxmlLoader.getController();
                //controller.setUtente(this.selectedUtente);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.btPagamento.getScene().getWindow());
                stage.showAndWait();
                this.initialize(null, null);
           }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void initializaTable() {
        this.colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.colNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.colDataNasc.setCellValueFactory(new PropertyValueFactory<>("datanascimento"));
        this.colMensalidade.setCellValueFactory((param)->{
            Utente utente= param.getValue();
            SimpleStringProperty mensalidade=null;
            ObservableValue<String>ov=null;
            if(!utente.getContratos().isEmpty()){
                Contrato contrato=utente.getContratoAtivo();
                if(contrato!=null){
                    mensalidade= new SimpleStringProperty(contrato.getValormensalidade().toString());
                    ov = mensalidade;
                }else{
                    mensalidade=new SimpleStringProperty("0");
                    ov = mensalidade;
                }
            }
            return ov;
        });
        this.colAtivo.setCellValueFactory((param)->{
            Utente utente= param.getValue();
            SimpleStringProperty ativo=null;
            ObservableValue<String>ov=null;
            if(!utente.getContratos().isEmpty()){
                Contrato contrato=utente.getContratoAtivo();
                if(contrato!=null){
                    if(contrato.getAtivo()=='1'){
                        ativo= new SimpleStringProperty("Ativo");
                    }else{
                        ativo= new SimpleStringProperty("Desativo");
                    }
                }else{
                    ativo= new SimpleStringProperty("Desativo");
                }
            }
            ov = ativo;
            return ov;
        });
        List<Utente> listUtente= UtenteService.getAllUtentes();
        this.utenteObservableList=FXCollections.observableArrayList(listUtente);
        this.utenteObservableListFiltro=FXCollections.observableArrayList(listUtente);
        this.setTableList();
    }
    
    private void setTableList(){
        this.tbUtente.setItems(this.utenteObservableListFiltro);
        this.tbUtente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedUtente=newValue;
            showUtente(newValue);
        });
    }
    
    private void showUtente(Utente utente){
        if(this.selectedUtente!=null){
            if(!this.selectedUtente.getContratos().isEmpty()){
                if(this.selectedUtente.getContratoAtivo()!=null){
                    this.selectedContrato= this.selectedUtente.getContratoAtivo();
                    this.btCriarContrato.setDisable(true);
                }
            }else{
                this.btCriarContrato.setDisable(false);
                this.selectedContrato=null;
            }
            this.txtCodPostal.textProperty().setValue(utente.getCodpostal());
            this.txtContacto.textProperty().setValue(utente.getContacto());
            this.txtLocalidade.textProperty().setValue(utente.getLocalidade());
            this.txtMorada.textProperty().setValue(utente.getMorada());
        }else{
            this.btCriarContrato.setDisable(false);
        }
        if(this.selectedContrato!=null){
            this.colData.setCellValueFactory(new PropertyValueFactory<>("datapagamento"));
            this.colMes.setCellValueFactory((param)->{
                Pagamento pag= param.getValue();
                SimpleStringProperty pagamento=new SimpleStringProperty(pag.getId().getMespagamento());
                ObservableValue<String> ov = pagamento;
                return ov;
            });
            this.colAno.setCellValueFactory((param)->{
                Pagamento pag= param.getValue();
                SimpleStringProperty pagamento=new SimpleStringProperty(pag.getId().getAnopagamento());
                ObservableValue<String> ov = pagamento;
                return ov;
            });
            this.colTipoContrato.setCellValueFactory((param)->{
                Pagamento pag= param.getValue();
                SimpleStringProperty pagamento=new SimpleStringProperty(pag.getContrato().getTipocontrato().getDescricao());
                ObservableValue<String> ov = pagamento;
                return ov;
            });
        this.colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        List<Pagamento> listPagamento= (List<Pagamento>) this.selectedContrato.getPagamentos().stream().collect(Collectors.toList());
        this.pagamentoObservableList=FXCollections.observableList(listPagamento);
        this.tbPagamento.setItems(this.pagamentoObservableList);
       }else{
            if(this.pagamentoObservableList!=null && !this.pagamentoObservableList.isEmpty()){
                this.pagamentoObservableList.clear();
            }   
       }

    }
    
    @FXML
    private void clearFields(){
        this.txtCodPostal.setText("");
        this.txtContacto.setText("");
        this.txtLocalidade.setText("");
        this.txtMorada.setText("");
        this.txtProcura.setText("");
        this.tbUtente.getSelectionModel().clearSelection();
        this.tbPagamento.getSelectionModel().clearSelection();
        this.rbTodos.setSelected(true);
        if(this.pagamentoObservableList!=null)
            this.pagamentoObservableList.clear();
    }
    
}
