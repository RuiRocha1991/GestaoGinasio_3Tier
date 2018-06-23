/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiobll.ValidarStrings;
import gestaoginasiobll.exception.CodPostInvalidException;
import gestaoginasiobll.exception.EmailInvalidException;
import gestaoginasiobll.exception.EmailRepetidoException;
import gestaoginasiobll.exception.NIFRepetidoException;
import gestaoginasiobll.exception.NumericException;
import gestaoginasiobll.exception.PassInvalidaException;
import gestaoginasiobll.exception.PasswordInvalidException;
import gestaoginasiohibernate.model.Contrato;
import gestaoginasiohibernate.model.Tipocontrato;
import hibernate.HibernateGenericLib;
import java.sql.Date;
import java.util.List;
import gestaoginasiohibernate.model.Utente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Rui
 */
public class UtenteService {
    /**
     * Função que retorna todos os utentes existentes na base de dados.
     * @return List de utentes da base de dados
     */
    public static List<Utente> getAllUtentes() {
        List<Utente> list=HibernateGenericLib.executeHQLQuery("from Utente");
        return list;
    }
    
    /**
     * Atualiza os dados de um utente na base de dados
     * @param utente Utente que deseja atualizar
     * @param codPostal Codigo Postal do utente
     * @param morada Morada do utente
     * @param localidade Localidade do Utente
     * @param email Email do utente
     * @param telemovel Telemovel do utente
     * @param dataNas Data Nascimento do utente
     * @param senha Senha do Utente
     */
    public static void updateDataAll(Utente utente,String codPostal, String morada, String localidade, String email, String telemovel, Date dataNas, String senha){
        utente.setCodpostal(codPostal);
        utente.setMorada(morada);
        utente.setLocalidade(localidade);
        utente.setEmail(email);
        utente.setContacto(telemovel);
        utente.setDatanascimento(dataNas);
        utente.setSenha(senha);
        HibernateGenericLib.saveObject(utente);
    }
    
    /**
     * Atualiza os dados de um utente na base de dados menos a palavra pass.
     * @param utente Utente que deseja atualizar
     * @param codPostal Codigo Postal do utente
     * @param morada Morada do utente
     * @param localidade Localidade do Utente
     * @param email Email do utente
     * @param telemovel Telemovel do utente
     * @param dataNas Data Nascimento do utente
     */
    public static void updateData(Utente utente,String codPostal, String morada, String localidade, String email, String telemovel, Date dataNas){
        utente.setCodpostal(codPostal);
        utente.setMorada(morada);
        utente.setLocalidade(localidade);
        utente.setEmail(email);
        utente.setContacto(telemovel);
        utente.setDatanascimento(dataNas);
        HibernateGenericLib.saveObject(utente); 
    }

    /**
     * Metodo que cria um novo Utente
     * @param nome Nome do utente
     * @param NIF Numero de Identificação Fiscal do utente
     * @param codPostal Codigo Postal do utente
     * @param morada Morada do utente
     * @param localidade Localidade do utente
     * @param email Email do utente
     * @param telemovel Telemovel do utente
     * @param dataNas Data Nascimento do utente
     * @param tipocontrato Tipo Contrato que vai ser associado ao utente
     * @param senha Senha do utente
     * @return Contrato com todos os dados do utente
     */
    public static Contrato createUtente(String nome, String NIF,String codPostal, String morada, String localidade, String email, String telemovel, LocalDate dataNas, Tipocontrato tipocontrato, String senha) {
        Utente utente = new Utente();
        Contrato contrato = new Contrato();
        contrato.setTipocontrato(tipocontrato);
        contrato.setValormensalidade(tipocontrato.getValor());
        contrato.setAtivo('1');
        utente.setNome(nome);
        utente.setNif(Integer.valueOf(NIF));
        utente.setCodpostal(codPostal);
        utente.setMorada(morada);
        utente.setLocalidade(localidade);
        utente.setEmail(email);
        utente.setContacto(telemovel);
        utente.setDatanascimento(Date.valueOf(dataNas));
        utente.setSenha(senha);
        contrato.setUtente(utente);
        utente.getContratos().add(contrato);
        tipocontrato.getContratos().add(contrato);
        HibernateGenericLib.saveObject(utente); 
        return contrato;
    }
    
    /**
     * Função para validar o Numero de identificação Fiscal e verificar de já existe.
     * @param nif recebe o numero em String
     * @throws NumericException lança exceção se não for numerico
     * @throws NIFRepetidoException Lança a exceção se ja existir na base de dados.
     */
    public static void validarNif(String nif) throws NumericException, NIFRepetidoException{
        ValidarStrings va = new ValidarStrings();
        if(!va.validarNifTEL(nif)){
            throw new  NumericException();
        }
        int NIF=Integer.valueOf(nif);
        if(HibernateGenericLib.executeHQLQuery("from Utente where nif='"+NIF+"'").size()>0){
            throw new NIFRepetidoException();
        }
    }
    
