/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiohibernate.model.Tipoaula;
import hibernate.HibernateGenericLib;
import java.util.List;

/**
 *
 * @author Rui
 */
public class TipoAulaService {
    /**
     * Devolve uma lista do tipo de aulas existentes
     * @return List com os tipos de aula que existem no sistema
     */
    public static List<Tipoaula> getAllTipoAula(){
        return HibernateGenericLib.executeHQLQuery("from Tipoaula");
    }
    
    /**
     * Função que cria um tipo de aula e insere na base de dados.
     * @param nome nome do tipo de aula que vai ser criado
     * @param descricao descrição do tipo de aula
     * @return devolve o tipo de aula acabado de criar
     */
    public static Tipoaula createTipoAula(String nome, String descricao){
        Tipoaula tipo= new Tipoaula();
        tipo.setNome(nome);
        tipo.setDescricao(descricao);
        HibernateGenericLib.saveObject(tipo);
        return tipo;
    }
    
    /**
     * funçao que atualiza tipos de aula
     * @param tipo tipo de aula que pretende atualizar
     * @param nome novo nome que o tipo de aula vai receber
     * @param descricao nova descrição que o tipo de aula vai receber
     */
    public static void updateTipoAula(Tipoaula tipo, String nome, String descricao){
        tipo.setNome(nome);
        tipo.setDescricao(descricao);
        HibernateGenericLib.saveObject(tipo);
    }
}
