/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import gestaoginasiohibernate.model.Aula;

/**
 *
 * @author Rui
 */
public class SalaService {
     public static boolean verificaDisponibilidadeDataHora(Set<Aula> aulas,LocalDate date, LocalTime time){
        for(Aula a: aulas){
            if(LocalDate.parse(a.getData().toString()).isEqual(date) &&
                    LocalTime.parse(a.getHora()).equals(time)){
                return false;
            }
        }
        return true;
    } 
}
