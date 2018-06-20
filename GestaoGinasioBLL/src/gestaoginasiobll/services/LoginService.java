/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import hibernate.HibernateGenericLib;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import gestaoginasiobll.exception.UtilizadorInvalidoException;
import gestaoginasiohibernate.model.Colaborador;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Utente;
import gestaoginasiobll.exception.PagamentoEmAtrasoException;

/**
 *
 * @author Rui
 */
public class LoginService {
    public static Utente getUtenteLogin(String user, String senha) throws PagamentoEmAtrasoException{
        Utente utente=null;
        List<Utente> utentes= (List<Utente>)HibernateGenericLib.executeHQLQuery("from Utente Where Email='"+ user +"'and Senha='"+senha+"'");
        if(utentes.size()>0){
            utente=utentes.get(0);
            Set<Contrato> contratos =utente.getContratos();
            if(verificaSituacaoUtente(contratos)){
                return utente;
            }else{
                throw new PagamentoEmAtrasoException();
            }    
        }
        return null;
    }
    
    private static boolean verificaSituacaoUtente(Set<Contrato> contratos){
        for(Contrato c: contratos){
            if(c.getAtivo()=='1'){
                if(c.getMesultimopagamento()>= LocalDate.now().getMonthValue()
                        && c.getAnoultimopagamento()==LocalDate.now().getYear()){
                    return true;
                }else{
                    if((c.getMesultimopagamento()>=(LocalDate.now().getMonthValue()-1)
                            && c.getAnoultimopagamento()==LocalDate.now().getYear()) && 
                            LocalDate.now().getDayOfMonth()<10){
                        return true;
                    }else{
                        if((c.getMesultimopagamento()== 12
                            && c.getAnoultimopagamento()==LocalDate.now().getYear()-1) && 
                                LocalDate.now().getMonthValue()==1 &&LocalDate.now().getDayOfMonth()<10){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static Colaborador getColaboradorLogin(String user, String senha) throws PagamentoEmAtrasoException, UtilizadorInvalidoException{
        Colaborador colaborador=null;
        List<Colaborador> colaboradores = (List<Colaborador>) HibernateGenericLib.executeHQLQuery("from Colaborador Where Utilizador='"+ user +"'and Senha='"+senha+"'");
        if(colaboradores.size()>0){
            colaborador = colaboradores.get(0);
            return colaborador;
        }else{
            throw new UtilizadorInvalidoException();
        }
    }
}
