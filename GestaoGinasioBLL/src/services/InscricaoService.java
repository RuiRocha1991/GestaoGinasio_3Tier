/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import gestaoginasiohibernate.model.Inscricao;

/**
 *
 * @author Rui
 */
public class InscricaoService {
    public static void createInscricao(Inscricao inscricao) {
            HibernateGenericLib.saveObject(inscricao);
    }
    
    public static void deleteInscricao(Inscricao inscricao){
        HibernateGenericLib.deleteObject(inscricao);
    }
}
