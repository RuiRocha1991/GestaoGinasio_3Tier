/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiohibernate.model.Tipoaula;
import hibernate.HibernateGenericLib;
import java.util.List;

/**
 *
 * @author Rui
 */
public class TipoAulaService {
    public static List<Tipoaula> getAllTipoAula(){
        return HibernateGenericLib.executeHQLQuery("from Tipoaula");
    }
    
    public static Tipoaula createTipoAula(String nome, String descricao){
        Tipoaula tipo= new Tipoaula();
        tipo.setNome(nome);
        tipo.setDescricao(descricao);
        HibernateGenericLib.saveObject(tipo);
        return tipo;
    }
    
    public static void updateTipoAula(Tipoaula tipo, String nome, String descricao){
        tipo.setNome(nome);
        tipo.setDescricao(descricao);
        HibernateGenericLib.saveObject(tipo);
    }
}
