/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;


import gestaoginasiohibernate.model.Avaliacaofisica;
import hibernate.HibernateGenericLib;

/**
 *
 * @author Rui
 */
public class AvaliacaoFisicaService {
    public static void createAvaliacaoFisica(Avaliacaofisica avaliacao){
        HibernateGenericLib.saveObject(avaliacao);
    }
}
