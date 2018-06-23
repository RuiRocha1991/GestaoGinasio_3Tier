/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import hibernate.HibernateGenericLib;
import java.math.BigDecimal;
import java.util.List;
import gestaoginasiohibernate.model.Tipocontrato;

/**
 *
 * @author Rui
 */
public class TipoContratoService {
    /**
     * Função que carrega da base de dados todos os tipos de contratos existentes
     * @return List de tipos de contratos
     */
    public static List<Tipocontrato> getAllTipoContratos(){
        List<Tipocontrato> list=HibernateGenericLib.executeHQLQuery("from Tipocontrato");
        return list;
    }
    
    /**
     * Função que cria e grava na base de dados tipos de contratos
     * @param ativo parametro que valida se o tipo de contrato vai estar ativo ou não.
     * @param descricao Descrição para o tipo de contrato
     * @param valor mensalidade que o contrato vai ter.
     * @return devolve o tipo de contrato acabado de criar.
     */
    public static Tipocontrato createTipoContrato(Boolean ativo, String descricao, BigDecimal valor){
        Tipocontrato tipo= new Tipocontrato();
        tipo.setAtivo(ativo);
        tipo.setDescricao(descricao);
        tipo.setValor(valor);
        HibernateGenericLib.saveObject(tipo);
        return tipo;
    }
    
    /**
     * Atualiza tipos de contratos
     * @param tipo tipo de contrato que pretende atualizar
     */
    public static void updateTipoContrato(Tipocontrato tipo){
        HibernateGenericLib.saveObject(tipo);
    }
}
