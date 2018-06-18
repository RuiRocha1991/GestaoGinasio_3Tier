/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Inscricao;
import gestaoginasiohibernate.model.Utente;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import gestaoginasiobll.AulaContrato;
import projetogestaoginasio.ShowMessage;
import gestaoginasiobll.services.AulaIndividualService;
import gestaoginasiobll.services.ContratoService;
import gestaoginasiobll.services.InscricaoService;
import gestaoginasiobll.services.UtenteService;
import gestaoginasiofx.Notificacao;
import java.util.Set;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLClienteInicialController implements Initializable {
    private Utente utente;
    private Contrato contrato;
    private List aulasList;
    private ObservableList aulasFiltradasObservableList; 
    private AulaContrato aulaSelected;
    @FXML private TextField txtUltimoPagamento;
    @FXML private TextField txtDataUltimoPagamento;
    @FXML private TextField txtValorDivida;
    @FXML private TextField txtPersonalTrainer;
    @FXML private TextField txtCustoAula;
    @FXML private TextField txtDuracaoAula;
    @FXML private TextField txtNomeCliente;
    @FXML private Button btCloseSession;
    @FXML private Button btInscAulaGrupo;
    @FXML private Button btPlanoTreino;
    @FXML private Button btAvaliacaoFisica;
    @FXML private Button btEditarDados;
    @FXML private Button btCancelarAula;
    
    @FXML private TableView<AulaContrato> tbAulas;
    @FXML private TableColumn<AulaContrato, LocalDate> colDate;
    @FXML private TableColumn<AulaContrato, Integer> colTime;
    @FXML private TableColumn<AulaContrato, Integer> colSala;
    @FXML private TableColumn<AulaContrato, String> colTipoAula; 

    @FXML private RadioButton rbAnteriores;
    @FXML private RadioButton rbProximas;
    @FXML private RadioButton rbHoje;
    @FXML private RadioButton rbTodas;
    @FXML private ToggleGroup toggleGroup;
    
    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.btCancelarAula.setDisable(true);
        this.toggleGroup= new ToggleGroup();
        this.rbAnteriores.setToggleGroup(this.toggleGroup);
        this.rbHoje.setToggleGroup(this.toggleGroup);
        this.rbProximas.setToggleGroup(this.toggleGroup);
        this.rbTodas.setToggleGroup(this.toggleGroup);
        
    }  
    
    public void setUtente(Utente utente){
        this.utente=utente;
        this.setContrato(UtenteService.getContratoAtivo(utente));
        this.txtUltimoPagamento.textProperty().setValue(this.contrato.getMesultimopagamento()+" / "+this.contrato.getAnoultimopagamento());
        this.txtDataUltimoPagamento.textProperty().setValue(ContratoService.consultaDataUltimoPagamento(this.contrato));
        this.txtValorDivida.textProperty().setValue( ContratoService.calculaValorDivida(this.contrato)+"€");
        this.txtNomeCliente.textProperty().setValue(this.contrato.getUtente().getNome());
        
    }
    private void setContrato(Contrato contrato){
        this.contrato=contrato;
        this.aulasList=ContratoService.getAulasContrato(contrato);
        this.aulasFiltradasObservableList=FXCollections.observableArrayList(this.aulasList);
        this.fillTableAulas();
        this.rbTodas.setSelected(true);
    }
    @FXML 
    private void openInscricaoAulaGrupo(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteInscreverAulaGrupo.fxml"));
            Parent parent =loader.load();
            FXMLClienteInscreverAulaGrupoController controller= loader.getController();
            controller.setUtente(this.utente);
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    @FXML 
    private void openPlanoTreino(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClientePlanoTreino.fxml"));
            Parent parent =loader.load();
            FXMLClientePlanoTreinoController controller= loader.getController();
            controller.setContrato(this.contrato);
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    @FXML 
    private void openAvaliacaoFisica(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteAvaliacaoFisica.fxml"));
            Parent parent =loader.load();
            FXMLClienteAvaliacaoFisicaController controller= loader.getController();
            controller.setContrato(this.contrato);
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    @FXML 
    private void openDadosPessoais(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteEditarDados.fxml"));
            Parent parent =loader.load();
            FXMLClienteEditarDadosController controller= loader.getController();
            controller.setContrato(this.contrato);
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
   @FXML
    private void closeSession(ActionEvent event){
        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        try{
            Parent parent =loader.load();
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel");
        }
    }
    
    private void fillTableAulas(){
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        this.colTime.setCellValueFactory(new PropertyValueFactory<>("hora"));
        this.colSala.setCellValueFactory(new PropertyValueFactory<>("sala"));
        this.colTipoAula.setCellValueFactory(new PropertyValueFactory<>("tipoaula"));
        this.tbAulas.setItems(this.aulasFiltradasObservableList);
        this.tbAulas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.aulaSelected=newValue;
            showAulaSelected(newValue);
        });
    }
    
    
    private void showAulaSelected(AulaContrato aula) {
        if(this.aulaSelected!=null){
            this.txtCustoAula.textProperty().setValue(aula.getCusto()+"€");
            this.txtPersonalTrainer.textProperty().setValue(aula.getProfessor());
            this.txtDuracaoAula.textProperty().setValue(aula.getDuracao());
            if(this.aulaSelected.getData().isAfter(LocalDate.now())){
                this.btCancelarAula.setDisable(false);
            }else{
                this.btCancelarAula.setDisable(true);
            }
        }
        
    }
    @FXML
    private void cancelarAula(){
        int selectedIndex=this.tbAulas.getSelectionModel().getSelectedIndex();
        if(selectedIndex!=-1){
            if(ShowMessage.showConfirmation("Eliminar Inscrição", "Tem a certeza que quer eliminar a inscrição?")){
                if(this.aulaSelected.getTipoaula().equals("Individual")){
                    ContratoService.removeAulaIndividual(this.contrato.getAulaindividuals(),this.aulaSelected.getCodigo());
                    Notificacao.successNotification("Aula Individual", "Cancelada Aula Individual.");
                }else{
                    ContratoService.removeInscricao(this.contrato.getInscricaos(),this.aulaSelected.getCodigo());
                    Notificacao.successNotification("Aula de Grupo", "Cancelada Aula de Grupo.");
                }
                this.aulasFiltradasObservableList.remove(selectedIndex);
                this.aulasList.remove(this.aulaSelected);
                this.clearFields();
                this.aulaSelected=null;
            }else{
                return;
            }
        }
    }
    
    private void clearFields(){
        this.btCancelarAula.disableProperty().set(true);
        this.tbAulas.getSelectionModel().clearSelection();
        this.txtCustoAula.textProperty().setValue("");
        this.txtDuracaoAula.textProperty().setValue("");
        this.txtPersonalTrainer.textProperty().setValue("");
    }
    
    @FXML 
    private void rbAnteriorSelected(){
        this.aulasFiltradasObservableList.clear();
        if(this.rbAnteriores.isSelected()){
            this.aulasFiltradasObservableList=FXCollections.observableArrayList(ContratoService.getAulaContratoAnterior(this.aulasList));
            this.fillTableAulas();
            Notificacao.filtrosNotification("Aulas Selecionadas", "Selecionadas Aulas Anteriores.");
        }
    }
    
    @FXML 
    private void rbHojeSelected(){
        this.aulasFiltradasObservableList.clear();
        if(this.rbHoje.isSelected()){
            this.aulasFiltradasObservableList=FXCollections.observableArrayList(ContratoService.getAulaContratoHoje(this.aulasList));
            this.fillTableAulas();
            Notificacao.filtrosNotification("Aulas Selecionadas", "Selecionadas Aulas de Hoje.");
        }
    }
    @FXML 
    private void rbProximasSelected(){
        this.aulasFiltradasObservableList.clear();
        if(this.rbProximas.isSelected()){
            this.aulasFiltradasObservableList=FXCollections.observableArrayList(ContratoService.getAulaContratoProximas(this.aulasList));
            this.fillTableAulas();
            Notificacao.filtrosNotification("Aulas Selecionadas", "Selecionadas Próximas Aulas.");
        }
    }
    @FXML 
    private void rbTodasSelected(){
        if(this.rbTodas.isSelected()){
            this.aulasFiltradasObservableList=FXCollections.observableArrayList(this.aulasList);
            Notificacao.filtrosNotification("Aulas Selecionadas", "Todas as Aulas Visiveis.");
        }
    }
}
