/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Avaliacaofisica;
import gestaoginasiohibernate.model.Personaltrainer;
import gestaoginasiohibernate.model.Professor;

/**
 *
 * @author Rui
 */
public class PersonalTrainerService {
    /**
     * Método que devolve uma lista de aulas de grupo de um professor de uma determinada data
     * @param professor professor que pretende saber as repetivas aulas
     * @param date data que pretende verificar as aulas
     * @return devolve lista com as aulas que o personal trainer realiza num determinado dia.
     */
    public static List<Aula> getAulasGrupoData(Professor professor,LocalDate date){
        List<Aula> aulas= new ArrayList<>();
        if(!professor.getAulas().isEmpty()){
            for(Object o: professor.getAulas()){
                Aula aula=(Aula)o;
                if(LocalDate.parse(aula.getData().toString()).isEqual(date)){
                    aulas.add(aula);
                }
            }
        }
        return aulas;
    }
    
    /**
     * Método que devolve a lista de aulas individuais de um determinado Personal trainer.
     * @param pt personal trainer que pretentde a informação
     * @param date date que pretende que seja filtrada
     * @return lista de aulas individuais que realiza nessa data
     */
    public static List<Aulaindividual> getAulasIndividualData(Personaltrainer pt, LocalDate date){
        List<Aulaindividual> aulas= new ArrayList<>();
        if(!pt.getAulaindividuals().isEmpty()){
            for(Object o: pt.getAulaindividuals()){
                Aulaindividual aula=(Aulaindividual)o;
                if(LocalDate.parse(aula.getData().toString()).isEqual(date)){
                    aulas.add(aula);
                }
            }
        }
        return aulas;
    }
    
    /**
     * Devolve uma lista com avaliações fisicas realizadas numa determinada data
     * @param pt personal trainer que pretende obter a informação
     * @param date data que pretende filtrar a informação
     * @return devolve lista com as avaliações físicas realizadas nessa data
     */
    public static List<Avaliacaofisica> getAvaliacaoFisicasData(Personaltrainer pt, LocalDate date){
        List<Avaliacaofisica> avaliacoes= new ArrayList<>();
        if(!pt.getAvaliacaofisicas().isEmpty()){
            for(Object o: pt.getAvaliacaofisicas()){
                Avaliacaofisica avaliacao=(Avaliacaofisica)o;
                if(LocalDate.parse(avaliacao.getData().toString()).isEqual(date)){
                    avaliacoes.add(avaliacao);
                }
            }
        }
        return avaliacoes;
    }
    
    /**
     * Devolve uma lista de horas disponiveis do personal trainer de uma determinada data
     * @param pt Personal Trainer que pretende obter a informação
     * @param date Data que pretende filtrar a informação
     * @return devolve uma lista de horas que o personal trainer se encontra disponivel.
     */
    public static List<LocalTime> getHorasDisponiveisDia(Personaltrainer pt,LocalDate date){
        List<LocalTime> lista=new ArrayList<>();
        List<Aula> aulasGrupo=getAulasGrupoData(pt.getProfessor(),date);
        List<Aulaindividual> aulasIndividuais=getAulasIndividualData(pt ,date);
        List<Avaliacaofisica> avaliacoes=getAvaliacaoFisicasData(pt,date);
        for(int i=7; i<21; i++){
            boolean exist =false;
            for(Aula aula: aulasGrupo){
                if(LocalTime.of(i, 00).toString().equals(aula.getHora())){
                    exist=true;
                }
            }
            for(Aulaindividual aulaInd: aulasIndividuais){
                if(LocalTime.of(i, 00).toString().equals(aulaInd.getHora())){
                    exist=true;
                }
            }
            for(Avaliacaofisica ava: avaliacoes){
                if(LocalTime.of(i, 00).toString().equals(ava.getHora())){
                    exist=true;
                }
            }
            if(!exist){
                LocalTime hora=LocalTime.of(i, 00);
                lista.add(hora);
            }
        }
        
        return lista;
    }
}
