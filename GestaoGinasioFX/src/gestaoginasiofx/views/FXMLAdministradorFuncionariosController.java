/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import enumerados.EnumTipoFuncionario;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import projetogestaoginasio.ConvertType;
import gestaoginasiofx.FillComboBox;
import gestaoginasiohibernate.model.Colaborador;
import gestaoginasiohibernate.model.Personaltrainer;
import gestaoginasiohibernate.model.Professor;
import projetogestaoginasio.ShowMessage;
import services.ColaboradorService;
import services.exception.FieldsEmptyException;
import services.exception.NumericException;
import services.exception.PasswordInvalidException;


/**
 * FXML Controller class
 *
 * @author Rui
 */
public class FXMLAdministradorFuncionariosController implements Initializable {
    @FXML private TextField txtNome;
    @FXML private TextField txtUtilizador;
    @FXML private TextField txtPrecoHora;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmaSenha;
    @FXML private ComboBox cbTipoFuncionario;    
    @FXML private Button btGravar;
    @FXML private Button btProcura;
    @FXML private TextField txtProcura;

    @FXML TableView<Colaborador> tblColaborador;
    @FXML private TableColumn<Colaborador, String> colNome;
    @FXML private TableColumn<Colaborador, String> colUtilizador;
    @FXML private TableColumn<Colaborador, Integer> colPreco;
    @FXML private TableColumn<Colaborador, String> colTipoFuncionario;
 
