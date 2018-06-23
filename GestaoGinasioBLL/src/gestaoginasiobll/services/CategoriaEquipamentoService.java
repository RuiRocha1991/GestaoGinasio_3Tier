/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiohibernate.model.Categoriaequipamento;
import hibernate.HibernateGenericLib;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rui
 */
public class CategoriaEquipamentoService {
    /**
     * Devolve uma lista com todas as categorias de equipamentos
     * @return list de categorias de equipamentos
     */
    public static List<Categoriaequipamento> getAllCategorias(){
        return HibernateGenericLib.executeHQLQuery("from Categoriaequipamento");
    }
    
    /**
     * Cria uma nova categoria de equipamento
     * @param categoria descrição da nova categoria
     * @return devolve a categoria acabada de criar.
     */
    public static Categoriaequipamento createCategoria(String categoria){
        Categoriaequipamento cat= new Categoriaequipamento();
        cat.setCodigo(5);
        cat.setDesignacao(categoria);
        HibernateGenericLib.saveObject(cat);
        return cat;
    }
    
    /**
     * Método que atualiza os dados de uma categoria
     * @param categoria Categoria que pretende atualizar
     * @param descricao nova descrição da categoria
     */
    public static void updateCategoria(Categoriaequipamento categoria, String descricao){
        categoria.setDesignacao(descricao);
        HibernateGenericLib.saveObject(categoria);
    }
    
    /**
     * Método que devolve a lista de categorias filtrada por nome
     * @param lista lista com as categorias que vão ser filtradas
     * @param nome nome pelo qual quer filtrar as categorias
     * @return devolve uma lista de categorias que contenham o nome recebido como parametro.
     */
    public static List<Categoriaequipamento> getCategoriasFiltrada(List<Categoriaequipamento> lista, String nome){
        List<Categoriaequipamento> listaFiltro= new ArrayList<>();
        for(Categoriaequipamento cat : lista){
                if(cat.getDesignacao().toLowerCase().contains(nome.toLowerCase())){
                    listaFiltro.add(cat);
                }
            }
        return listaFiltro;
    }
}
