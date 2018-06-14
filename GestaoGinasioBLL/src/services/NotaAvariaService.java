/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import gestaoginasiohibernate.model.Equipamento;
import hibernate.HibernateGenericLib;
import java.util.List;
import gestaoginasiohibernate.model.Notaavaria;
import java.util.Date;

/**
 *
 * @author Rui
 */
public class NotaAvariaService {
    public static List<Notaavaria> getAllNotasAvaria(){
        List<Notaavaria> lista=HibernateGenericLib.executeHQLQuery("from Notaavaria");
        return lista;
    }
    
    public static void createNotaAvaria( Equipamento equipamento, Date date, String descricao){
        Notaavaria nota = new Notaavaria();
        nota.setDatanota(date);
        nota.setDescricao(descricao);
        nota.setEquipamento(equipamento);
        nota.setEstado('1');
        equipamento.setAtivo('0');
        equipamento.getNotaavarias().add(nota);
        HibernateGenericLib.saveObject(nota);
    }
}
