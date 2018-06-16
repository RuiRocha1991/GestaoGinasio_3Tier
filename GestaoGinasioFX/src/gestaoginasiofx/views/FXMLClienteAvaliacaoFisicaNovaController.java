/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import gestaoginasiofx.FillListView;
import gestaoginasiohibernate.model.Avaliacaofisica;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Personaltrainer;
import projetogestaoginasio.ShowMessage;
import gestaoginasiobll.services.AvaliacaoFisicaService;
import gestaoginasiobll.services.PersonalTrainerService;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLClienteAvaliacaoFisicaNovaController implements  Initializable {
    private Contrato contrato;
    private Personaltrainer personalTrainer;
    private LocalDate data;
    private LocalTime hora;
    private ObservableList<LocalTime> horasObservableList;
    @FXML private ListView lvPersonalTrainer;
    @FXML private ListView lvHorasLivres;
    @FXML private DatePicker dpDate;
    @FXML private Pane pane;
    @FXML private TextField txtDescricao;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.lvPersonalTrainer.setCellFactory(FillListView.fillPersonalTrainerListView(this.lvPersonalTrainer));
        this.lvPersonalTrainer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.personalTrainer=(Personaltrainer) newValue;
            this.pane.setDisable(false);
        });
        this.pane.setDisable(true);
    }    

    private void showPersonalTrainer(LocalDate date){
        if(this.personalTrainer!=null){
            List<LocalTime> listaHoras=new ArrayList<>();
            listaHoras=PersonalTrainerService.getHorasDisponiveisDia(this.personalTrainer,date);
            this.horasObservableList=FXCollections.observableList(listaHoras);
            this.lvHorasLivres.setItems(this.horasObservableList);
            this.lvHorasLivres.setCellFactory(new Callback<ListView<LocalTime>, ListCell<LocalTime>>(){
                public ListCell<LocalTime> call(ListView<LocalTime> p) {
                    ListCell<LocalTime> cell = new ListCell<LocalTime>(){
                        @Override
                        protected void updateItem(LocalTime t, boolean bln) {
                            super.updateItem(t, bln);
                            if (t != null) {
                                setText(t.toString());
                            }
                        }
                    };
                    return cell;
                }
            });
            
            this.lvHorasLivres.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.hora=(LocalTime) newValue;
        });
        }
    }
    
    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
    
    @FXML 
    private void comeBackInicial(ActionEvent event){
        try{
            Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("FXMLClienteInicial.fxml"));
            Parent parent =loader.load();
            FXMLClienteInicialController controller= loader.getController();
            controller.setUtente(this.contrato.getUtente());
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel Sair do inicio Sessao");
        }
    }
    
    @FXML 
    private void selecionaData(){
        this.data=this.dpDate.getValue();
        this.showPersonalTrainer(this.dpDate.getValue());
    }
    
    @FXML
    private void addAvaliacaoFisica(ActionEvent event){
        if(ShowMessage.showConfirmation("Pedir nova avaliação física", "Tem a certeza que quer agendar uma nova avaliação física?")){
            if(this.data!=null && !this.txtDescricao.getText().equals("")&& this.hora!=null){
                Avaliacaofisica avali = new Avaliacaofisica();
                avali.setCod(6);
                avali.setPersonaltrainer(this.personalTrainer);
                avali.setContrato(this.contrato);
                avali.setData(Date.valueOf(this.data));
                avali.setHora(this.hora.toString());
                avali.setDescricao(this.txtDescricao.getText());
                this.personalTrainer.getAvaliacaofisicas().add(avali);
                this.contrato.getAvaliacaofisicas().add(avali);
                AvaliacaoFisicaService.createAvaliacaoFisica(avali);
                this.comeBackInicial(event);
            }else{
                if(this.data==null){
                    this.dpDate.setStyle("-fx-background-color: #ff5252;");
                }
                if(this.hora==null){
                    this.lvHorasLivres.setStyle("-fx-background-color: #ff5252;");
                }
                if(this.txtDescricao.getText().equals("")){
                    this.txtDescricao.setStyle("-fx-background-color: #ff5252;");
                }
            }
            
        }else{
            return;
        }
    }
    
    
    
}
