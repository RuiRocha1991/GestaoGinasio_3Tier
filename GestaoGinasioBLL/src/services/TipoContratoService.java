/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import java.math.BigDecimal;
import java.util.List;
import gestaoginasiohibernate.model.Tipocontrato;

/**
 *
 * @author Rui
 */
public class TipoContratoService {
    public static List<Tipocontrato> getAllTipoContratos(){
        List<Tipocontrato> list=HibernateGenericLib.executeHQLQuery("from Tipocontrato");
        return list;
    }
    
    public static Tipocontrato createTipoContrato(Boolean ativo, String descricao, BigDecimal valor){
        Tipocontrato tipo= new Tipocontrato();
        tipo.setAtivo(ativo);
        tipo.setDescricao(descricao);
        tipo.setValor(valor);
        HibernateGenericLib.saveObject(tipo);
        return tipo;
    }
    
    public static void updateTipoContrato(Tipocontrato tipo){
        HibernateGenericLib.saveObject(tipo);
    }
}
