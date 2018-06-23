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
import gestaoginasiohibernate.model.Avaliacaofisica;
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
    /**
     * Método que verifica se um Utente já está inscrito numa determinada aula de grupo
     * @param inscricoes lista de inscrições do utente
     * @param aula aula que pretende se registar
     * @return devolve true se existir e false se não estiver inscrito.
     */
    public static boolean verificarInscricao(Set<Inscricao> inscricoes, Aula aula){
        for(Inscricao i:(Set<Inscricao>)inscricoes){
            if(i.getAula().getCodigo()==aula.getCodigo()){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Método que devolve a data do ultimo pagamento
     * @param contrato contrato que pretende realizar a consulta
     * @return devolve a string com o mes e anos do ultimo pagamento realizado.
     */
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
    
    /**
     * Método que calcula o valor da divida de um determinado contrato
     * @param contrato contrato que pretende calcular o valor
     * @return devolve o montante total em divida
     */
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
    
    /**
     * Método que converte os vários tipos de aula em aulaContrato para poder 
     * apresentar todas as aulas na mesma tabele
     * @param contrato contrato que pretende obter essa informação
     * @return devolve a lista de aulas que está inscrito
     */
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
        
        for(Avaliacaofisica av:(Set<Avaliacaofisica>) contrato.getAvaliacaofisicas()){
            AulaContrato ac= new AulaContrato(av.getCod(),LocalDate.parse(av.getData().toString()), LocalTime.parse(av.getHora())
                    , "", "Avaliação Física",av.getPersonaltrainer().getProfessor().getColaborador().getNome(), 
                    "0", "1");
            listAulas.add(ac);
        }
        
        return listAulas;
    }
    
    /**
     * Método que remove aula individual de um determinado contrato
     * @param aulas lista de aulas individuais do contrato
     * @param id id da aulaContrato que pretende eliminar
     */
    public static void removeAulaIndividual(Set<Aulaindividual> aulas,int id){
        Aulaindividual aula=null;
        for(Aulaindividual a : aulas){
            if(a.getIdaula()==id){
                aula=a;
                a.getPersonaltrainer().getAulaindividuals().remove(a);
                aulas.remove(a);
                break;
            }
        }
        AulaIndividualService.deleteAulaIndividual(aula);
    }
     
    /**
     * Método que remove inscrição de um determinado contrato
     * @param inscricoes Lista de inscrições do contrato que pretende eliminar 
     * a inscrição
     * @param id id da aula que pretende remover a inscrição 
     */
    public static void removeInscricao(Set<Inscricao> inscricoes,int id){
        Inscricao inscricao=null;
        for(Inscricao a : inscricoes){
            if(a.getId().getAula()==id){
                inscricao=a;
                a.getAula().getInscricaos().remove(a);
                a.getAula().setInscritos((byte) (a.getAula().getInscritos()-1));
                inscricoes.remove(a);
                break;
            }
        }
        InscricaoService.deleteInscricao(inscricao);
    }
     
    /**
     * Método que remove a inscrição numa avaliação fisica
     * @param avaliacoes lista de avaliações fisicas do contrato
     * @param id id da valiação fisica que pretende eliminar
     */
    public static void removeAvaliacaoFisica(Set<Avaliacaofisica> avaliacoes,int id){
        Avaliacaofisica avaliacao=null;
        for(Avaliacaofisica a : avaliacoes){
            if(a.getCod()==id){
                avaliacao=a;
                a.getPersonaltrainer().getAvaliacaofisicas().remove(a);
                a.getContrato().getAvaliacaofisicas().remove(a);
                avaliacoes.remove(a);
                
                break;
            }
        }
        AvaliacaoFisicaService.deleteAvaliacaoFisica(avaliacao);
    }
    
    /**
     * Método que devolve a lista de todos os contratos ativos
     * @return list de contratos ativos
     */
    public static List<Contrato> getListContratoActive(){
        List<Contrato> lista= HibernateGenericLib.executeHQLQuery("from Contrato where ativo=1");
        return lista;
    }
    
    /**
     * Método que filtra os utentes por nome, Nif, telefone e email
     * @param descricao String que pretende pesquisar
     * @param list lista de Utentes que pretende filtrar
     * @return devolve lista de utentes que contenham o texto da descrição.
     */
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
    
    /**
     * Método que devolve o total das aulas individuais de um utente
     * @param contrato recebe o contrato que pretende fazer a consulta
     * @return devolve o total do custo com as aulas individuais.
     */
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
    
    /**
     * Método que atualiza as aulas individuais como pagas, sempre que é confirmado um pagamento do utente.
     * @param contrato contrato que realiza o pagamento.
     */
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
    
    /**
     * Método que vai confirmar e validar pagamento
     * @param contrato contrato que vai realizar o pagamento
     * @param valorTotal valor total que tem a pagar
     * @param date data que se realiza o pagamento
     * @throws NumericException lança excepção se o valor não for numerico.
     */
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
    
    /**
     * método que desativa um determinado contrato
     * @param contrato contrato que pretende ver desativo
     */
    public static void desativarContrato(Contrato contrato){
        contrato.setAtivo('0');
        HibernateGenericLib.saveObject(contrato);
    }
    
    /**
     * Método que altura o tipo de contrato a um utente com contrato
     * @param contrato contrato que pretende realizar a alteração
     * @param tipo novo tipo de contrato que ficar a utilizar.
     */
    public static void alterarTipoContrato(Contrato contrato, Tipocontrato tipo){
        contrato.setTipocontrato(tipo);
        contrato.setValormensalidade(tipo.getValor());
        HibernateGenericLib.saveObject(contrato);
    }
    
    /**
     * Método que altera a senha de um determinado contrato
     * @param contrato contrato que pretende alterar a senha
     * @param senha nova senha
     * @throws PassInvalidaException lança excepçao se nova senha nao cumprir com requisitos.
     */
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
    
    /**
     * Método que devolve uma lista de aulas individuais por pagar de um determinado utente com contrato
     * @param contrato contrato que pretende obter a lista de aulas
     * @return devolve lista de aulas individuais com pagamentos em atraso.
     */
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
    
    /**
     * Método que filtra as aulas de um contrato anteriores ao dia atual.
     * @param lista lista que pretende filtrar
     * @return lista com as aulas anteriores a data atual
     */
    public static List<AulaContrato> getAulaContratoAnterior(List<AulaContrato> lista){
        List<AulaContrato> aulas= new ArrayList<>();
        for(AulaContrato aula: lista){
            if(aula.getData().isBefore(LocalDate.now())||aula.getData().isEqual(LocalDate.now())&& aula.getHora().isBefore(LocalTime.now())){
                aulas.add(aula);
            }
        }
        return aulas;
    }
    
     /**
     * Método que filtra as aulas de um contrato do dia atual.
     * @param lista lista que pretende filtrar
     * @return lista com as aulas de hoje
     */
    public static List<AulaContrato> getAulaContratoHoje(List<AulaContrato> lista){
        List<AulaContrato> aulas= new ArrayList<>();
        for(AulaContrato aula: lista){
            if(aula.getData().isEqual(LocalDate.now()) && aula.getHora().isAfter(LocalTime.now()) ){
                aulas.add(aula);
            }
        }
        return aulas;
    }
    
     /**
     * Método que filtra as aulas de um contrato seguintes ao dia atual.
     * @param lista lista que pretende filtrar
     * @return lista com as aulas seguintes a data atual
     */
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
