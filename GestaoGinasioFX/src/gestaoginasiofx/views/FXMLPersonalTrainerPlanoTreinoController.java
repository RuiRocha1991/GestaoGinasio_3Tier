/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import enumerados.Dados;
import gestaoginasiobll.exception.DiaExistenteException;
import gestaoginasiobll.services.ColaboradorService;
import gestaoginasiobll.services.PlanoTreinoService;
import gestaoginasiofx.Notificacao;
import gestaoginasiohibernate.model.Linhaplanotreino;
import gestaoginasiohibernate.model.Personaltrainer;
import gestaoginasiohibernate.model.Planotreino;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import projetogestaoginasio.ShowMessage;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLPersonalTrainerPlanoTreinoController implements Initializable {

    private Planotreino planoSelected;
    private Linhaplanotreino linhaSelected;
    private Personaltrainer personalTrainer;
    private String diaSelected;
    
    private ObservableList<Planotreino> observableListPlano;
    private ObservableList<Linhaplanotreino> observableListLinha;
    
    @FXML private TableView<Planotreino> tbPlanoTreino;
    @FXML private TableColumn<Planotreino, String> colUtente;
    @FXML private TableColumn<Planotreino, String> colDescricao;
    
    @FXML private TableView<Linhaplanotreino> tbLinhaPlanoTreino;
    @FXML private TableColumn<Linhaplanotreino, String> colDiaSemana;
    @FXML private TableColumn<Linhaplanotreino, String> colExercicios;
    
    @FXML private ListView lvDiasSemana;
    @FXML private TextArea txtDescricao;
    
    @FXML private Button btAdicionarLinha;
    @FXML private Button btRemoverLinha;
    @FXML private Button btGravar;
    @FXML private Button btCancelar;
    @FXML private Button btLimparSelecao;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.personalTrainer=FXMLInstrutorController.getCt().getProfessor().getPersonaltrainer();
        this.observableListLinha=FXCollections.observableArrayList(new ArrayList<>());
        List<String> lista = new ArrayList<>(Arrays.asList(Dados.getDias()));
        ObservableList<String >List=FXCollections.observableArrayList(lista);
        this.lvDiasSemana.setItems( List);
        this.lvDiasSemana.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(this.txtDescricao.getText().length()>0 && this.diaSelected!=null && this.planoSelected!=null){
                if(ShowMessage.showConfirmation("Adicionar Nova Linha", "Tem a certeza que pretende adicionar nova Linha?")){
                    try {
                        this.observableListLinha.add(PlanoTreinoService.createLinhaPlanoTreino(this.planoSelected, this.diaSelected, this.txtDescricao.getText()));
                        this.fillTableLinhaPlanoTreino();
                        this.diaSelected=null;
                        this.txtDescricao.setText("");
                    } catch (DiaExistenteException ex) {
                        ShowMessage.showError("Dia Inválido", "Dia da já existe no Plano.");
                    }
                }else{
                    this.diaSelected=null;
                    this.txtDescricao.setText("");
                }
            }
            this.diaSelected=(String) newValue;
        });
        this.initializeTables();
    }    

    
    private void initializeTables(){
        this.colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.colUtente.setCellValueFactory((TableColumn.CellDataFeatures<Planotreino, String> param) -> {
            Planotreino plano= param.getValue();
            ObservableValue<String>ov=null;
            ov=new SimpleStringProperty(plano.getContrato().getUtente().getNome());
            return ov;
        });
        this.colDiaSemana.setCellValueFactory(new PropertyValueFactory<>("dia"));
        this.colExercicios.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.fillTablePlanoTreino();
    }
    
    private void fillTablePlanoTreino(){
        this.observableListPlano=FXCollections.observableArrayList(ColaboradorService.getNewsPlanoTreinoPT(this.personalTrainer));
        this.tbPlanoTreino.setItems(this.observableListPlano);
        this.tbPlanoTreino.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.planoSelected=(Planotreino)newValue;
        });
    }
    private void fillTableLinhaPlanoTreino(){
        this.tbLinhaPlanoTreino.setItems(this.observableListLinha);
        this.tbLinhaPlanoTreino.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.linhaSelected=(Linhaplanotreino)newValue;
        });
    }
    
    @FXML 
    private void removeLinhaPlanoTreino(){
        if(this.linhaSelected!=null){
            PlanoTreinoService.removeLinhaPlanoTreino(this.linhaSelected, this.planoSelected);
            this.observableListLinha.remove(this.linhaSelected);
            this.tbLinhaPlanoTreino.getSelectionModel().clearSelection();
            Notificacao.successNotification("Atividade Plano Treino", "Removida Atividade Plano Treino.");
        }
    }
    
    @FXML
    private void addLinhaPlanoTreino(){
         if(ShowMessage.showConfirmation("Adicionar Nova Linha", "Tem a certeza que pretende adicionar nova Linha?")){
            if(this.diaSelected!=null && this.txtDescricao.getText().length()>0&& this.planoSelected!=null){
                try {
                    this.observableListLinha.add(PlanoTreinoService.createLinhaPlanoTreino(this.planoSelected, this.diaSelected, this.txtDescricao.getText()));
                    this.fillTableLinhaPlanoTreino();
                    this.txtDescricao.setText("");
                    this.diaSelected=null;
                    this.lvDiasSemana.getSelectionModel().clearSelection();
                    Notificacao.successNotification("Atividade Plano Treino", "Adicionada Atividade Plano Treino.");
                } catch (DiaExistenteException ex) {
                    ShowMessage.showError("Dia Inválido", "Dia da já existe no Plano.");
                }
            }else{
                ShowMessage.showError("Erro ao adicionar nova Linha", "Dia da semana ou descrição não foram adicionados.");
            }
        }else{
            return;
        }
    }
    
    @FXML 
    private void saveListLinhaPlanoTreino(){
        PlanoTreinoService.saveLinhasPlanoTreino(this.planoSelected);
        Notificacao.successNotification("Atividade Plano Treino", "Atividades adicionadas Plano Treino.");
        this.planoSelected=null;
        this.observableListLinha.clear();
        this.fillTableLinhaPlanoTreino();
        this.fillTablePlanoTreino();
    }
    
    @FXML
    private void clearListLinhaPlanoTreino(){
        this.planoSelected.getLinhaplanotreinos().clear();
        Notificacao.successNotification("Atividade Plano Treino", "Removidas todas as atividades Plano Treino.");
        this.observableListLinha.clear();
        this.fillTableLinhaPlanoTreino();
        this.fillTablePlanoTreino();
    }
    
    
}