    private ObservableList<Colaborador> colaboradorObservableList;
    private ObservableList<Colaborador> colaboradorObservableListFiltro;
    private Colaborador selectedColaborador;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.startPane();
    }   
    
    private void startPane(){
        this.cbTipoFuncionario.setItems(FillComboBox.fillTipoFuncionarioComboBox());
        this.initializaTable();
    }
    
    private void initializaTable() {
        this.colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.colUtilizador.setCellValueFactory(new PropertyValueFactory<>("utilizador"));
        this.colPreco.setCellValueFactory(new PropertyValueFactory<>("precohora"));
        this.colTipoFuncionario.setCellValueFactory(new PropertyValueFactory<>("tipofuncionario"));
        List<Colaborador> listColaborador= ColaboradorService.getAllColaboradores();
        this.colaboradorObservableList=FXCollections.observableArrayList(listColaborador);
        this.colaboradorObservableListFiltro=FXCollections.observableArrayList(listColaborador);
        this.setTableList();
    }
    
    private void setTableList(){
        this.tblColaborador.setItems(this.colaboradorObservableListFiltro);
        this.tblColaborador.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedColaborador=newValue;
            this.clearFieldsSelecion();
            showColaborador(newValue);
        });
    }
    
    private void showColaborador(Colaborador colaborador) {
        if(colaborador !=null){
            this.txtNome.textProperty().setValue(colaborador.getNome());
            this.txtUtilizador.textProperty().setValue(colaborador.getUtilizador());
            this.cbTipoFuncionario.setValue(colaborador.getTipofuncionario());
            this.txtSenha.textProperty().setValue(colaborador.getSenha());
            if(colaborador.getProfessor()!=null &&colaborador.getProfessor().getPersonaltrainer()!=null)
                this.txtPrecoHora.textProperty().setValue(String.valueOf(colaborador.getProfessor().getPersonaltrainer().getPrecohora()));
        }
    }
    
    @FXML
    private void clearFieldsSelecion(){
        this.txtNome.textProperty().setValue("");
        this.txtUtilizador.textProperty().setValue("");
        this.txtSenha.textProperty().setValue("");
        this.txtConfirmaSenha.textProperty().setValue("");
        this.cbTipoFuncionario.getSelectionModel().clearSelection();
        this.txtPrecoHora.textProperty().setValue("");
        
    }
    
    @FXML
    private void clearFields(){
        this.txtNome.textProperty().setValue("");
        this.txtUtilizador.textProperty().setValue("");
        this.txtSenha.textProperty().setValue("");
        this.txtConfirmaSenha.textProperty().setValue("");
        this.cbTipoFuncionario.getSelectionModel().clearSelection();
        this.txtPrecoHora.textProperty().setValue("");
        this.selectedColaborador=null;
        this.tblColaborador.getSelectionModel().clearSelection();
    }
    
    
    private boolean verificaUtilizador(){
        String user=this.txtUtilizador.getText().toLowerCase();
        for(Colaborador c:this.colaboradorObservableList){
            if(user.equals(c.getUtilizador().toLowerCase())){
                return true;
            }
        }
        return false;
    }
    
    @FXML 
    private void updateData(){
        if(ShowMessage.showConfirmation("Confirmação de alteração", "Tem a certeza que pretende salvar?")){
            try{
                if(this.selectedColaborador == null){
                    if(!this.txtNome.getText().equals("")&& !this.txtUtilizador.getText().equals("") 
                            &&!this.txtSenha.getText().equals("")&& !this.txtConfirmaSenha.getText().equals("") 
                            && !this.cbTipoFuncionario.getValue().equals("")){
                        if(!this.verificaUtilizador()){
                            if(this.txtSenha.getText().equals(this.txtConfirmaSenha.getText())){
                                if(this.cbTipoFuncionario.getValue().equals(EnumTipoFuncionario.PERSONALTRAINER)){
                                    if(!this.txtPrecoHora.getText().equals(""))
                                        this.addPersonalTrainer();
                                    else
                                        throw new FieldsEmptyException();
                                }else{
                                    if(this.cbTipoFuncionario.getValue().equals(EnumTipoFuncionario.PROFESSOR))
                                        this.addProfessor();
                                    else
                                        this.addOthers();
                                }
                            }else
                                throw new PasswordInvalidException();
                        }else{
                            ShowMessage.showError("Utilizador já existe", "O nome de utilizador já existe.");
                        }
                    }else
                        throw new FieldsEmptyException();
                }else{
                        try{
                            this.updateDataColaborador();
                        }catch(NumericException e){
                            ShowMessage.showError("Erro campo Preço Hora", "O texto no campo Preço Hora tem de ser numérico");
                        }catch(PasswordInvalidException e){
                            ShowMessage.showError("Erro campo Senha", "Campo Senha e Confirmar Senha inválidos");
                        }
                        this.clearFields();
                    }
            }catch(NumericException e){
                ShowMessage.showError("Erro campo Preço Hora", "O texto no campo Preço Hora tem de ser numérico");
                this.txtPrecoHora.setStyle("-fx-background-color: #ff5252; -fx-border-color: #757575");
            }catch(FieldsEmptyException e){
                ShowMessage.showError("Erro campos vazio", "Contem campos vazios");
                this.checkEmptyFields();
            }catch (PasswordInvalidException e){
                ShowMessage.showError("Erro campo Senha", "Campo Senha e Confirmar Senha inválidos");
            }
        }else
            return;
    }
    
    private void addPersonalTrainer() throws NumericException{
        Colaborador newColaborador = new Colaborador();
        Professor newProfessor = new Professor();
        Personaltrainer newpt= new Personaltrainer();
        //newColaborador.setAll(this.txtUtilizador.getText(),this.txtNome.getText(),
                 // this.txtSenha.getText(), this.cbTipoFuncionario.getValue().toString());
        newProfessor.setColaborador(newColaborador);
        newProfessor.setCodigo(this.txtUtilizador.getText());
        newpt.setCodigo(this.txtUtilizador.getText());
        newpt.setPrecohora(ConvertType.stringToBigDecimal(this.txtPrecoHora.getText()));
        newpt.setProfessor(newProfessor);
        newProfessor.setPersonaltrainer(newpt);
        newColaborador.setProfessor(newProfessor);
        ColaboradorService.createPersonalTrainer(newColaborador);
        this.colaboradorObservableList.add(newColaborador);
       // this.colaboradorObservableListFiltro.add(newColaborador);
        this.clearFields();
    }
    
    private void addProfessor(){
        Colaborador newColaborador = new Colaborador();
        Professor newProfessor = new Professor();
        newProfessor.setColaborador(newColaborador);
        newProfessor.setCodigo(this.txtUtilizador.getText());
        newColaborador.setProfessor(newProfessor);
       // newColaborador.setAll(this.txtUtilizador.getText(),this.txtNome.getText(),
             //     this.txtSenha.getText(), this.cbTipoFuncionario.getValue().toString());
        ColaboradorService.createProfessor(newColaborador);
        this.colaboradorObservableList.add(newColaborador);
//        this.colaboradorObservableListFiltro.add(newColaborador);
        this.clearFields();
    }
    
    
    private void addOthers(){
        Colaborador newColaborador = new Colaborador();
        //newColaborador.setAll(this.txtUtilizador.getText(),this.txtNome.getText(),
                 // this.txtSenha.getText(), this.cbTipoFuncionario.getValue().toString());
        ColaboradorService.createColaborador(newColaborador);
        this.colaboradorObservableList.add(newColaborador);
        //this.colaboradorObservableListFiltro.add(newColaborador);
        this.clearFields();
    }
    
    private void updateOthers() throws NumericException{
        if(this.selectedColaborador.getProfessor()!=null && this.selectedColaborador.getProfessor().getPersonaltrainer()!=null){
            this.selectedColaborador.getProfessor().getPersonaltrainer().setPrecohora(ConvertType.stringToBigDecimal("0"));
            ColaboradorService.updateObjectPersonalTrainer(this.selectedColaborador.getProfessor().getPersonaltrainer());
        }
        ColaboradorService.updateObjectColaborador(selectedColaborador);
    }
    
    private void updateProfessor() throws NumericException{
        if(this.selectedColaborador.getProfessor()!=null && this.selectedColaborador.getProfessor().getPersonaltrainer()!=null){
            this.selectedColaborador.getProfessor().getPersonaltrainer().setPrecohora(ConvertType.stringToBigDecimal("0"));
            ColaboradorService.updateObjectPersonalTrainer(this.selectedColaborador.getProfessor().getPersonaltrainer());
        }
        if(this.selectedColaborador.getProfessor()==null){
            Professor pf=new Professor();
            pf.setColaborador(this.selectedColaborador);
            this.selectedColaborador.setProfessor(pf);
            ColaboradorService.updateObjectProfessor(this.selectedColaborador.getProfessor());
        }
        ColaboradorService.updateObjectColaborador(this.selectedColaborador);
    }
    
    private void updatePersonalTrainer() throws NumericException{
        if(this.selectedColaborador.getProfessor()!=null){
                    if(this.selectedColaborador.getProfessor().getPersonaltrainer()!=null){
                        if(!this.txtPrecoHora.getText().equals("")) {
                            this.selectedColaborador.getProfessor().getPersonaltrainer()
                                    .setPrecohora(ConvertType.stringToBigDecimal(this.txtPrecoHora.getText()));
                            ColaboradorService.updateObjectColaborador(this.selectedColaborador);
                            ColaboradorService.updateObjectPersonalTrainer(this.selectedColaborador.getProfessor().getPersonaltrainer());
                        }else{
                            throw new NumericException();
                        }
                    }else{
                        Personaltrainer pt = new Personaltrainer();
                        if(!this.txtPrecoHora.getText().equals("")) {
                            pt.setPrecohora(ConvertType.stringToBigDecimal(this.txtPrecoHora.getText()));
                            this.selectedColaborador.getProfessor().setPersonaltrainer(pt);
                            pt.setProfessor(this.selectedColaborador.getProfessor());
                            ColaboradorService.updateObjectColaborador(this.selectedColaborador);
                            ColaboradorService.updateObjectProfessor(this.selectedColaborador.getProfessor());
                            ColaboradorService.updateObjectPersonalTrainer(pt);
                        }else{
                            throw new NumericException();
                        }
                    }
                }else{
                    Personaltrainer pt = new Personaltrainer();
                    Professor pf = new Professor();
                    if(!this.txtPrecoHora.getText().equals("")) {
                        pt.setPrecohora(ConvertType.stringToBigDecimal(this.txtPrecoHora.getText()));
                        pf.setPersonaltrainer(pt);
                        pt.setProfessor(pf);
                        this.selectedColaborador.setProfessor(pf);
                        pf.setColaborador(this.selectedColaborador);
                        ColaboradorService.updateObjectColaborador(this.selectedColaborador);
                        ColaboradorService.updateObjectProfessor(pf);
                        ColaboradorService.updateObjectPersonalTrainer(pt);
                    }else{
                        throw new NumericException();
                    }
                }
    }
    
    private void updateDataColaborador() throws NumericException, PasswordInvalidException{
         this.selectedColaborador.setTipofuncionario(this.cbTipoFuncionario.getValue().toString());
            if(!this.txtSenha.getText().equals(this.selectedColaborador.getSenha())){
                if(this.txtSenha.getText().equals(this.txtConfirmaSenha.getText())){
                    this.selectedColaborador.setSenha(this.txtSenha.getText());
                }else{
                    throw new PasswordInvalidException();
                }
            }
        if(this.cbTipoFuncionario.getValue().equals(EnumTipoFuncionario.ADMINISTRADOR)||
                this.cbTipoFuncionario.getValue().equals(EnumTipoFuncionario.RECECIONISTA)||
                this.cbTipoFuncionario.getValue().equals(EnumTipoFuncionario.INSTRUTOR)){
            this.updateOthers();
        }else{
            if(this.cbTipoFuncionario.getValue().equals(EnumTipoFuncionario.PROFESSOR)){
                this.updateProfessor();
            }else{
                    this.updatePersonalTrainer();
            }
        }
        this.colaboradorObservableList.set(this.colaboradorObservableList.indexOf(this.selectedColaborador),this.selectedColaborador);
        this.colaboradorObservableListFiltro.set(this.colaboradorObservableListFiltro.indexOf(this.selectedColaborador),this.selectedColaborador);
        this.clearFields();
    }
    
    
    
    @FXML private void changeBackground(){
        this.txtPrecoHora.setStyle("-fx-background-color: #fffff;");
        this.txtConfirmaSenha.setStyle("-fx-background-color: #fffff;");
        this.txtNome.setStyle("-fx-background-color: #fffff;");
        this.txtSenha.setStyle("-fx-background-color: #fffff;");
        this.txtUtilizador.setStyle("-fx-background-color: #fffff;");
        this.cbTipoFuncionario.setStyle("-fx-background-color: #fffff;");
    }
    
    private void checkEmptyFields(){
        if(this.txtConfirmaSenha.getText().equals("")){
             this.txtConfirmaSenha.setStyle("-fx-background-color: #ff5252;");
        }
        if(this.txtNome.getText().equals("")){
             this.txtNome.setStyle("-fx-background-color: #ff5252;");
        }
        if(this.txtPrecoHora.getText().equals("")){
             this.txtPrecoHora.setStyle("-fx-background-color: #ff5252;");
        }
        if(this.txtSenha.getText().equals("")){
             this.txtSenha.setStyle("-fx-background-color: #ff5252;");
        }
        if(this.txtUtilizador.getText().equals("")){
             this.txtUtilizador.setStyle("-fx-background-color: #ff5252;");
        }
    }
    
    @FXML 
    private void pesquisa(){
        if(this.txtProcura.getText().length()>0){
            this.colaboradorObservableListFiltro.clear();
            for(Colaborador c: this.colaboradorObservableList){
                String nomeLista= c.getNome().toLowerCase();
                String procura= this.txtProcura.getText().toLowerCase();
                String userLista= c.getUtilizador().toLowerCase();
                String tipoFuncLista=c.getTipofuncionario().toLowerCase();
                if(nomeLista.contains(procura) ||userLista.contains(procura)||tipoFuncLista.contains(procura) ){
                    this.colaboradorObservableListFiltro.add(c);
                }
            }
            this.setTableList();
        }else{
            this.colaboradorObservableListFiltro.setAll(this.colaboradorObservableList);
            this.setTableList();
        }
    }
    
}
