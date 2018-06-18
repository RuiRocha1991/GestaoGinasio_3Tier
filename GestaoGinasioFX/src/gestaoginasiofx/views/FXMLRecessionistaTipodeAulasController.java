/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import gestaoginasiobll.services.TipoAulaService;
import gestaoginasiofx.Notificacao;
import gestaoginasiohibernate.model.Tipoaula;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import projetogestaoginasio.ShowMessage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXMLRecessionistaTipodeAulasController implements Initializable {
private ObservableList<Tipoaula> observableList;
    private Tipoaula tipoAulaSelected;

    @FXML private TextField txtNome;
    @FXML private TextField txtDescricao;

    @FXML private Button btGravar;
    @FXML private Button btLimparSelecao;
   
    @FXML private TableView<Tipoaula> tbAulas;
    @FXML private TableColumn<Tipoaula, String> colNome;
    @FXML private TableColumn<Tipoaula, String> colDescricao;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        this.colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.observableList=FXCollections.observableArrayList(TipoAulaService.getAllTipoAula());
        this.setTable();
    }    
    
    private void setTable(){
        
        this.tbAulas.setItems(this.observableList);
        this.tbAulas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.tipoAulaSelected=newValue;
            if(this.tipoAulaSelected!=null){
                showTipoAula(newValue);
            }
        });
    }
    
    private void showTipoAula(Tipoaula tipo){
        this.txtNome.textProperty().setValue(tipo.getNome());
        this.txtDescricao.textProperty().setValue(tipo.getDescricao());
    }
    
    @FXML
    private void addTipoAula(){
        if(this.txtNome.getText().length()>0 && this.txtDescricao.getText().length()>0){
            if(this.tipoAulaSelected==null){
                if(ShowMessage.showConfirmation("Adicionar Tipo Aula", "Tem a certeza que pretende adicionar um novo tipo de aula?")){
                    this.observableList.add(TipoAulaService.createTipoAula(this.txtNome.getText(), this.txtDescricao.getText()));
                    Notificacao.successNotification("Tipo de Aula", "Adicionado com sucesso");
                }else{
                    return;
                }
            }else{
                if(ShowMessage.showConfirmation("Atualizar Tipo Aula", "Tem a certeza que pretende atualizar o tipo de aula?")){
                    TipoAulaService.updateTipoAula(this.tipoAulaSelected, this.txtNome.getText(), this.txtDescricao.getText());
                    this.observableList.add(this.observableList.indexOf(this.tipoAulaSelected), tipoAulaSelected);
                    Notificacao.successNotification("Tipo de Aula", "Atualizado com sucesso");
                }else{
                    return;
                }
            }
            this.clearFields();
            this.setTable();
        }else{
            ShowMessage.showError("Campos Vazios", "Para continuar tem de preencher todos os campos.");
        }
    }
    
    @FXML
    private void clearFields(){
        this.txtDescricao.setText("");
        this.txtNome.setText("");
        this.tipoAulaSelected=null;
        this.setTable();
    }
    
    
}
