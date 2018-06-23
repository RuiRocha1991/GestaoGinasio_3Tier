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
    /**
     * Função que verifica a disponibilidade de uma sala numa determinada data e hora
     * @param aulas lista de aulas da sala
     * @param date data que pretende ver a disponibilidade
     * @param time Hora que pretende ver a disponibilidade
     * @return return true se houver disponibilidade ou false de nao houver disponibilidade.
     */
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
