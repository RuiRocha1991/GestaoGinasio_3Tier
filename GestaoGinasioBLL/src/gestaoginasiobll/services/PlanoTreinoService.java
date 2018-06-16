/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiobll.exception.DiaExistenteException;
import gestaoginasiohibernate.model.Linhaplanotreino;
import gestaoginasiohibernate.model.Personaltrainer;
import hibernate.HibernateGenericLib;
import gestaoginasiohibernate.model.Planotreino;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Rui
 */
public class PlanoTreinoService {
    
    public static void createPlanoTreino(Planotreino newpt){
        HibernateGenericLib.saveObject(newpt);
    }
    
    public static Linhaplanotreino createLinhaPlanoTreino(Planotreino plano, String dia, String descricao) throws DiaExistenteException{
        Linhaplanotreino linha = new Linhaplanotreino();
        if(!plano.getLinhaplanotreinos().isEmpty()){
            for(Linhaplanotreino linhaPlano: (Set<Linhaplanotreino>)plano.getLinhaplanotreinos()){
                if(linhaPlano.getDia().equals(dia)){
                    throw new DiaExistenteException();
                }
            }
        }
        linha.setDescricao(descricao);
        linha.setDia(dia);
        linha.setPlanotreino(plano);
        plano.getLinhaplanotreinos().add(linha);
            
        return linha;
    }
    
    public static void removeLinhaPlanoTreino(Linhaplanotreino linha, Planotreino plano){
        plano.getLinhaplanotreinos().remove(linha);
    }
    
    public static void saveLinhasPlanoTreino(Planotreino plano){
        for(Linhaplanotreino linha: (Set<Linhaplanotreino>)plano.getLinhaplanotreinos()){
            HibernateGenericLib.saveObject(linha);
        }
    }
}
