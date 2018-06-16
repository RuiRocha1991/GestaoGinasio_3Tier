/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiohibernate.model.Categoriaequipamento;
import hibernate.HibernateGenericLib;
import java.util.List;

/**
 *
 * @author Rui
 */
public class CategoriaEquipamentoService {
    public static List<Categoriaequipamento> getAllCategorias(){
        return HibernateGenericLib.executeHQLQuery("from Categoriaequipamento");
    }
    public static Categoriaequipamento createCategoria(String categoria){
        Categoriaequipamento cat= new Categoriaequipamento();
        cat.setCodigo(5);
        cat.setDesignacao(categoria);
        HibernateGenericLib.saveObject(cat);
        return cat;
    }
    public static void updateCategoria(Categoriaequipamento categoria, String descricao){
        categoria.setDesignacao(descricao);
        HibernateGenericLib.saveObject(categoria);
    }
}