    /**
     * Valida Numero de telemovel
     * @param telefone recebe o numero em String
     * @throws NumericException lança exceção se não for numerico
     */
    public static void validarTelefone(String telefone) throws NumericException{
        ValidarStrings va = new ValidarStrings();
        if(!va.validarNifTEL(telefone)){
            throw new  NumericException();
        }
    }
    
    /**
     * Validar email
     * @param email recebe email como String
     * @throws EmailInvalidException lança exceção se email não cumprir com os requisitos
     * @throws EmailRepetidoException Lança exceção se email ja se encontrar registado no sistema
     */
    public static void validarEmail(String email) throws EmailInvalidException, EmailRepetidoException {
        ValidarStrings va = new ValidarStrings();
        if(!va.validateEmail(email)){
            throw new  EmailInvalidException();
        }
        if(HibernateGenericLib.executeHQLQuery("from Utente where email='"+email+"'").size()>0){
            throw new EmailRepetidoException();
        }
    }
    
    /**
     * Validar codigo postal
     * @param codigo recebe o codigo como String
     * @throws CodPostInvalidException lança exceção se o codigo não respeitar os requisitos
     */
    public static void validarCodPostal(String codigo) throws CodPostInvalidException {
        ValidarStrings va = new ValidarStrings();
        if(!va.validarCodPostal(codigo)){
            throw new CodPostInvalidException();
        }
    }
    
    /**
     * Função que valida senha e verifica se são iguais
     * @param senha recebe a senha como String
     * @param senha2 recebe a senha de confirmação como String
     * @throws PassInvalidaException lança exceção se a senha não cumprir com os requisitos
     * @throws PasswordInvalidException lança exceção se as senhas nao forem iguais
     */
    public static void validarSenha(String senha, String senha2) throws PassInvalidaException, PasswordInvalidException  {
        ValidarStrings va = new ValidarStrings();
        if(!va.validarSenha(senha)){
            throw new PassInvalidaException();
        }
        if(!senha.equals(senha2)){
            throw new PasswordInvalidException();
        }
    }
    
    /**
     * Função que verifica se um determinado Utente tem algum contrato ativo e devolve o ativo
     * @param utente utente que pretende verificar se tem contrato ativo
     * @return retorna o contrato ativo do utente que recebe como parametro
     */
    public static Contrato getContratoAtivo(Utente utente){
        Contrato contrato =null;
        for(Contrato c : (Set<Contrato>)utente.getContratos()){
            if(c.getAtivo()=='1'){
                contrato=c;
            }
        }
        return contrato;
    }
    
    
    /**
     * Função que devolve os utentes que contenham no nome, Nif, telefone e email a String recebida como parametro.
     * @param listaUtentes lista onde pretende fazer a consulta e filtrar
     * @param pesquisa String onde tem o texto para filtrar a lista
     * @return retorna uma lista de utentes que contenham o texto recebido na pesquisa
     */
    public static List<Utente> getUtentePesquisa(List<Utente> listaUtentes, String pesquisa){
        List<Utente> lista= new ArrayList<>();
        ValidarStrings va = new ValidarStrings();
        String procura="";
        for(Utente c: listaUtentes){
            if(va.validarNifTEL(pesquisa)){
                procura=String.valueOf(pesquisa);
            }else{
                procura=pesquisa.toLowerCase();
            }
            String nomeLista= c.getNome().toLowerCase();
            String contactoLista= c.getContacto();
            String emailLista= c.getEmail().toLowerCase();
            String nifLista=String.valueOf(c.getNif());
            if(nomeLista.contains(procura) ||contactoLista.contains(procura)||
                    emailLista.contains(procura) ||nifLista.contains(procura)){
                lista.add(c);
            }
        }
        return lista;
    }
    
    /**
     * Devolve todos os utente que não tenham contrato ativo no sistema
     * @param listaUtentes lista de utente que pretende filtrar
     * @return lista filtrada com utentes sem contratos ativos.
     */
    public static List<Utente> getUtentesContratoDesativo(List<Utente> listaUtentes){
        List<Utente> lista= new ArrayList<>();
        for(Utente ut: listaUtentes){
            if(UtenteService.getContratoAtivo(ut)==null){
                lista.add(ut);
            }
        }   
        return lista;
    }
    
    /**
     * Devolve todos os utente que tenham contratos ativos
     * @param listaUtentes lista que pretende filtrar 
     * @return devolve lista com utentes que tem contratos ativos.
     */
    public static List<Utente> getUtentesContratoAtivo(List<Utente> listaUtentes){
        List<Utente> lista= new ArrayList<>();
        for(Utente ut: listaUtentes){
            if(UtenteService.getContratoAtivo(ut)!=null){
                lista.add(ut);
            }
        }   
        return lista;
    }
    
   
}
