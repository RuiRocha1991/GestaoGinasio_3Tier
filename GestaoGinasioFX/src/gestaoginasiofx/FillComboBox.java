/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx;

import enumerados.Dados;
import hibernate.HibernateGenericLib;
import enumerados.EnumTipoEspaco;
import enumerados.EnumTipoFuncionario;
import gestaoginasiohibernate.model.Categoriaequipamento;
import gestaoginasiohibernate.model.Espaco;
import gestaoginasiohibernate.model.Professor;
import gestaoginasiohibernate.model.Sala;
import gestaoginasiohibernate.model.Tipoaula;
import gestaoginasiohibernate.model.Tipocontrato;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;


/**
 *
 * @author Rui
 */
public class FillComboBox {
    
    public static StringConverter<Tipoaula> fillTipoAulaComboBox(ComboBox combo){
        ObservableList<Tipoaula> tipoAulaObservableList;
        String hql="From Tipoaula";
        List<Tipoaula> tipoAulaLista = (List<Tipoaula>)HibernateGenericLib.executeHQLQuery(hql);
        tipoAulaObservableList = FXCollections.observableArrayList(tipoAulaLista);
        combo.setItems(tipoAulaObservableList);
        return (new StringConverter<Tipoaula>()
        {
            @Override
            public String toString(Tipoaula object) {
                return object.getNome();
            }

            @Override
            public Tipoaula fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }

    public static StringConverter<LocalTime> fillHorasComboBox(ComboBox combo){
        ObservableList<LocalTime> horasObservableList;
        horasObservableList = FXCollections.observableArrayList(Dados.getHoras());
        combo.setItems(horasObservableList);
        return (new StringConverter<LocalTime>()
        {
            @Override
            public String toString(LocalTime object) {
                return object.toString();
            }

            @Override
            public LocalTime fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
    }
    
    public static StringConverter<Sala> fillSalaComboBox(ComboBox combo){
        ObservableList<Sala> salaObservableList;
        String hql="From Sala";
        List<Sala> salaLista = (List<Sala>)HibernateGenericLib.executeHQLQuery(hql);
        salaObservableList = FXCollections.observableArrayList(salaLista);
        combo.setItems(salaObservableList);
        return (new StringConverter<Sala>()
        {
            @Override
            public String toString(Sala object) {
                return object.getEspaco().getDescricao();
            }

            @Override
            public Sala fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }
    
    public static StringConverter<Sala> fillSalaCreateAulaComboBox(ComboBox combo, List salaLista){
        ObservableList<Sala> salaObservableList;
        salaObservableList = FXCollections.observableArrayList(salaLista);
        combo.setItems(salaObservableList);
        return (new StringConverter<Sala>()
        {
            @Override
            public String toString(Sala object) {
                return object.getEspaco().getDescricao();
            }

            @Override
            public Sala fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }
    
    public static StringConverter<Professor> fillProfessorComboBox(ComboBox combo){
        ObservableList<Professor> professorObservableList;
        String hql="From Professor";
        List<Professor> lista = (List<Professor>)HibernateGenericLib.executeHQLQuery(hql);
        professorObservableList = FXCollections.observableArrayList(lista);
        combo.setItems(professorObservableList);
        return (new StringConverter<Professor>()
        {
            @Override
            public String toString(Professor object) {
                return object.getColaborador().getNome();
            }

            @Override
            public Professor fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }
    
    public static StringConverter<Professor> fillProfessorCreateAulaComboBox(ComboBox combo, List lista){
        ObservableList<Professor> professorObservableList;
        professorObservableList = FXCollections.observableArrayList(lista);
        combo.setItems(professorObservableList);
        return (new StringConverter<Professor>()
        {
            @Override
            public String toString(Professor object) {
                return object.getColaborador().getNome();
            }

            @Override
            public Professor fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }
    
    public static StringConverter<Tipocontrato> fillTipoContratoComboBox(ComboBox combo){
        ObservableList<Tipocontrato> tipoContratoObservableList;
        String hql="From Tipocontrato where ativo=1 order by valor ";
        List<Tipocontrato> lista = (List<Tipocontrato>)HibernateGenericLib.executeHQLQuery(hql);
        tipoContratoObservableList = FXCollections.observableArrayList(lista);
        combo.setItems(tipoContratoObservableList);
        return (new StringConverter<Tipocontrato>()
        {
            @Override
            public String toString(Tipocontrato object) {
                return object.getDescricao();
            }

            @Override
            public Tipocontrato fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }
    
    public static StringConverter<Espaco> FillEspacoComboBox(ComboBox combo){
        ObservableList<Espaco> observableList;
        String hql = "from Espaco";
        List<Espaco> lista = (List<Espaco>)HibernateGenericLib.executeHQLQuery(hql);
        observableList = FXCollections.observableArrayList(lista);
        combo.setItems(observableList);
        return (new StringConverter<Espaco>()
        {
            @Override
            public String toString(Espaco object) {
                return object.getDescricao();
            }

            @Override
            public Espaco fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }
        
     public static StringConverter<Categoriaequipamento> FillCategoriasEquipamentosComboBox(ComboBox combo){
        ObservableList<Categoriaequipamento> observableList;
        String hql = "from Categoriaequipamento";
        List<Categoriaequipamento> lista = (List<Categoriaequipamento>)HibernateGenericLib.executeHQLQuery(hql);
        observableList = FXCollections.observableArrayList(lista);
        combo.setItems(observableList);
        return (new StringConverter<Categoriaequipamento>()
        {
            @Override
            public String toString(Categoriaequipamento object) {
                return object.getDesignacao();
            }

            @Override
            public Categoriaequipamento fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });  
        
    }
    

    public static ObservableList<EnumTipoFuncionario> fillTipoFuncionarioComboBox(){
        ObservableList<EnumTipoFuncionario> enumTipoFunObservableList;
        List<EnumTipoFuncionario> enumList =new ArrayList<>(EnumSet.allOf(EnumTipoFuncionario.class));
        enumTipoFunObservableList = FXCollections.observableArrayList(enumList);
        return enumTipoFunObservableList;
        
    }
   
    public static ObservableList<EnumTipoEspaco> fillTipoEspacoComboBox(){
        ObservableList<EnumTipoEspaco> enumObservableList;
        List<EnumTipoEspaco> enumList =new ArrayList<>(EnumSet.allOf(EnumTipoEspaco.class));
        enumObservableList = FXCollections.observableArrayList(enumList);
        return enumObservableList;
    }
}
