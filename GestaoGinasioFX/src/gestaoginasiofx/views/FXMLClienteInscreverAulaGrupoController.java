/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import gestaoginasiofx.FillListView;
import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Inscricao;
import gestaoginasiohibernate.model.InscricaoId;
import gestaoginasiohibernate.model.Sala;
import gestaoginasiohibernate.model.Tipoaula;
import gestaoginasiohibernate.model.Utente;
import projetogestaoginasio.ShowMessage;
import gestaoginasiobll.services.AulaService;
import gestaoginasiobll.services.ContratoService;
import gestaoginasiobll.services.InscricaoService;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLClienteInscreverAulaGrupoController implements Initializable {
    private Utente utente;
    private Contrato contrato;
    private Tipoaula tipoAulaSelected;
    private Sala salaSelected;
    private Aula aulaSelected;
    private LocalDate dataSelected;
    private ObservableList<Aula> observableListAulas;
    private ObservableList<Aula> observableListAulasFiltro;
    
    
    @FXML private Button btLimpaSelecao;
    @FXML private Button btCancelar;
    @FXML private Button btInscrever;
    @FXML private ListView lvTipoAula;
    @FXML private ListView lvSala;
    @FXML private DatePicker dpDate;
    
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
       
        this.lvTipoAula.setCellFactory(FillListView.fillTipoAulaListView(this.lvTipoAula));
        this.lvTipoAula.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.tipoAulaSelected=(Tipoaula)newValue;
            this.filtrarListObservable();
        });
        this.lvSala.setCellFactory(FillListView.fillSalaListView(this.lvSala));
        this.lvSala.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            this.salaSelected=(Sala)newValue;
             this.filtrarListObservable();
        });
        this.dpDate.setValue(LocalDate.now());
        this.fillTable();
        
        
    }  
    
    public Utente getUtente() {
        return utente;
    }    
    
    public void setUtente(Utente utente) {
        this.utente = utente;
        this.contrato=utente.getContratoAtivo();
    }
    
    @FXML
    private void limpaSelecao(){
        this.lvSala.getSelectionModel().clearSelection();
        this.salaSelected=null;
        this.lvTipoAula.getSelectionModel().clearSelection();
        this.tipoAulaSelected=null;
        this.dpDate.setValue(LocalDate.now());
        this.dataSelected=LocalDate.now();
        this.filtrarListObservable();
    }
    
    private void fillTable(){
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
            int vagas=aula.getSala().getNumerovagas()-aula.getInscritos();
            ov=new SimpleStringProperty(String.valueOf(vagas));
            return ov;
        });
        //List aulasList= HibernateGenericLib.executeHQLQueryGetAulas();
        List aulasList= AulaService.getAllAulasToDate();
        this.observableListAulas=FXCollections.observableArrayList(aulasList);
        this.observableListAulasFiltro=FXCollections.observableArrayList(aulasList);
        this.filtrarListObservable();
    }
    
    @FXML 
    private void filtrarListObservable(){
        List<Aula> lista= new ArrayList<>();
        if(!this.observableListAulasFiltro.isEmpty()){
           this.observableListAulasFiltro.clear();
        }
        this.dataSelected=this.dpDate.getValue();
        if(this.dataSelected!=null){
            for(Aula a :this.observableListAulas){
                if(a.getData().toString().equals(this.dataSelected.toString())){
                    if(this.salaSelected!=null && this.tipoAulaSelected!=null){
                        if(a.getSala().equals(this.salaSelected) && a.getTipoaula().equals(this.tipoAulaSelected)){
                            //this.observableListAulasFiltro.add(a);
                            lista.add(a);
                        }
                    }else{
                        if(this.salaSelected!=null){
                            if(a.getSala().equals(this.salaSelected)){
                               // this.observableListAulasFiltro.add(a);
                               lista.add(a);
                            }
                        }
                        if(this.tipoAulaSelected!=null){
                            if(this.tipoAulaSelected.equals(a.getTipoaula())){
                                //this.observableListAulasFiltro.add(a);
                                lista.add(a);
                            }
                        }
                        if(this.salaSelected==null && this.tipoAulaSelected==null){
                            //this.observableListAulasFiltro.add(a);
                            lista.add(a);
                        }
                    }
                }
            }
        }
       if(this.dataSelected.equals(LocalDate.now())){
            for(Aula a :lista){
                if( LocalTime.parse(a.getHora()).isAfter(LocalTime.now())){
                    this.observableListAulasFiltro.add(a);
                }
            }
        }else{
           this.observableListAulasFiltro.setAll(lista);
       }

        this.tbAulas.setItems(this.observableListAulasFiltro);
        this.tbAulas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.aulaSelected=newValue;
        });
    }
    
    
    @FXML 
    private void comeBackInicial(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteInicial.fxml"));
            Parent parent =loader.load();
            FXMLClienteInicialController controller= loader.getController();
            controller.setUtente(this.utente);
            this.utente=null;
            this.contrato=null;
            this.salaSelected=null;
            this.observableListAulas.clear();
            this.observableListAulasFiltro.clear();
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    @FXML
    private void criarInscricaoAulaGrupo(ActionEvent event){
        if(this.aulaSelected!=null){
            if(this.aulaSelected.getInscritos()<this.aulaSelected.getSala().getNumerovagas()){
                if(ShowMessage.showConfirmation("Inscrever na aula", "Tem a certeza que se quer inscrever na aula Selecionada?")){
                    if(!ContratoService.verificarInscricao(this.contrato.getInscricaos(),this.aulaSelected)){
                        Inscricao insc= new Inscricao();
                        InscricaoId id = new InscricaoId();
                        id.setAula(this.aulaSelected.getCodigo());
                        id.setUtente(this.contrato.getIdcontrato());
                        insc.setData(Date.from(Instant.now()));
                        insc.setId(id);
                        insc.setAula(this.aulaSelected);
                        insc.setContrato(this.contrato);
                        this.aulaSelected.setInscritos((byte) (this.aulaSelected.getInscritos()+1));
                        this.aulaSelected.getInscricaos().add(insc);
                        this.contrato.getInscricaos().add(insc);
                        InscricaoService.createInscricao(insc);
                        this.comeBackInicial(event);
                    }else{
                        ShowMessage.showError("Erro na Inscrição!", "Já se encontra Registado na aula selecionada.");
                    }                
                }else{
                    return;
                }
            }else{
                ShowMessage.showError("Inscrever na aula", "Atingido o limite de vagas.");
            }
        }else{
            ShowMessage.showError("Inscrever na aula", "Para se inscrever tem de ter uma aula selecionada.");
        }
    }
    
}
