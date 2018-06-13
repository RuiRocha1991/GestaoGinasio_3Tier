/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import java.util.List;
import gestaoginasiohibernate.model.Espaco;
import gestaoginasiohibernate.model.Espacocomum;
import gestaoginasiohibernate.model.Sala;

/**
 *
 * @author Rui
 */
public class EspacoService {
    public static List<Espaco> getAllEspacos(){
         List list= HibernateGenericLib.executeHQLQuery("from Espaco");
         return list;
    }
    
    public static void createEspacoComum(Espaco newEsp, Espacocomum espCom){
        HibernateGenericLib.saveObject(newEsp);
        HibernateGenericLib.saveObject(espCom);
    }
    
    public static void createSala(Espaco newEsp, Sala newSala){
        HibernateGenericLib.saveObject(newEsp);
        HibernateGenericLib.saveObject(newSala);
    }
    
    public static void updateEspaco(Espaco esp){
        HibernateGenericLib.saveObject(esp);
    }
}
