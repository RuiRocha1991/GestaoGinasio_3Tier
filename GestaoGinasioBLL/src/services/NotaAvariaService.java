/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import java.util.List;
import gestaoginasiohibernate.model.Notaavaria;

/**
 *
 * @author Rui
 */
public class NotaAvariaService {
    public static List<Notaavaria> getAllNotasAvaria(){
        List<Notaavaria> lista=HibernateGenericLib.executeHQLQuery("from Notaavaria");
        return lista;
    }
}
