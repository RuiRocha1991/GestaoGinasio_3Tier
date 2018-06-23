/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiobll.ConvertType;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiohibernate.model.Equipamento;
import hibernate.HibernateGenericLib;
import java.util.List;
import gestaoginasiohibernate.model.Notaavaria;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Rui
 */
public class NotaAvariaService {
    /**
     * Método que devolve todas as notas de avaria existentes no sistema
     * @return devolve lista com as avarias todas registadas
     */
    public static List<Notaavaria> getAllNotasAvaria(){
        List<Notaavaria> lista=HibernateGenericLib.executeHQLQuery("from Notaavaria");
        return lista;
    }
    
    /**
     * Método que cria uma nova nota de avaria e coloca o equipamento fora de serviço.
     * @param equipamento equipamento que se encontra com problemas
     * @param date data em que ocorreram os problemas
     * @param descricao uma breve descrição do problema do equipamento.
     */
    public static void createNotaAvaria( Equipamento equipamento, Date date, String descricao){
        Notaavaria nota = new Notaavaria();
        nota.setDatanota(date);
        nota.setDescricao(descricao);
        nota.setEquipamento(equipamento);
        nota.setEstado('1');
        equipamento.setAtivo('0');
        equipamento.getNotaavarias().add(nota);
        HibernateGenericLib.saveObject(nota);
    }
    
    /**
     * Método que atualiza a nota de avaria e coloca o equipamento em serviço novamente.
     * @param nota nota que pretende atualizar
     * @param dataResolucao data da resolução do problema
     * @param valor custo da resolução do problema
     * @param obs breve observação a fazer sobre a avaria
     * @return devolve a nota de avaria
     * @throws NumericException lança a excepção caso o valor da reparação nao seja numérico.
     */
    public static Notaavaria updateNotaAvaria(Notaavaria nota, LocalDate dataResolucao, String valor, String obs) throws NumericException{
        nota.setDataresolusao(java.sql.Date.valueOf(dataResolucao));
        nota.setValor(ConvertType.stringToBigDecimal(valor));
        nota.setObservacoes(obs);
        nota.setEstado('0');
        HibernateGenericLib.saveObject(nota);
        nota.getEquipamento().setAtivo('1');
        return nota;
    }
}
