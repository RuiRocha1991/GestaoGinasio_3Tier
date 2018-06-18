/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import hibernate.HibernateGenericLib;
import java.util.List;
import gestaoginasiohibernate.model.Categoriaequipamento;
import gestaoginasiohibernate.model.Equipamento;
import gestaoginasiohibernate.model.Espaco;
import gestaoginasiohibernate.model.Notaavaria;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Rui
 */
public class EquipamentoService {
    public static List<Equipamento> getAllEquipamento(){
        List<Equipamento> lista=HibernateGenericLib.executeHQLQuery("From Equipamento");
        return lista;
    }
    
    public static List<Equipamento> getEquipamentoEspacoAndCategoria(List<Equipamento> lista,Espaco esp, Categoriaequipamento cat){
        List<Equipamento> list = new ArrayList<>();
        for(Equipamento equi: lista){
            if(equi.getCategoriaequipamento().equals(cat) && equi.getEspaco().equals(esp)){
                list.add(equi);
            }
        }
        return list;
    }
    
    public static List<Equipamento> getEquipamentoToName(List<Equipamento> lista,String name){
        List<Equipamento> list = new ArrayList<>();
        for(Equipamento equi: lista){
            if(equi.getDescricao().toLowerCase().contains(name.toLowerCase())){
                list.add(equi);
            }
        }
        return list;
    }
    
    public static Equipamento createEquipamento( Espaco espaco, Categoriaequipamento categoriaequipamento, String descricao, char ativo){
        Equipamento equipamento= new Equipamento(0,espaco,categoriaequipamento ,descricao,ativo);
        espaco.getEquipamentos().add(equipamento);
        categoriaequipamento.getEquipamentos().add(equipamento);
        HibernateGenericLib.saveObject(equipamento);
        return equipamento;
    }
    
    public static Equipamento updateEquipamento(Equipamento equipamento , Espaco espaco, Categoriaequipamento categoriaequipamento, String descricao, char ativo){
        equipamento.setCategoriaequipamento(categoriaequipamento);
        equipamento.setEspaco(espaco);
        equipamento.setDescricao(descricao);
        equipamento.setAtivo(ativo);
        HibernateGenericLib.saveObject(equipamento);
        return equipamento;
    }
    
    public static int getTotalDespesasEquipamento(Equipamento equipamento){
        int tot=0;
        for(Notaavaria nota:(Set<Notaavaria>) equipamento.getNotaavarias()){
            if(nota.getValor()!=null){
                int valor=nota.getValor().intValue();
                tot= tot+valor;
            }
        }
        return tot;
    }
    
}
