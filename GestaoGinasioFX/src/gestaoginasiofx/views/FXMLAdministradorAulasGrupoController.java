/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Professor;
import gestaoginasiohibernate.model.Sala;
import gestaoginasiohibernate.model.Tipoaula;
import java.net.URL;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import gestaoginasiofx.FillComboBox;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import projetogestaoginasio.ShowMessage;
import services.AulaService;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLAdministradorAulasGrupoController implements Initializable {
    private Tipoaula selectedTipoAula;
    private Tipoaula selectedTipoAulaFiltro;
    private Sala selectedSala;
    private Sala selectedSalaFiltro;
    private Professor selectedProfessor;
    private ObservableList<Aula> observableListAulas;
    private ObservableList<Aula> observableListAulasFiltro;
    private LocalDate dataSelectedFiltro;
    private Aula selectedAula;
    
    @FXML private DatePicker dpDate;
    @FXML private DatePicker dpDateFiltro;
    @FXML private ComboBox cbTipoAula;
    @FXML private ComboBox cbHoras;
    @FXML private ComboBox cbSala;
    @FXML private ComboBox cbProfessor;
    @FXML private ComboBox cbTipoAulaFiltro;
    @FXML private ComboBox cbSalaFiltro;
    @FXML private Spinner spDuracaoHoras;
    @FXML private Spinner spDuracaoSemanas;
    @FXML private TextField txtDescricao;
    @FXML private Button btAddAula;
    @FXML private Button btGerirTipoAula;
    @FXML private Button btNextDay;
    @FXML private Button btPreviousDay;
    
    @FXML private TableView<Aula> tbAulas;
    @FXML private TableColumn<Aula, Date> colDate;
    @FXML private TableColumn<Aula, String> colTime;
    @FXML private TableColumn<Aula, String> colTipoAula;
    @FXML private TableColumn<Aula, String> colSala;
    @FXML private TableColumn<Aula, String> colDescricao;
    @FXML private TableColumn<Aula, String> colProfessor;
    @FXML private TableColumn<Aula, String> colDuracao;
    @FXML private TableColumn<Aula, String> colVagas;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.dpDate.setValue(LocalDate.now());
        this.dpDateFiltro.setValue(LocalDate.now());
        this.started();
        this.initializaTable();
    }    
    
    public void started(){
        this.spDuracaoHoras.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,24,1));
        this.spDuracaoSemanas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,20,4));
        this.cbTipoAula.setConverter(FillComboBox.fillTipoAulaComboBox(this.cbTipoAula));
        this.cbTipoAula.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.selectedTipoAula= (Tipoaula) newValue;
        });
        this.cbHoras.setConverter(FillComboBox.fillHorasComboBox(this.cbHoras));
        this.cbSala.setConverter(FillComboBox.fillSalaComboBox(this.cbSala));
        this.cbSala.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.selectedSala= (Sala) newValue;
        });
        this.cbProfessor.setConverter(FillComboBox.fillProfessorComboBox(this.cbProfessor));
        this.cbProfessor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.selectedProfessor= (Professor) newValue;
        });
        this.cbTipoAulaFiltro.setConverter(FillComboBox.fillTipoAulaComboBox(this.cbTipoAulaFiltro));
        this.cbTipoAulaFiltro.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.selectedTipoAulaFiltro= (Tipoaula) newValue;
            this.filtrarListObservable();
        });
        this.cbSalaFiltro.setConverter(FillComboBox.fillSalaComboBox(this.cbSalaFiltro));
        this.cbSalaFiltro.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.selectedSalaFiltro=(Sala) newValue;
            this.filtrarListObservable();
        });
    }
    @FXML
    private void updateData(){
        if(this.cbProfessor.getValue() != null && this.cbSala.getValue()!=null && this.cbTipoAula.getValue()!=null && 
                    this.dpDate.getValue()!=null && this.txtDescricao.getText()!=null){
            if(this.selectedAula==null){
                if(ShowMessage.showConfirmation("Confirmação para inserir", "Tem a certeza que pretende inserir?")){
                    this.addAula();
                }else{
                    return;
                }
            }else{
                if(ShowMessage.showConfirmation("Confirmação para atualizar", "Tem a certeza que pretende atualizar a aula?")){
                    this.updateAula();
                }else{
                    return;
                }
            }
        }else{
                ShowMessage.showConfirmation("Erro a Inserir", "Contem campos vazios");
            }
        this.limparCamposInsert();
        this.filtrarListObservable();
    }
    
    private void addAula(){
        Date date = java.sql.Date.valueOf(this.dpDate.getValue());
        LocalDate localDate=this.dpDate.getValue();
        Date dataAula=date;
        String descricaoAula=this.txtDescricao.textProperty().getValue();
        int duracaoHoras=(int)this.spDuracaoHoras.getValue();
        String horaAula=(String)this.cbHoras.getValue().toString();
        Professor professor=this.selectedProfessor;
        Sala sala=this.selectedSala;
        Tipoaula tipoAula=this.selectedTipoAula;
        int duracaoSemanas=(int)this.spDuracaoSemanas.getValue();
        this.observableListAulas.addAll(AulaService.addAula(date,localDate,duracaoSemanas, descricaoAula,duracaoHoras, horaAula, professor, 
                sala,tipoAula));
    }    
    private void updateAula(){
        Date date = java.sql.Date.valueOf(this.dpDate.getValue());
        LocalDate localDate=this.dpDate.getValue();
        this.selectedAula.setData(date);
        this.selectedAula.setDescricao(this.txtDescricao.textProperty().getValue());
        this.selectedAula.setDuracao((int)this.spDuracaoHoras.getValue());
        this.selectedAula.setHora((String)this.cbHoras.getValue().toString());
        this.selectedAula.setProfessor(this.selectedProfessor);
        this.selectedAula.setSala(this.selectedSala);
        this.selectedAula.setTipoaula(this.selectedTipoAula);
        AulaService.updateAula(this.selectedAula);
    }
    
    @FXML 
    private void deleteAula(){
        if(this.selectedAula!=null){
            if(ShowMessage.showConfirmation("Eliminar Aula", "Tem a certeza que pertende eliminar a aula selecionada?")){
                if(LocalDate.parse(this.selectedAula.getData().toString()).isAfter(LocalDate.now())||
                        LocalDate.parse(this.selectedAula.getData().toString()).isEqual(LocalDate.now())&&
                        LocalTime.parse(this.selectedAula.getHora()).isAfter(LocalTime.now())){
                    this.observableListAulas.remove(this.selectedAula);    
                    AulaService.deleteAula(selectedAula);
                    this.filtrarListObservable();
                    this.limparSelecao();
                }else{
                    ShowMessage.showError("Erro Eliminar", "Aula não pode ser eliminada porque ja foi realizada.");
                }
            }else{
                return;
            }
        }
    }
    
    private void initializaTable() {
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        this.colTime.setCellValueFactory(new PropertyValueFactory<>("hora"));
        this.colProfessor.setCellValueFactory((TableColumn.CellDataFeatures<Aula, String> param) -> {
            Aula aula= param.getValue();
            ObservableValue<String>ov=null;
            ov=new SimpleStringProperty(aula.getProfessor().getColaborador().getNome());
            return ov;
        });
        this.colTipoAula.setCellValueFactory((TableColumn.CellDataFeatures<Aula, String> param) -> {
            Aula aula= param.getValue();
            ObservableValue<String>ov=null;
            ov=new SimpleStringProperty(aula.getTipoaula().getNome());
            return ov;
        });
        this.colSala.setCellValueFactory((TableColumn.CellDataFeatures<Aula, String> param) -> {
            Aula aula= param.getValue();
            ObservableValue<String>ov=null;
            ov=new SimpleStringProperty(aula.getSala().getEspaco().getDescricao());
            return ov;
        });
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        this.colVagas.setCellValueFactory((TableColumn.CellDataFeatures<Aula, String> param) -> {
            Aula aula= param.getValue();
            ObservableValue<String>ov=null;
            int vagas=aula.getInscritos();
            ov=new SimpleStringProperty(String.valueOf(vagas));
            return ov;
        });
        List aulasList= AulaService.getAulasAll();
        this.observableListAulas=FXCollections.observableArrayList(aulasList);
        this.observableListAulasFiltro=FXCollections.observableArrayList(aulasList);
        this.filtrarListObservable();
    }
    
    @FXML 
    private void filtrarListObservable(){
        if(!this.observableListAulasFiltro.isEmpty()){
           this.observableListAulasFiltro.clear();
        }
        this.dataSelectedFiltro=this.dpDateFiltro.getValue();
        if(this.dataSelectedFiltro!=null){
            for(Aula a :this.observableListAulas){
                if(a.getData().toString().equals(this.dataSelectedFiltro.toString())){
                    if(this.selectedSalaFiltro!=null && this.selectedTipoAulaFiltro!=null){
                        if(a.getSala().equals(this.selectedSalaFiltro) && a.getTipoaula().equals(this.selectedTipoAulaFiltro)){
                            this.observableListAulasFiltro.add(a);
                        }
                    }else{
                        if(this.selectedSalaFiltro!=null){
                            if(a.getSala().equals(this.selectedSalaFiltro)){
                                this.observableListAulasFiltro.add(a);
                            }
                        }
                        if(this.selectedTipoAulaFiltro!=null){
                            if(this.selectedTipoAulaFiltro.equals(a.getTipoaula())){
                                this.observableListAulasFiltro.add(a);
                            }
                        }
                        if(this.selectedSalaFiltro==null && this.selectedTipoAulaFiltro==null){
                            this.observableListAulasFiltro.add(a);
                        }
                    }
                }
            }
        }
        this.tbAulas.setItems(this.observableListAulasFiltro);
        this.tbAulas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedAula=(Aula)newValue;
            this.showAula(newValue);
        });
    }
    
    private void showAula(Aula aula){
        if(this.selectedAula!=null){
            this.txtDescricao.textProperty().setValue(aula.getDescricao());
            this.cbHoras.getSelectionModel().select(LocalTime.parse(aula.getHora()));
            this.dpDate.setValue(LocalDate.parse(aula.getData().toString()));
            this.cbTipoAula.getSelectionModel().select(aula.getTipoaula());
            this.cbSala.getSelectionModel().select(aula.getSala());
            this.cbProfessor.getSelectionModel().select(aula.getProfessor());
            this.spDuracaoHoras.getValueFactory().setValue(aula.getDuracao());
            this.spDuracaoSemanas.getValueFactory().setValue(0);
        }
    }
    
    @FXML 
    private void nextDay(){
        this.dpDateFiltro.setValue(this.dataSelectedFiltro.plusDays(1));
    }
    
    @FXML 
    private void previousDay(){
        this.dpDateFiltro.setValue(this.dataSelectedFiltro.plusDays(-1));
    }
    
    @FXML
    private void limparSelecao(){
        this.cbSalaFiltro.getSelectionModel().clearSelection();
        this.cbTipoAulaFiltro.getSelectionModel().clearSelection();
        this.selectedAula=null;
        this.selectedProfessor=null;
        this.selectedSala=null;
        this.selectedSalaFiltro=null;
        this.selectedTipoAula=null;
        this.selectedTipoAulaFiltro=null;
        this.dpDateFiltro.setValue(LocalDate.now());
        this.dataSelectedFiltro=LocalDate.now();
        this.limparCamposInsert();
        this.filtrarListObservable();
    }
    
    private void limparCamposInsert(){
        this.txtDescricao.textProperty().setValue("");
        this.cbHoras.getSelectionModel().clearSelection();
        this.cbProfessor.getSelectionModel().clearSelection();
        this.cbSala.getSelectionModel().clearSelection();
        this.cbTipoAula.getSelectionModel().clearSelection();
        this.dpDate.setValue(LocalDate.now());
        this.spDuracaoHoras.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,24,1));
        this.spDuracaoSemanas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,20,4));
    }
    
    @FXML 
    private void OpenWindowTipoAula(ActionEvent event) {
        Parent root;
        try {
            
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FXMLRecessionistaTipodeAulas.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
