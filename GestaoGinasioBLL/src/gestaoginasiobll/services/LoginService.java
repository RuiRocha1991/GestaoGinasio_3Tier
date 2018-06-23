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
    /**
     * função que devolve o utente para realizar o login, se existir.
     * @param user nome do utilizador que vai ser procurado na base de dados
     * @param senha senha do utilizador que vai ser procurado na base de dados
     * @return devolve o utente se todos os parametros estiverem corretos.
     * @throws PagamentoEmAtrasoException lança a excepção se o utente estiver com pagamentos em atraso.
     */
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
    
    /**
     * Método que verifica se um determinado utente tem contratos ativos e com os pagamentos em dia
     * @param contratos lista de todos os contratos possiveis de um determinado utente
     * @return devolve true se estiver com os pagamentos em dia false se estiver com pagamentos em atraso.
     */
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
    
    /**
     * Devolve o colaborador que com o utilizador e senha que recebe por parametro
     * @param user utilizador que pretende procurar no sistema
     * @param senha senha do utilizador que vai ser procurado no sistema
     * @return devolve o colaborador que tem o user e senha recebidos como parametro
     * @throws UtilizadorInvalidoException lança a excepção se nao encontrar nenhum utilizador com estes parametros.
     */
    public static Colaborador getColaboradorLogin(String user, String senha) throws UtilizadorInvalidoException{
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
