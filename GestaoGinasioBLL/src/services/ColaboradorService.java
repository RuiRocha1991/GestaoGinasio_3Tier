/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import java.util.List;
import gestaoginasiohibernate.model.Colaborador;
import gestaoginasiohibernate.model.Personaltrainer;
import gestaoginasiohibernate.model.Professor;

/**
 *
 * @author Rui
 */
public class ColaboradorService {
    public static List<Colaborador> getAllColaboradores(){
         List<Colaborador> list=HibernateGenericLib.executeHQLQuery("from Colaborador");
         return list;
    }
    
    public static void createPersonalTrainer(Colaborador newColaborador){
        HibernateGenericLib.saveObject(newColaborador);
        HibernateGenericLib.saveObject(newColaborador.getProfessor());
        HibernateGenericLib.saveObject(newColaborador.getProfessor().getPersonaltrainer());
    }
    
    public static void createProfessor(Colaborador newColaborador){
        HibernateGenericLib.saveObject(newColaborador);
        HibernateGenericLib.saveObject(newColaborador.getProfessor());
    }
    
    public static void createColaborador(Colaborador newColaborador){
        HibernateGenericLib.saveObject(newColaborador);
    }
    
    public static void updateObjectColaborador(Colaborador colaborador){
        HibernateGenericLib.saveObject(colaborador);
    }
    
    public static void updateObjectPersonalTrainer(Personaltrainer pt){
        HibernateGenericLib.saveObject(pt);
    }
    
    public static void updateObjectProfessor(Professor professor){
        HibernateGenericLib.saveObject(professor);
    }
    
    
    public static void newColaborador(Colaborador colaborador, String utilizador, String nome, String senha, String tipofuncionario){
        colaborador.setUtilizador(utilizador);
        colaborador.setNome(nome);
        colaborador.setSenha(senha);
        colaborador.setTipofuncionario(tipofuncionario);  
    }
    
}
