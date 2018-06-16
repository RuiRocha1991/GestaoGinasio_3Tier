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
        }
        return true;
    }
     
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
       
        if(colaborador.getProfessor().getPersonaltrainer()!=null && colaborador.getProfessor().getPersonaltrainer().getAulaindividuals()!=null){
            for(Aulaindividual aula:(Set<Aulaindividual>) colaborador.getProfessor().getPersonaltrainer().getAulaindividuals()){
                AulaProfessor ap= new AulaProfessor(aula.getIdaula(),LocalDate.parse(aula.getData().toString()),LocalTime.parse(aula.getHora()),aula.getContrato().getUtente().getNome(),"Espaço Comum",
                                    "Individual", 1, String.valueOf(aula.getDuracao()));
                listAulas.add(ap);
            }
        }
        return listAulas;
    }
    
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
