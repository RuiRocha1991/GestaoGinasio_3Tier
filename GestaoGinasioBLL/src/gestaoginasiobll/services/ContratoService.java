/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import hibernate.HibernateGenericLib;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import gestaoginasiobll.AulaContrato;
import gestaoginasiobll.ConvertType;
import gestaoginasiobll.ValidarStrings;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.exception.PassInvalidaException;
import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Aulaindividual;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Inscricao;
import gestaoginasiohibernate.model.Pagamento;
import gestaoginasiohibernate.model.PagamentoId;
import gestaoginasiohibernate.model.Tipocontrato;
import gestaoginasiohibernate.model.Utente;
import java.sql.Date;
import javafx.collections.ObservableList;

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
    
    public static void removeAulaIndividual(Set<Aulaindividual> aulas,int id){
        Aulaindividual aula=null;
        for(Object o : aulas){
            Aulaindividual a=(Aulaindividual)o;
            if(a.getIdaula()==id){
                aula=a;
                a.getPersonaltrainer().getAulaindividuals().remove(a);
                aulas.remove(a);
                break;
            }
        }
        AulaIndividualService.deleteAulaIndividual(aula);
    }
     
     public static void removeInscricao(Set<Inscricao> inscricoes,int id){
        Inscricao inscricao=null;
        for(Object o : inscricoes){
            Inscricao a=(Inscricao)o;
            if(a.getId().getAula()==id){
                inscricao=a;
                a.getAula().getInscricaos().remove(a);
                inscricoes.remove(a);
                break;
            }
        }
        InscricaoService.deleteInscricao(inscricao);
    }
     
    public static List<Contrato> getListContratoActive(){
        List<Contrato> lista= HibernateGenericLib.executeHQLQuery("from Contrato where ativo=1");
        return lista;
    }
    
    public static List<Contrato> getListFiltraContrato(String descricao, ObservableList<Contrato> list){
        List<Contrato> lista = new ArrayList<>();
        for(Contrato cont: list){
            if(cont.getUtente().getNome().toLowerCase().contains(descricao.toLowerCase())||
                    cont.getUtente().getEmail().toLowerCase().contains(descricao.toLowerCase())||
                    String.valueOf(cont.getUtente().getNif()).contains(descricao)){
                lista.add(cont);
            }
        }
        return lista;
    }
    
    public static String getTotalAulaIndividual(Contrato contrato){
        int tot=0;
        if(!contrato.getAulaindividuals().isEmpty()){
            for(Aulaindividual aula: (Set<Aulaindividual>)contrato.getAulaindividuals()){
                if(aula.getPago()=='0'){
                    tot+=ConvertType.BigDecimalToInteger(aula.getValor());
                }
            }
        }
        return String.valueOf(tot);
    }
    
    public static void atualizaAulasIndividuaisPago(Contrato contrato){
        if(!contrato.getAulaindividuals().isEmpty()){
            for(Aulaindividual aula: (Set<Aulaindividual>)contrato.getAulaindividuals()){
                if(aula.getPago()=='0'){
                    aula.setPago('1');
                    HibernateGenericLib.saveObject(aula);
                }
            }
        }
    }
    
    public static void confirmarPagamento(Contrato contrato, String valorTotal, LocalDate date) throws NumericException{
        Pagamento pagamento= new Pagamento();
        PagamentoId pagamentoId = new PagamentoId();
        contrato.setAnoultimopagamento(Short.valueOf(String.valueOf(date.getYear())));
        contrato.setMesultimopagamento(Byte.valueOf(String.valueOf(date.getMonthValue())));
        HibernateGenericLib.saveObject(contrato);
        pagamento.setContrato(contrato);
        pagamento.setDatapagamento(Date.valueOf(LocalDate.now()));
        pagamento.setValor(ConvertType.stringToBigDecimal(valorTotal));
        pagamentoId.setAnopagamento(String.valueOf(date.getYear()));
        pagamentoId.setMespagamento(String.valueOf(date.getMonthValue()));
        pagamentoId.setUtente(contrato.getIdcontrato());
        pagamento.setId(pagamentoId);
        contrato.getPagamentos().add(pagamento);
        HibernateGenericLib.saveObject(contrato);
        HibernateGenericLib.saveObject(pagamento);
        atualizaAulasIndividuaisPago(contrato);
    }
    
    public static void desativarContrato(Contrato contrato){
        contrato.setAtivo('0');
        HibernateGenericLib.saveObject(contrato);
    }
    
    public static void alterarTipoContrato(Contrato contrato, Tipocontrato tipo){
        contrato.setTipocontrato(tipo);
        contrato.setValormensalidade(tipo.getValor());
        HibernateGenericLib.saveObject(contrato);
    }
    
    public static void alterarSenhaContrato(Contrato contrato, String senha) throws PassInvalidaException{
        ValidarStrings va= new ValidarStrings();
        if(va.validarSenha(senha)){
            contrato.getUtente().setSenha(senha);
            HibernateGenericLib.saveObject(contrato);
        }else{
            throw new PassInvalidaException();
        }
    }
    
    public static Contrato createContrato(Utente utente, Tipocontrato tipo){
        Contrato contrato = new Contrato();
        contrato.setUtente(utente);
        contrato.setTipocontrato(tipo);
        contrato.setAtivo('1');
        contrato.setValormensalidade(tipo.getValor());
        utente.getContratos().add(contrato);
        return contrato;
    }
    
    public static List<Aulaindividual> getAulaIndividualPorPagar(Contrato contrato){
        List<Aulaindividual> lista= new ArrayList<>();
        if(!contrato.getAulaindividuals().isEmpty()){
            for(Aulaindividual aula: (Set<Aulaindividual>)contrato.getAulaindividuals()){
                if(aula.getPago()=='0'){
                    lista.add(aula);
                }
            }
        }
        return lista;
    }
    
    public static List<AulaContrato> getAulaContratoAnterior(List<AulaContrato> lista){
        List<AulaContrato> aulas= new ArrayList<>();
        for(AulaContrato aula: lista){
            if(aula.getData().isBefore(LocalDate.now())||aula.getData().isEqual(LocalDate.now())&& aula.getHora().isBefore(LocalTime.now())){
                aulas.add(aula);
            }
        }
        return aulas;
    }
    
    public static List<AulaContrato> getAulaContratoHoje(List<AulaContrato> lista){
        List<AulaContrato> aulas= new ArrayList<>();
        for(AulaContrato aula: lista){
            if(aula.getData().isEqual(LocalDate.now()) && aula.getHora().isAfter(LocalTime.now()) ){
                aulas.add(aula);
            }
        }
        return aulas;
    }
    
    public static List<AulaContrato> getAulaContratoProximas(List<AulaContrato> lista){
        List<AulaContrato> aulas= new ArrayList<>();
        for(AulaContrato aula: lista){
            if(aula.getData().isAfter(LocalDate.now())||aula.getData().isEqual(LocalDate.now())&& aula.getHora().isAfter(LocalTime.now()) ){
                aulas.add(aula);
            }
        }
        
        
        return aulas;
    }
}
