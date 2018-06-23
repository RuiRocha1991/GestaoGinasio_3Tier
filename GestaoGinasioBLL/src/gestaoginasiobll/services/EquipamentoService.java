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
    /**
     * Método que devolve todos os equipamentos existentes no sistema
     * @return lista de equipamentos 
     */
    public static List<Equipamento> getAllEquipamento(){
        List<Equipamento> lista=HibernateGenericLib.executeHQLQuery("From Equipamento");
        return lista;
    }
    
    /**
     * Método que filtra a lista de equipamentos por espaço e categoria de equipamento
     * @param lista lista de equipamentos que pretende filtrar
     * @param esp espaço pelo qual quer receber os equipamentos existentes
     * @param cat categoria pela qual quer ver os equipamentos existentes.
     * @return devolve uma lista de equipamentos filtrada pelos paramentros recebidos.
     */
    public static List<Equipamento> getEquipamentoEspacoAndCategoria(List<Equipamento> lista,Espaco esp, Categoriaequipamento cat){
        List<Equipamento> list = new ArrayList<>();
        for(Equipamento equi: lista){
            if(equi.getCategoriaequipamento().equals(cat) && equi.getEspaco().equals(esp)){
                list.add(equi);
            }
        }
        return list;
    }
    
    /**
     * Método que filtra os equipamentos pelo nome
     * @param lista recebe uma lista que quer filtrar
     * @param name recebe uma string com o nome ou parte do nome do equipamento
     * @return devolve uma lista de equipamentos que contenham o nome ou parte do nome recebido como paramentro.
     */
    public static List<Equipamento> getEquipamentoToName(List<Equipamento> lista,String name){
        List<Equipamento> list = new ArrayList<>();
        for(Equipamento equi: lista){
            if(equi.getDescricao().toLowerCase().contains(name.toLowerCase())){
                list.add(equi);
            }
        }
        return list;
    }
    
    /**
     * método que cria novo equipamento e o adiciona a respetiva categoria e espaço
     * @param espaco espaço onde quer inserir o equipamento
     * @param categoriaequipamento categoria a que pertence o equipamento
     * @param descricao uma breve descrição sobre o equipamento
     * @param ativo estado em que se encontra o equipamento
     * @return devolve o equipamento acabado de criar
     */
    public static Equipamento createEquipamento( Espaco espaco, Categoriaequipamento categoriaequipamento, String descricao, char ativo){
        Equipamento equipamento= new Equipamento(0,espaco,categoriaequipamento ,descricao,ativo);
        espaco.getEquipamentos().add(equipamento);
        categoriaequipamento.getEquipamentos().add(equipamento);
        if(espaco.getSala()!=null){
            espaco.getSala().setNumerovagas((byte) (espaco.getSala().getNumerovagas()+1));
        }
        HibernateGenericLib.saveObject(equipamento);
        return equipamento;
    }
    
    /**
     * Método que atualiza equipamentos
     * @param equipamento equipamento que pretende atualizar
     * @param espaco novo espaço que pretende que o equipemaneto pertença
     * @param categoriaequipamento nova categoria 
     * @param descricao uma descrição sobre o equipamento
     * @param ativo estado em que se encontra o equipamento
     */
    public static void updateEquipamento(Equipamento equipamento , Espaco espaco, Categoriaequipamento categoriaequipamento, String descricao, char ativo){
        equipamento.setCategoriaequipamento(categoriaequipamento);
        if(equipamento.getEspaco().getSala()!=null){
            equipamento.getEspaco().getSala().setNumerovagas((byte) (equipamento.getEspaco().getSala().getNumerovagas()-1));
        }
        equipamento.setEspaco(espaco);
        if(espaco.getSala()!=null){
            espaco.getSala().setNumerovagas((byte) (espaco.getSala().getNumerovagas()+1));
        }
        equipamento.setDescricao(descricao);
        equipamento.setAtivo(ativo);
        HibernateGenericLib.saveObject(equipamento);
    }
    
    /**
     * Método que devolve o total de custos por cada equipamento
     * @param equipamento equipamento que pretentede ver essa informação
     * @return devolve o total de despesa do equipamento.
     */
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
