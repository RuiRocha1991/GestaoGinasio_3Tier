/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

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
    
    public static void updateEspaco(Espaco esp, int espacoSelected, String descricao){
        if(esp.getSala()!=null && !esp.getSala().getAulas().isEmpty()){
            System.out.println("Erro");
        }else{
            if(esp.getSala()!=null){
                HibernateGenericLib.deleteObject(esp.getSala());
                esp.setSala(null);
            }
            if(esp.getEspacocomum()!=null){
                HibernateGenericLib.deleteObject(esp.getEspacocomum());
                esp.setEspacocomum(null);
            }
            esp.setDescricao(descricao);
            if(espacoSelected==0){
                Espacocomum espCom = new Espacocomum();
                espCom.setCodigo(esp.getCodigo());
                espCom.setEspaco(esp);
                esp.setEspacocomum(espCom);
                EspacoService.createEspacoComum(esp, espCom);
            }else{
                Sala sala= new Sala();
                sala.setCodigo(esp.getCodigo());
                sala.setNumerovagas(Byte.valueOf("0"));
                sala.setEspaco(esp);
                esp.setSala(sala);
                EspacoService.createSala(esp, sala);
            }
        }
        HibernateGenericLib.saveObject(esp);
    }
    
    public static Espaco createEspaco(String descricao, int espacoSelected ){
        Espaco newEsp= new Espaco();
        newEsp.setDescricao(descricao);
        if(espacoSelected==0){
            Espacocomum espCom = new Espacocomum();
            espCom.setCodigo(newEsp.getCodigo());
            espCom.setEspaco(newEsp);
            newEsp.setEspacocomum(espCom);
            EspacoService.createEspacoComum(newEsp, espCom);
        }else{
            Sala sala= new Sala();
            sala.setCodigo(newEsp.getCodigo());
            sala.setNumerovagas(Byte.valueOf("0"));
            sala.setEspaco(newEsp);
            newEsp.setSala(sala);
            EspacoService.createSala(newEsp, sala);
        }
        return newEsp;
    }
}
