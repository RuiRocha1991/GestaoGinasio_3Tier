/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import gestaoginasiohibernate.model.Aulaindividual;
import hibernate.HibernateGenericLib;


/**
 *
 * @author Rui
 */
public class AulaIndividualService {
    
    
    public static void createAulaIndividual(Aulaindividual aula){
        
    }
    
    public static void deleteAulaIndividual(Aulaindividual aula){
        HibernateGenericLib.deleteObject(aula);
    }
}
