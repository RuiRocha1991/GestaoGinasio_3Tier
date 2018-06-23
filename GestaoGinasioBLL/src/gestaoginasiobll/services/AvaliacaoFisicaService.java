/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;


import gestaoginasiobll.ConvertType;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiohibernate.model.Avaliacaofisica;
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
public class AvaliacaoFisicaService {
    /**
     * Método que cria uma nova avaliação física
     * @param personalTrainer personal trainer que vai realizar avaliação fisica
     * @param contrato contrato que pediu a nova avaliação fisica
     * @param date data em que se vai realizar a avaliação fisica
     * @param time Hora em que se vai realizar a avaliação fisica
     * @param descricao uma pequena descrição do que pretende ver com a avaliação fisica.
     */
    public static void createAvaliacaoFisica(Personaltrainer personalTrainer, Contrato contrato, LocalDate date, LocalTime time, String descricao){
        Avaliacaofisica avaliacao = new Avaliacaofisica();
        avaliacao.setPersonaltrainer(personalTrainer);
        avaliacao.setContrato(contrato);
        avaliacao.setData(Date.valueOf(date));
        avaliacao.setHora(time.toString());
        avaliacao.setDescricao(descricao);
        personalTrainer.getAvaliacaofisicas().add(avaliacao);
        contrato.getAvaliacaofisicas().add(avaliacao);
        HibernateGenericLib.saveObject(avaliacao);
    }
        
    /**
     * Método que atualiza a avaliação fisica, quando a avaliação é realizada 
     * e o personal trainer preenche os dados da mesma
     * @param avaliacao
     * @param massagorda
     * @param peso
     * @param altura
     * @param dct
     * @param dcs
     * @param dcb
     * @param dcam
     * @param dcsi
     * @param dcto
     * @param dcc
     * @param dca
     * @param dcpm
     * @throws NumericException lança excepção se o campo altura nao for numerico.
     */
    public static void updateAvaliacaoFisica(Avaliacaofisica avaliacao,String massagorda,String peso, String altura,
            String dct,String dcs,String dcb,String dcam, String dcsi,String dcto,String dcc,String dca,String dcpm) throws NumericException{
        avaliacao.setDca(dca);
        avaliacao.setDcam(dcam);
        avaliacao.setDcb(dcb);
        avaliacao.setDcc(dcc);
        avaliacao.setDcpm(dcpm);
        avaliacao.setDcs(dcs);
        avaliacao.setDcsi(dcsi);
        avaliacao.setDct(dct);
        avaliacao.setDcto(dcto);
        avaliacao.setAltura(ConvertType.stringToBigDecimal(altura));
        avaliacao.setMassagorda(massagorda);
        avaliacao.setPeso(peso);
        HibernateGenericLib.saveObject(avaliacao);
    }
    
    /**
     * Método que elimina uma determinada avaliação fisica
     * @param avaliacao avaliação fisica que pretende que seja eliminada.
     */
    public static void deleteAvaliacaoFisica(Avaliacaofisica avaliacao){
        HibernateGenericLib.deleteObject(avaliacao);
    }
}
