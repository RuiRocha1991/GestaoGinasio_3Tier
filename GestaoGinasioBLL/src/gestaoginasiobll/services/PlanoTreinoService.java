/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiobll.exception.DiaExistenteException;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Linhaplanotreino;
import gestaoginasiohibernate.model.Personaltrainer;
import hibernate.HibernateGenericLib;
import gestaoginasiohibernate.model.Planotreino;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

/**
 *
 * @author Rui
 */
public class PlanoTreinoService {
    /**
     * Método que cria um novo plano de treino.
     * @param contrato contrato que pretende pedir o novo plano de treino
     * @param personalTrainer personal trainer selecionado para realizar o plano de treino
     * @param descricao descrição do novo plano de treino
     */
    public static void createPlanoTreino(Contrato contrato, Personaltrainer personalTrainer, String descricao){
        Planotreino newpt= new Planotreino();
        newpt.setContrato(contrato);
        newpt.setData(Date.valueOf(LocalDate.now().toString()));
        newpt.setPersonaltrainer(personalTrainer);
        newpt.setDescricao(descricao);
        personalTrainer.getPlanotreinos().add(newpt);
        contrato.getPlanotreinos().add(newpt);
        HibernateGenericLib.saveObject(newpt);
    }
    
    /**
     * Método que adiciona as linhas  ao plano de treino
     * @param plano plano que quer adicionar as linhas
     * @param dia dia da semana para elaborar um determinado conjunto de exercicios
     * @param descricao descrição dos exercicios que tem de realizar
     * @return devolve a linha plano de treino acabada de criar
     * @throws DiaExistenteException lança excepção se o dia que pretende adicionar já se encontrar registado
     */
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
    
    /**
     * Método que remove linhas do plano de treino
     * @param linha linha que pretende remover
     * @param plano plano onde a linha se encontra.
     */
    public static void removeLinhaPlanoTreino(Linhaplanotreino linha, Planotreino plano){
        plano.getLinhaplanotreinos().remove(linha);
    }
    
    /**
     * Método que adiciona as linhas do plano de treino na base de dados.
     * @param plano recebe o plano e grava todas as linhas.
     */
    public static void saveLinhasPlanoTreino(Planotreino plano){
        for(Linhaplanotreino linha: (Set<Linhaplanotreino>)plano.getLinhaplanotreinos()){
            HibernateGenericLib.saveObject(linha);
        }
    }
}