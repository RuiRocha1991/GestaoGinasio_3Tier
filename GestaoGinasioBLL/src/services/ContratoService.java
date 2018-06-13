/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import hibernate.HibernateGenericLib;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import projetogestaoginasio.AulaContrato;
import projetogestaoginasio.ConvertType;
import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Inscricao;
import gestaoginasiohibernate.model.Pagamento;

/**
 *
 * @author Rui
 */
public class ContratoService {
    public static boolean verificarInscricao(Set<Inscricao> inscricoes, Aula aula){
        boolean exit=false;
        
        for(Inscricao i:(Set<Inscricao>)inscricoes){
            if(i.getAula().getCodigo()==aula.getCodigo()){
                exit=true;
            }
        }
        return exit;
    }
    
    public static String consultaDataUltimoPagamento(Contrato contrato){
        String data="";
        short mes=0;
        int ano=0;
        int codigo=0;
        mes=contrato.getMesultimopagamento();
        ano=contrato.getAnoultimopagamento();
        codigo=contrato.getIdcontrato();
        List<Pagamento> ultimoPagamento= HibernateGenericLib.executeHQLQuery("from Pagamento where utente="+codigo+"and Mespagamento="+mes+"and anopagamento="+ano);
        data= ultimoPagamento.get(0).getDatapagamento().toString();
        return data;
    }
    
    public static int calculaValorDivida(Contrato contrato){
        int total=0;
        if(contrato.getAulaindividuals()!=null){
            for(Object o : contrato.getAulaindividuals()){
                Aulaindividual a=(Aulaindividual)o;
                if(a.getPago()=='0'){
                    total= total +ConvertType.BigDecimalToInteger(a.getValor());
                }
            }
            if((contrato.getMesultimopagamento()< LocalDate.now().getMonthValue() && contrato.getAnoultimopagamento()== LocalDate.now().getYear()) || contrato.getAnoultimopagamento() <LocalDate.now().getYear() ){
                total += ConvertType.BigDecimalToInteger(contrato.getValormensalidade());
            }
        }
        return total;
    }
    
    public static List<AulaContrato> getAulasContrato(Contrato contrato){
        List<AulaContrato> listAulas = new ArrayList<>();
        for(Object inscricao  :contrato.getInscricaos()){
            Inscricao in =(Inscricao)inscricao;
            AulaContrato ac= new AulaContrato(in.getId().getAula(),LocalDate.parse(in.getAula().getData().toString()), LocalTime.parse(in.getAula().getHora())
                    , in.getAula().getSala().getEspaco().getDescricao(), in.getAula().getTipoaula().getNome(),
                    in.getAula().getProfessor().getColaborador().getNome(), "0", String.valueOf(in.getAula().getDuracao()));
            
            listAulas.add(ac);
        }
        
        for(Object aula: contrato.getAulaindividuals()){
            Aulaindividual a=(Aulaindividual)aula;
            AulaContrato ac= new AulaContrato(a.getIdaula(),LocalDate.parse(a.getData().toString()), LocalTime.parse(a.getHora())
                    , "", "Individual",a.getPersonaltrainer().getProfessor().getColaborador().getNome(), 
                    a.getValor().toString(), String.valueOf(a.getDuracao()));
            listAulas.add(ac);
        }
        return listAulas;
    }
    
     public static Aulaindividual getAulaIndividual(List<Aulaindividual> aulas,int id){
        Aulaindividual aula=null;
        for(Object o : aulas){
            Aulaindividual a=(Aulaindividual)o;
            if(a.getIdaula()==id){
                aula=a;
                aulas.remove(a);
                break;
            }
        }
        return aula;
    }
     
     public static Inscricao getInscricao(List<Inscricao> inscricoes,int id){
        Inscricao inscricao=null;
        for(Object o : inscricoes){
            Inscricao a=(Inscricao)o;
            if(a.getId().getAula()==id){
                inscricao=a;
                inscricoes.remove(a);
                break;
            }
        }
        return inscricao;
    }
}
