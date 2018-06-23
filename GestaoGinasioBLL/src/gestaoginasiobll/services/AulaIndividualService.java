/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiobll.AulaProfessor;
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
    
    /**
     * Função que cria aula individual e adiciona na base de dados
     * @param contrato Contrato de utente para associar a aula que var ser criada
     * @param personaltrainer Personal Trainer que vai realizar a aula
     * @param data Data em que a aula se vai realizar
     * @param hora Hora em qua a aula se vai realizar
     * @param valor Custo final da aula
     * @param duracao Duração em Horas da aula
     * @throws NumericException Lança exceção se o valor não for numérico
     */
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
    
    /**
     * Recebe aula individual e apenas elimina da base de dados
     * @param aula aula que vai ser eliminada da base de dados, Aulaindividual.
     */
    public static void deleteAulaIndividual(Aulaindividual aula){
        HibernateGenericLib.deleteObject(aula);
    }
    /**
     * Recebe uma AulaProfessor, classe adaptada para conseguir incluir todas as aulas dadas por um determinado colaborador
     * e elimina a aula de todas as listas onde se encontra e em seguida elimina da base de dados.
     * @param aula AulaProfessor que vai buscar a aula individual a base de dados e será eliminada
     */
    public static void deleteAulaIndividual(AulaProfessor aula){
        Aulaindividual aulaDelete = (Aulaindividual) HibernateGenericLib.executeHQLQuery("from Aulaindividual where idaula="+aula.getCodigo()).get(0);
        aulaDelete.getPersonaltrainer().getAulaindividuals().remove(aulaDelete);
        aulaDelete.getContrato().getAulaindividuals().remove(aulaDelete);
        HibernateGenericLib.deleteObject(aulaDelete);
    }
    
    
    
}
