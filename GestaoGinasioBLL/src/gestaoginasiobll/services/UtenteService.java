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
    public static List<Utente> getAllUtentes() {
        List<Utente> list=HibernateGenericLib.executeHQLQuery("from Utente");
        return list;
    }
    
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
    
    public static void updateData(Utente utente,String codPostal, String morada, String localidade, String email, String telemovel, Date dataNas){
        utente.setCodpostal(codPostal);
        utente.setMorada(morada);
        utente.setLocalidade(localidade);
        utente.setEmail(email);
        utente.setContacto(telemovel);
        utente.setDatanascimento(dataNas);
        HibernateGenericLib.saveObject(utente); 
    }

    
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
    
    public static void validarTelefone(String telefone) throws NumericException{
        ValidarStrings va = new ValidarStrings();
        if(!va.validarNifTEL(telefone)){
            throw new  NumericException();
        }
    }
    
    public static void validarEmail(String email) throws EmailInvalidException, EmailRepetidoException {
        ValidarStrings va = new ValidarStrings();
        if(!va.validateEmail(email)){
            throw new  EmailInvalidException();
        }
        if(HibernateGenericLib.executeHQLQuery("from Utente where email='"+email+"'").size()>0){
            throw new EmailRepetidoException();
        }
    }
    
    public static void validarCodPostal(String codigo) throws CodPostInvalidException {
        ValidarStrings va = new ValidarStrings();
        if(!va.validarCodPostal(codigo)){
            throw new CodPostInvalidException();
        }
    }
    
    public static void validarSenha(String senha, String senha2) throws PassInvalidaException, PasswordInvalidException  {
        ValidarStrings va = new ValidarStrings();
        if(!va.validarSenha(senha)){
            throw new PassInvalidaException();
        }
        if(!senha.equals(senha2)){
            throw new PasswordInvalidException();
        }
    }
    
    public static Contrato getContratoAtivo(Utente utente){
        Contrato contrato =null;
        for(Contrato c : (Set<Contrato>)utente.getContratos()){
            if(c.getAtivo()=='1'){
                contrato=c;
            }
        }
        return contrato;
    }
    
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
    
    public static List<Utente> getUtentesContratoDesativo(List<Utente> listaUtentes){
        List<Utente> lista= new ArrayList<>();
        for(Utente ut: listaUtentes){
            if(UtenteService.getContratoAtivo(ut)==null){
                lista.add(ut);
            }
        }   
        return lista;
    }
    
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
