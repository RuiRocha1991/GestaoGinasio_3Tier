/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.services.AvaliacaoFisicaService;
import gestaoginasiobll.services.ColaboradorService;
import gestaoginasiofx.Notificacao;
import gestaoginasiohibernate.model.Avaliacaofisica;
import gestaoginasiohibernate.model.Personaltrainer;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class FXMLPersonalTrainerAvaliacaoFisicaController implements Initializable {

    private Personaltrainer personalTrainer;
    private ObservableList<Avaliacaofisica> observableListAvaliacao;
    private Avaliacaofisica avaliacaoSelected;
    
    @FXML private TableView<Avaliacaofisica> tbAvaliacaoFisica;
    @FXML private TableColumn<Avaliacaofisica, String> colUtente;
    @FXML private TableColumn<Avaliacaofisica, String> colDescricao;
    @FXML private TableColumn<Avaliacaofisica, Date> colDate;
    @FXML private TableColumn<Avaliacaofisica, String> colTime;
    
    @FXML private TextField txtUtente;
    @FXML private TextField txtAltura;
    @FXML private TextField txtPeso;
    @FXML private TextField txtMassaGorda;
    @FXML private TextField txtDCT;
    @FXML private TextField txtDCS;
    @FXML private TextField txtDCA;
    @FXML private TextField txtDCB;
    @FXML private TextField txtDCAM;
    @FXML private TextField txtDCSI;
    @FXML private TextField txtDCTO;
    @FXML private TextField txtDCC;
    @FXML private TextField txtDCPM;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.personalTrainer=FXMLInstrutorController.getCt().getProfessor().getPersonaltrainer();
        this.initializeTables();
    }    
    
    private void initializeTables(){
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colUtente.setCellValueFactory((TableColumn.CellDataFeatures<Avaliacaofisica, String> param) -> {
            Avaliacaofisica av= param.getValue();
            ObservableValue<String>ov=null;
            ov=new SimpleStringProperty(av.getContrato().getUtente().getNome());
            return ov;
        });
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        this.colTime.setCellValueFactory(new PropertyValueFactory<>("hora"));
        this.fillTableAvaliacao();
    }
    
    private void fillTableAvaliacao(){
        this.observableListAvaliacao=FXCollections.observableArrayList(ColaboradorService.getNewsAvaliacaoFisicaPT(this.personalTrainer));
        this.tbAvaliacaoFisica.setItems(this.observableListAvaliacao);
        this.tbAvaliacaoFisica.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.avaliacaoSelected=(Avaliacaofisica)newValue;
            if(this.avaliacaoSelected!=null){
                this.showAvaliacaoFisica();
            }
        });
    }
    
    private void showAvaliacaoFisica(){
        this.txtUtente.textProperty().setValue(this.avaliacaoSelected.getContrato().getUtente().getNome());
    }
    
    @FXML
    private void saveAvaliacaoFisica(){
        if(ShowMessage.showConfirmation("Concluir Avaliação física", "Tem a certeza que pretende concluir avaliação física?")){
            if(this.avaliacaoSelected!=null &&this.txtAltura.getText().length()>0 && this.txtPeso.getText().length()>0&&
                    this.txtMassaGorda.getText().length()>0&&this.txtDCT.getText().length()>0&&this.txtDCS.getText().length()>0&&
                    this.txtDCA.getText().length()>0&&this.txtDCB.getText().length()>0&&this.txtDCAM.getText().length()>0&&
                    this.txtDCSI.getText().length()>0&&this.txtDCTO.getText().length()>0&&this.txtDCC.getText().length()>0&&
                    this.txtDCPM.getText().length()>0){
                try {
                    AvaliacaoFisicaService.updateAvaliacaoFisica(avaliacaoSelected, this.txtMassaGorda.getText(), this.txtPeso.getText(), this.txtAltura.getText()
                            , this.txtDCT.getText(), this.txtDCS.getText(), this.txtDCB.getText(), this.txtDCAM.getText()
                            , this.txtDCSI.getText(), this.txtDCTO.getText(), this.txtDCC.getText(), this.txtDCA.getText()
                            , this.txtDCPM.getText());
                    this.clearFields();
                    Notificacao.successNotification("Avaliação Física", "Atualizada Avaliação Física.");
                } catch (NumericException ex) {
                    ShowMessage.showError("Erro Inserir", "Altura inválida");
                }
            }else{
                this.verifyFillEmpty();
            }
        }else{
            return;
        }
    }
    
    private void verifyFillEmpty(){
        if(this.avaliacaoSelected==null)
            ShowMessage.showError("Utente não selecionado", "Para continuar tem de selecionar um utente.");
        if(this.txtAltura.getText().length()<=0)
            this.txtAltura.setStyle("-fx-background-color: #ff5252;");
        if(this.txtPeso.getText().length()<=0)
            this.txtPeso.setStyle("-fx-background-color: #ff5252;");
        if(this.txtMassaGorda.getText().length()<=0)
            this.txtMassaGorda.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCT.getText().length()<=0)
            this.txtDCT.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCS.getText().length()<=0)
            this.txtDCS.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCA.getText().length()<=0)
            this.txtDCA.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCB.getText().length()<=0)
            this.txtDCB.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCAM.getText().length()<=0)
            this.txtDCAM.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCSI.getText().length()<=0)
            this.txtDCSI.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCTO.getText().length()<=0)
            this.txtDCTO.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCC.getText().length()<=0)
            this.txtDCC.setStyle("-fx-background-color: #ff5252;");
        if(this.txtDCPM.getText().length()<=0)
            this.txtDCPM.setStyle("-fx-background-color: #ff5252;");
    }
    
    @FXML
    private void clearFields(){
        this.avaliacaoSelected=null;
        this.txtUtente.setText("");
        this.txtAltura.setText("");
        this.txtPeso.setText("");
        this.txtMassaGorda.setText("");
        this.txtDCT.setText("");
        this.txtDCS.setText("");
        this.txtDCA.setText("");
        this.txtDCB.setText("");
        this.txtDCAM.setText("");
        this.txtDCSI.setText("");
        this.txtDCTO.setText("");
        this.txtDCC.setText("");
        this.txtDCPM.setText("");
        this.tbAvaliacaoFisica.getSelectionModel().clearSelection();
        this.fillTableAvaliacao();
    }
    
    @FXML
    private void clearBackground(){
            this.txtAltura.setStyle("-fx-background-color: #fffff;");
            this.txtPeso.setStyle("-fx-background-color: #fffff;");
            this.txtMassaGorda.setStyle("-fx-background-color: #fffff;");
            this.txtDCT.setStyle("-fx-background-color: #fffff;");
            this.txtDCS.setStyle("-fx-background-color: #fffff;");
            this.txtDCA.setStyle("-fx-background-color: #fffff;");
            this.txtDCB.setStyle("-fx-background-color: #fffff;");
            this.txtDCAM.setStyle("-fx-background-color: #fffff;");
            this.txtDCSI.setStyle("-fx-background-color: #fffff;");
            this.txtDCTO.setStyle("-fx-background-color: #fffff;");
            this.txtDCC.setStyle("-fx-background-color: #fffff;");
            this.txtDCPM.setStyle("-fx-background-color: #fffff;");
    }
}
