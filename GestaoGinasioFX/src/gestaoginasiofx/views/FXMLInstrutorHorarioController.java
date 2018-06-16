/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.AulaContrato;
import gestaoginasiobll.AulaProfessor;
import gestaoginasiobll.services.AulaIndividualService;
import gestaoginasiobll.services.ContratoService;
import gestaoginasiobll.services.ProfessorService;
import gestaoginasiohibernate.model.Colaborador;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLInstrutorHorarioController implements Initializable {
    
    private Colaborador colaborador;
    private ObservableList aulasFiltradasObservableList; 
    private AulaProfessor aulaSelected;
    private LocalDate date;
    
    @FXML private TableView<AulaProfessor> tbAulas;
    @FXML private TableColumn<AulaProfessor, LocalTime> colHora;
    @FXML private TableColumn<AulaProfessor, String> colSala;
    @FXML private TableColumn<AulaProfessor, String> colDescricao;
    @FXML private TableColumn<AulaProfessor, String> colTipoAula;
    @FXML private TableColumn<AulaProfessor, String> colDuracao;
    @FXML private TableColumn<AulaProfessor, Integer> colInscritos;
    
    @FXML private DatePicker dpDate;
    @FXML private Button btPrevious;
    @FXML private Button btNext;
    @FXML private Button btCriarAula;
    @FXML private Button btCancelarAula;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(FXMLAdministradorController.getCt()!=null){
           this.colaborador=FXMLAdministradorController.getCt();
       }else{
           if(FXMLInstrutorController.getCt()!=null){
                this.colaborador=FXMLInstrutorController.getCt();
           }
       }
        if(this.colaborador.getTipofuncionario().equals("INSTRUTOR")||this.colaborador.getTipofuncionario().equals("PROFESSOR")){
            this.btCancelarAula.setVisible(false);
            this.btCriarAula.setVisible(false);
        }else{
            this.btCancelarAula.setDisable(true);
        }
            
        this.dpDate.setValue(LocalDate.now());
        this.initializeTable();
    }  
    
    private void initializeTable(){
        this.colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        this.colSala.setCellValueFactory(new PropertyValueFactory<>("sala"));
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colTipoAula.setCellValueFactory(new PropertyValueFactory<>("tipoaula"));
        this.colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        this.colInscritos.setCellValueFactory(new PropertyValueFactory<>("inscritos"));
        this.tbAulas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.aulaSelected=newValue;
            if(this.aulaSelected!=null){
                if(this.aulaSelected.getTipoaula().equals("Individual")){
                    this.btCancelarAula.setDisable(false);
                }else{
                    this.btCancelarAula.setDisable(true);
                }
            }else{
                this.btCancelarAula.setDisable(true);
            }
        });
        this.filtrarTable();
    }
    @FXML
    private void filtrarTable(){
        if(this.aulasFiltradasObservableList!=null){
            this.aulasFiltradasObservableList.clear();
        }
        this.aulasFiltradasObservableList=FXCollections.observableArrayList(ProfessorService.getAulasDate(this.colaborador, this.dpDate.getValue()));
        this.tbAulas.setItems(this.aulasFiltradasObservableList);
    }
    
    @FXML
    private void nextOrPreviousDay(ActionEvent event) throws IOException{
        switch(((Button)event.getSource()).getId()){
            case "btPrevious":
                this.dpDate.setValue(this.dpDate.getValue().plusDays(-1));
               this.filtrarTable();
                break;
            case "btNext":
                this.dpDate.setValue(this.dpDate.getValue().plusDays(1));
               this.filtrarTable();
               break;
        }
    
    }
    
    @FXML 
    private void criarAulaIndividual(ActionEvent event){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FXMLPersonalTrainerAulaIndividual.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Aula Individual");
            stage.setScene(scene);    
            FXMLPersonalTrainerAulaIndividualController controller = fxmlLoader.getController();
            controller.setPersonalTrainer(this.colaborador.getProfessor().getPersonaltrainer());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.btCriarAula.getScene().getWindow());
            stage.showAndWait();
            this.initialize(null, null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void cancelAulaIndividual(){
        AulaIndividualService.deleteAulaIndividual(this.aulaSelected);
        this.aulasFiltradasObservableList.remove(this.aulaSelected);
        this.aulaSelected=null;
        this.btCancelarAula.setDisable(true);
        this.tbAulas.getSelectionModel().clearSelection();
    }
    
}
