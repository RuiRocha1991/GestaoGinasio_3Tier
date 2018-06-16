/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;


import gestaoginasiobll.ConvertType;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiohibernate.model.Avaliacaofisica;
import hibernate.HibernateGenericLib;
import java.math.BigDecimal;

/**
 *
 * @author Rui
 */
public class AvaliacaoFisicaService {
    public static void createAvaliacaoFisica(Avaliacaofisica avaliacao){
        HibernateGenericLib.saveObject(avaliacao);
    }
        
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
}
