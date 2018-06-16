/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiobll.ConvertType;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Personaltrainer;
import hibernate.HibernateGenericLib;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 *
 * @author Rui
 */
public class AulaIndividualService {
    
    
    public static void createAulaIndividual(Contrato contrato,Personaltrainer personaltrainer,LocalDate data, LocalTime hora, String valor, String duracao) throws NumericException{
       Aulaindividual aula= new Aulaindividual();
       aula.setContrato(contrato);
       aula.setData(Date.valueOf(data));
       aula.setDuracao(Short.valueOf(duracao));
       aula.setHora(hora.toString());
       aula.setPago('0');
       aula.setPersonaltrainer(personaltrainer);
       aula.setValor(ConvertType.stringToBigDecimal(valor));
       personaltrainer.getAulaindividuals().add(aula);
       contrato.getAulaindividuals().add(aula);
       HibernateGenericLib.saveObject(aula);
    }
    
    public static void deleteAulaIndividual(Aulaindividual aula){
        HibernateGenericLib.deleteObject(aula);
    }
}
