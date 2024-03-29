/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import enumerados.Dados;
import gestaoginasiobll.AulaProfessor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Avaliacaofisica;
import gestaoginasiohibernate.model.Colaborador;
import gestaoginasiohibernate.model.Professor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rui
 */
public class ProfessorService {
    /**
     * Verifica a disponibilidade de um professor dada uma data e uma hora
     * @param professor professor que pretende verificar a disponibilidade.
     * @param date data que pretende ver a disponibilidade
     * @param time hora que pretende ver a disponibilidade
     * @return return true se disponivel, false de indisponivel
     */
     public static boolean verificaDisponibilidadeDataHoras(Professor professor,LocalDate date, LocalTime time){
        for(Aula a: (Set<Aula>)professor.getAulas()){
            if(LocalDate.parse(a.getData().toString()).isEqual(date)&& 
                    LocalTime.parse(a.getHora()).equals(time)){
                return false;
            }
        }
        if(professor.getPersonaltrainer()!=null){
            for(Aulaindividual a: PersonalTrainerService.getAulasIndividualData(professor.getPersonaltrainer(),date)){
                if(LocalTime.parse(a.getHora()).equals(time)){
                    return false;
                }
            }
            for(Avaliacaofisica av: PersonalTrainerService.getAvaliacaoFisicasData(professor.getPersonaltrainer(), date)){
                if(LocalTime.parse(av.getHora()).equals(time)){
                    return false;
                }
            }
        }
        return true;
    }
     
     
    /**
     * Metodo que devolve todas as aulas de um professor
     * @param colaborador colaborador que pretende receber a lista das aulas
     * @return devolve uma lista com as aulas de um professor, aulas individuais, em grupo e avaliações fisicas.
     */ 
    public static List<AulaProfessor> getAulasProfessor(Colaborador colaborador){
        List<AulaProfessor> listAulas = new ArrayList<>();
        if(colaborador.getProfessor().getAulas()!=null){
             for(Aula aula  :(Set<Aula>)colaborador.getProfessor().getAulas()){
                AulaProfessor ap= new AulaProfessor(aula.getCodigo(),LocalDate.parse(aula.getData().toString()),
                        LocalTime.parse(aula.getHora()),aula.getDescricao(),aula.getSala().getEspaco().getDescricao(),
                         aula.getTipoaula().getNome(), aula.getInscricaos().size(), String.valueOf(aula.getDuracao()));
                listAulas.add(ap);
            }
        }
       
        if(colaborador.getProfessor().getPersonaltrainer()!=null ){
            if(colaborador.getProfessor().getPersonaltrainer().getAulaindividuals()!=null){
                for(Aulaindividual aula:(Set<Aulaindividual>) colaborador.getProfessor().getPersonaltrainer().getAulaindividuals()){
                    AulaProfessor ap= new AulaProfessor(aula.getIdaula(),LocalDate.parse(aula.getData().toString()),LocalTime.parse(aula.getHora()),aula.getContrato().getUtente().getNome(),"Espaço Comum",
                                        "Individual", 1, String.valueOf(aula.getDuracao()));
                    listAulas.add(ap);
                }
            }
            if(colaborador.getProfessor().getPersonaltrainer().getAvaliacaofisicas()!=null){
                for(Avaliacaofisica av:(Set<Avaliacaofisica>) colaborador.getProfessor().getPersonaltrainer().getAvaliacaofisicas()){
                     AulaProfessor ap= new AulaProfessor(av.getCod(),LocalDate.parse(av.getData().toString()),LocalTime.parse(av.getHora()),av.getContrato().getUtente().getNome(),"Espaço Comum",
                                        "Avaliacao", 1, String.valueOf("1"));
                    listAulas.add(ap);
                }
            }
        }
        return listAulas;
    }
    
    
    /**
     * Método que devolve as aulas de um colaborador dado uma data
     * @param colaborador colaborador que pretende receber as aulas
     * @param date data que pretende filtrar as aulas
     * @return devolve lista do professor de uma determinada data
     */
    public static List<AulaProfessor> getAulasDate(Colaborador colaborador, LocalDate date){
        List<AulaProfessor> listAulas=getAulasProfessor(colaborador);
        List<AulaProfessor> list=new ArrayList<>();
        for(AulaProfessor aula: listAulas){
            if(aula.getData().isEqual(date)){
                list.add(aula);
            }
        }
        return list;
    }
    
    /**
     * Método que devolve as horas de um professor dado uma data
     * @param colaborador professor que pretende saber as horas disponiveis
     * @param date data em que pretende saber da disponibilidade
     * @return devolve uma lista de horas que o professor se encontra disponivel.
     */
     public static List<LocalTime> getTimeFromDate(Colaborador colaborador, LocalDate date){
        List<LocalTime> listTimes= new ArrayList<>(Arrays.asList(Dados.getHoras()));
        List<AulaProfessor> listAulas = getAulasDate(colaborador, date);
        if(listAulas.isEmpty()){
            return listTimes;
        }else{
            for( AulaProfessor aula : listAulas){
                if(listTimes.contains(aula.getHora()))
                    listTimes.remove(aula.getHora());
            }
            return listTimes;
        }
    }
}
