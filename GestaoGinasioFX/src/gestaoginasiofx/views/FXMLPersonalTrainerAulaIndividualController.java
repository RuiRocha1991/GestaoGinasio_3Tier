/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.ConvertType;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.services.AulaIndividualService;
import gestaoginasiobll.services.ContratoService;
import gestaoginasiobll.services.ProfessorService;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Personaltrainer;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import projetogestaoginasio.ShowMessage;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLPersonalTrainerAulaIndividualController implements Initializable {

    private ObservableList<Contrato> contratoObservableList;
    private ObservableList<Contrato> contratoObservableListFiltro;
    private Contrato contratoSelected;
    private Personaltrainer ptSelected;
    private ObservableList<LocalTime> horasObservableList;
    private LocalDate dateSelected;
    private LocalTime timeSelected;
    private int valor;
    
    @FXML private TextField txtUtente;
    @FXML private TextField txtValor;
    @FXML private TextField txtSearch;
    @FXML private TableView<Contrato> tbContrato;
    @FXML private TableColumn<Contrato, String> colNome;
    @FXML private TableColumn<Contrato, String> colNif;
    @FXML private TableColumn<Contrato, String> colEmail;
    @FXML private Spinner spDuracao;
    @FXML private ComboBox cbHoras;
    @FXML private DatePicker dpDate;
    @FXML private Button btNovo;
    @FXML private Button btCancelar;
    @FXML private Button btNext;
    @FXML private Button btPrevious;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.spDuracao.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,4,1));
        this.initializeTable();
    }  
    
    public void setPersonalTrainer(Personaltrainer pt){
        this.ptSelected=pt;
        this.dpDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            this.dateSelected=newValue;
            if(this.dateSelected!=null)
                this.fillComboBox();
        });
        this.dpDate.setValue(LocalDate.now());
        this.changeValuePayment();
    }
    
     private void fillComboBox(){
        if(this.horasObservableList!=null){
            this.horasObservableList.clear();
        }
        horasObservableList = FXCollections.observableArrayList(ProfessorService.getTimeFromDate(this.ptSelected.getProfessor().getColaborador(), this.dateSelected));
        this.cbHoras.setItems(horasObservableList);
         this.cbHoras.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.timeSelected=(LocalTime) newValue;
            this.cbHoras.setStyle("-fx-background-color: #fffff;");
        });
    }
    
    private void initializeTable(){
         this.colNome.setCellValueFactory((param)->{
            Contrato contrato= param.getValue();
            SimpleStringProperty cont=null;
            ObservableValue<String> ov=null;
            cont= new SimpleStringProperty(contrato.getUtente().getNome());
            ov = cont;
            return ov;
        });
        this.colNif.setCellValueFactory((param)->{
            Contrato contrato= param.getValue();
            SimpleStringProperty cont=null;
            ObservableValue<String> ov=null;
            cont= new SimpleStringProperty(String.valueOf(contrato.getUtente().getNif()));
            ov = cont;
            return ov;
        });
        this.colEmail.setCellValueFactory((param)->{
            Contrato contrato= param.getValue();
            SimpleStringProperty cont=null;
            ObservableValue<String> ov=null;
            cont= new SimpleStringProperty(contrato.getUtente().getEmail());
            ov = cont;
            return ov;
        });
        List<Contrato> lista=ContratoService.getListContratoActive();
        this.contratoObservableList=FXCollections.observableArrayList(lista);
        this.contratoObservableListFiltro=FXCollections.observableArrayList(lista);
        this.setTable();
    }
    
    private void setTable(){
        this.tbContrato.setItems(this.contratoObservableListFiltro);
        this.tbContrato.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.contratoSelected=(Contrato) newValue;
                if(this.contratoSelected!=null){
                    this.txtUtente.setStyle("-fx-background-color: #fffff;");
                    this.txtUtente.textProperty().setValue(this.contratoSelected.getUtente().getNome());
                }
        });
    }
    
    @FXML 
    private void search(){
        if(this.contratoObservableListFiltro!=null)
            this.contratoObservableListFiltro.clear();
       if(this.txtSearch.getText().length()>0){
            this.contratoObservableListFiltro=FXCollections.observableArrayList(
                ContratoService.getListFiltraContrato(this.txtSearch.getText(), this.contratoObservableList));
       }else{
           this.contratoObservableListFiltro.setAll(this.contratoObservableList);
       }
       this.setTable();
    }
    
     @FXML
    private void nextOrPreviousDay(ActionEvent event) throws IOException{
        switch(((Button)event.getSource()).getId()){
            case "btPrevious":
                this.dpDate.setValue(this.dpDate.getValue().plusDays(-1));
                this.fillComboBox();
                break;
            case "btNext":
                this.dpDate.setValue(this.dpDate.getValue().plusDays(1));
                this.fillComboBox();
                break;
        }
    }
    
    @FXML
    private void changeValuePayment(){
        int duracao= (Integer)this.spDuracao.getValue();
        this.valor=duracao*ConvertType.BigDecimalToInteger(this.ptSelected.getPrecohora());
        this.txtValor.textProperty().setValue(String.valueOf(this.valor)+"€");
    }
    
    @FXML
    private void createAula(){
        if(ShowMessage.showConfirmation("Criar nova aula", "Tem a certeza que pretende criar uma nova aula individual?")){
            if(this.contratoSelected!=null && this.timeSelected!=null){
                try{
                    AulaIndividualService.createAulaIndividual(this.contratoSelected, this.ptSelected, this.dateSelected, this.timeSelected, String.valueOf(this.valor), this.spDuracao.getValue().toString());
                    this.cleanFillds();
                }catch(NumericException e){
                    ShowMessage.showError("Valor Inválido", "O campo valor não está correto");
                }
            }else{
                ShowMessage.showError("Informação Inválida", "Hora ou Utente Invalidados");
                if(this.contratoSelected==null){
                    this.txtUtente.setStyle("-fx-background-color: #f44336;");
                } 
                if(this.timeSelected==null){
                    this.cbHoras.setStyle("-fx-background-color: #f44336;");
                } 
            }
        }else{
            return;
        }
    }
    
    private void cleanFillds(){
        this.horasObservableList.clear();
        this.dateSelected=LocalDate.now();
        this.dpDate.setValue(this.dateSelected);
        System.out.println(this.dateSelected);
        this.contratoSelected=null;
        this.txtUtente.setText("");
        this.txtValor.setText("");
        this.txtSearch.setText("");
        this.spDuracao.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,4,1));
    }
    
    
    
}
