/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx;

import gestaoginasiohibernate.model.Categoriaequipamento;
import gestaoginasiohibernate.model.Espaco;
import gestaoginasiohibernate.model.Linhaplanotreino;
import gestaoginasiohibernate.model.Personaltrainer;
import gestaoginasiohibernate.model.Planotreino;
import gestaoginasiohibernate.model.Sala;
import gestaoginasiohibernate.model.Tipoaula;
import hibernate.HibernateGenericLib;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


/**
 *
 * @author Rui
 */
public class FillListView {
    
    public static Callback<ListView<Tipoaula>, ListCell<Tipoaula>> fillTipoAulaListView(ListView list){
        ObservableList<Tipoaula > tipoAulaObservableList;
        String hql="From Tipoaula";
        List<Tipoaula> tipoAulaLista = (List<Tipoaula>)HibernateGenericLib.executeHQLQuery(hql);
        tipoAulaObservableList=FXCollections.observableList(tipoAulaLista);
        list.setItems(tipoAulaObservableList);
        return(new Callback<ListView<Tipoaula>, ListCell<Tipoaula>>(){
            public ListCell<Tipoaula> call(ListView<Tipoaula> p) {
                ListCell<Tipoaula> cell = new ListCell<Tipoaula>(){
                    @Override
                    protected void updateItem(Tipoaula t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getNome());
                        }
                    }
                };
                return cell;
            }
        });
    }
    
    
    public static Callback<ListView<Sala>, ListCell<Sala>> fillSalaListView(ListView list){
        ObservableList<Sala> salaObservableList;
        String hql="From Sala";
        List<Sala> salaLista = (List<Sala>)HibernateGenericLib.executeHQLQuery(hql);
        salaObservableList=FXCollections.observableList(salaLista);
        list.setItems(salaObservableList);
        return(new Callback<ListView<Sala>, ListCell<Sala>>(){
            public ListCell<Sala> call(ListView<Sala> p) {
                ListCell<Sala> cell = new ListCell<Sala>(){
                    @Override
                    protected void updateItem(Sala t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getEspaco().getDescricao());
                        }
                    }
                };
                return cell;
            }
        });
    }
    
    public static Callback<ListView<Personaltrainer>, ListCell<Personaltrainer>> fillPersonalTrainerListView(ListView list){
        ObservableList<Personaltrainer> ptObservableList;
        String hql="from Personaltrainer where precohora>0";
        List<Personaltrainer> ptLista = (List<Personaltrainer>)HibernateGenericLib.executeHQLQuery(hql);
        ptObservableList=FXCollections.observableList(ptLista);
        list.setItems(ptObservableList);
        return(new Callback<ListView<Personaltrainer>, ListCell<Personaltrainer>>(){
            public ListCell<Personaltrainer> call(ListView<Personaltrainer> p) {
                ListCell<Personaltrainer> cell = new ListCell<Personaltrainer>(){
                    @Override
                    protected void updateItem(Personaltrainer t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getProfessor().getColaborador().getNome());
                        }
                    }
                };
                return cell;
            }
        });
    }
    
    public static Callback<ListView<Categoriaequipamento>, ListCell<Categoriaequipamento>> fillCategoriasEquiListView(ListView list){
        ObservableList<Categoriaequipamento> categoriaObservableList;
        String hql="from Categoriaequipamento";
        List<Categoriaequipamento> lista = (List<Categoriaequipamento>)HibernateGenericLib.executeHQLQuery(hql);
        categoriaObservableList=FXCollections.observableList(lista);
        list.setItems(categoriaObservableList);
        return(new Callback<ListView<Categoriaequipamento>, ListCell<Categoriaequipamento>>(){
            public ListCell<Categoriaequipamento> call(ListView<Categoriaequipamento> p) {
                ListCell<Categoriaequipamento> cell = new ListCell<Categoriaequipamento>(){
                    @Override
                    protected void updateItem(Categoriaequipamento t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getDesignacao());
                        }
                    }
                };
                return cell;
            }
        });
    }
    
    public static Callback<ListView<Espaco>, ListCell<Espaco>> fillEspacoListView(ListView list){
        ObservableList<Espaco> espacoObservableList;
        String hql="from Espaco";
        List<Espaco> lista = (List<Espaco>)HibernateGenericLib.executeHQLQuery(hql);
        espacoObservableList=FXCollections.observableList(lista);
        list.setItems(espacoObservableList);
        return(new Callback<ListView<Espaco>, ListCell<Espaco>>(){
            public ListCell<Espaco> call(ListView<Espaco> p) {
                ListCell<Espaco> cell = new ListCell<Espaco>(){
                    @Override
                    protected void updateItem(Espaco t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getDescricao());
                        }
                    }
                };
                return cell;
            }
        });
    }
    
    public static Callback<ListView<Planotreino>, ListCell<Planotreino>> fillPlanoTreinoListView(ListView list, ObservableList<Planotreino> observable){
        return(new Callback<ListView<Planotreino>, ListCell<Planotreino>>(){
            public ListCell<Planotreino> call(ListView<Planotreino> p) {
                ListCell<Planotreino> cell = new ListCell<Planotreino>(){
                    @Override
                    protected void updateItem(Planotreino t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getDescricao());
                        }
                    }
                };
                return cell;
            }
        });
    }
    
     public static Callback<ListView<Linhaplanotreino>, ListCell<Linhaplanotreino>> fillLinhaPlanoTreinoListView(ListView list, ObservableList<Linhaplanotreino> observable){
        return(new Callback<ListView<Linhaplanotreino>, ListCell<Linhaplanotreino>>(){
            public ListCell<Linhaplanotreino> call(ListView<Linhaplanotreino> p) {
                ListCell<Linhaplanotreino> cell = new ListCell<Linhaplanotreino>(){
                    @Override
                    protected void updateItem(Linhaplanotreino t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getDia());
                        }
                    }
                };
                return cell;
            }
        });
    }
    
 
    
}
