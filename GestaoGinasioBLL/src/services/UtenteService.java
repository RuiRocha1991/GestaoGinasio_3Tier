/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import java.util.Date;
import java.util.List;
import gestaoginasiohibernate.model.Utente;

/**
 *
 * @author Rui
 */
public class UtenteService {
    public static List<Utente> getAllUtentes() {
        List<Utente> list=HibernateGenericLib.executeHQLQuery("from Utente");
        return list;
    }
    
    public static void updateDataAll(Utente utente,String codPostal, String morada, String localidade, String email, String telemovel, Date dataNas, String senha){
        utente.setCodpostal(codPostal);
        utente.setMorada(morada);
        utente.setLocalidade(localidade);
        utente.setEmail(email);
        utente.setContacto(telemovel);
        utente.setDatanascimento(dataNas);
        utente.setSenha(senha);
        HibernateGenericLib.saveObject(utente);
    }
    
    public static void updateData(Utente utente,String codPostal, String morada, String localidade, String email, String telemovel, Date dataNas){
        utente.setCodpostal(codPostal);
        utente.setMorada(morada);
        utente.setLocalidade(localidade);
        utente.setEmail(email);
        utente.setContacto(telemovel);
        utente.setDatanascimento(dataNas);
        HibernateGenericLib.saveObject(utente);
        
    }

    
}
