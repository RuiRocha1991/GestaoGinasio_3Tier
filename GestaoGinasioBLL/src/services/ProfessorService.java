/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Professor;

/**
 *
 * @author Rui
 */
public class ProfessorService {
     public boolean verificaDisponibilidadeDataHoras(Professor professor,LocalDate date, LocalTime time){
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
}
