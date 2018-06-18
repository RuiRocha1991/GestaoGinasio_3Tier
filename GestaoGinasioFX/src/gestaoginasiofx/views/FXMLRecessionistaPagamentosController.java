/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.services.ContratoService;
import gestaoginasiofx.Notificacao;
import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Contrato;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import projetogestaoginasio.ShowMessage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXMLRecessionistaPagamentosController implements Initializable {
    private Contrato contrato;
    private ObservableList<Aulaindividual> observableListAulas;
    
    @FXML private Button btConfirmar;
    @FXML private Button btCancelar;
    
    @FXML private DatePicker dpDate;
    @FXML private TextField txtNome;
    @FXML private TextField txtNIF;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtMensalidade;
    @FXML private TextField txtTotalAula;
    @FXML private TextField txtTotal;
    
    @FXML private TableView<Aulaindividual> tbAulas;
    @FXML private TableColumn<Aulaindividual, Date> colDate;
    @FXML private TableColumn<Aulaindividual, BigDecimal> colValor;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
        this.showDataContrato();
        this.dpDate.setValue(LocalDate.now());
    }
        
    private void showDataContrato(){
        this.txtEmail.textProperty().setValue(this.contrato.getUtente().getEmail());
        this.txtMensalidade.textProperty().setValue(String.valueOf(this.contrato.getValormensalidade()));
        this.txtNIF.textProperty().setValue(String.valueOf(this.contrato.getUtente().getNif()));
        this.txtNome.textProperty().setValue(this.contrato.getUtente().getNome());
        this.txtTelefone.textProperty().setValue(this.contrato.getUtente().getContacto());
        this.initializeTable();
    }
    
    private void initializeTable(){
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        this.colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        List aulasList= new ArrayList<>();
        aulasList.addAll(ContratoService.getAulaIndividualPorPagar(this.contrato));
        this.observableListAulas=FXCollections.observableArrayList(aulasList);
        this.tbAulas.setItems(this.observableListAulas);
        this.txtTotalAula.setText(ContratoService.getTotalAulaIndividual(this.contrato));
        this.txtTotal.setText(String.valueOf(Double.valueOf(this.txtMensalidade.getText())+Double.valueOf(this.txtTotalAula.getText())));
    }
    
    @FXML
    private void confirmarPagamento(){
        try {
            ContratoService.confirmarPagamento(this.contrato, this.txtTotal.getText(), this.dpDate.getValue());
            Notificacao.successNotification("Pagamento", "Confirmado com sucesso.");
            Stage stage = (Stage) this.btConfirmar.getScene().getWindow();
            stage.close();
        } catch (NumericException ex) {
            ShowMessage.showError("Valor Inválido", "Total é inválido");
        }
    }
    
    @FXML
    private void closeWindow(){
        Stage stage = (Stage) this.btCancelar.getScene().getWindow();
        stage.close();
    }
}
