/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import gestaoginasiohibernate.model.Planotreino;

/**
 *
 * @author Rui
 */
public class PlanoTreinoService {
    
    public static void createPlanoTreino(Planotreino newpt){
        HibernateGenericLib.saveObject(newpt);
    }
}
