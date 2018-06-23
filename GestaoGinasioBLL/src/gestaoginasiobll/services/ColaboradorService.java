/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiohibernate.model.Avaliacaofisica;
import hibernate.HibernateGenericLib;
import java.util.List;
import gestaoginasiohibernate.model.Colaborador;
import gestaoginasiohibernate.model.Personaltrainer;
import gestaoginasiohibernate.model.Planotreino;
import gestaoginasiohibernate.model.Professor;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Rui
 */
public class ColaboradorService {
    /**
     * Método que devolve todos os colaboradores existentes no sistema
     * @return devolve lista de colaboradores
     */
    public static List<Colaborador> getAllColaboradores(){
         List<Colaborador> list=HibernateGenericLib.executeHQLQuery("from Colaborador");
         return list;
    }
    
    /**
     * Metodo que cria um novo personal Trainer
     * @param newColaborador colaborador personal Trainer
     */
    public static void createPersonalTrainer(Colaborador newColaborador){
        HibernateGenericLib.saveObject(newColaborador);
        HibernateGenericLib.saveObject(newColaborador.getProfessor());
        HibernateGenericLib.saveObject(newColaborador.getProfessor().getPersonaltrainer());
    }
    
    /**
     * Metodo que cria um novo Professor
     * @param newColaborador colaborador professor
     */
    public static void createProfessor(Colaborador newColaborador){
        HibernateGenericLib.saveObject(newColaborador);
        HibernateGenericLib.saveObject(newColaborador.getProfessor());
    }
    
    /**
     * Método que cria os restantes tipos de colaboradores.
     * @param newColaborador novo colaborador
     */
    public static void createColaborador(Colaborador newColaborador){
        HibernateGenericLib.saveObject(newColaborador);
    }
    /**
     * Método que atualiza os colaboradores
     * @param colaborador colaborador que pretende atualizar
     */
    public static void updateObjectColaborador(Colaborador colaborador){
        HibernateGenericLib.saveObject(colaborador);
    }
    
    /**
     * Método que atualiza o personal trainer
     * @param pt personal trainer que pretende atualizar
     */
    public static void updateObjectPersonalTrainer(Personaltrainer pt){
        HibernateGenericLib.saveObject(pt);
    }
    
    /**
     * Método que atuliza o professor
     * @param professor professor que pretende atualizar.
     */
    public static void updateObjectProfessor(Professor professor){
        HibernateGenericLib.saveObject(professor);
    }
    
    /**
     * Método que cria um novo colaborador
     * @param colaborador colaborador que vai criar
     * @param utilizador nome de utilizador
     * @param nome nome do colaborador
     * @param senha senha de acesso
     * @param tipofuncionario tipo de funcionário.
     */
    public static void newColaborador(Colaborador colaborador, String utilizador, String nome, String senha, String tipofuncionario){
        colaborador.setUtilizador(utilizador);
        colaborador.setNome(nome);
        colaborador.setSenha(senha);
        colaborador.setTipofuncionario(tipofuncionario);  
    }
    
    /**
     * Devolve os planos de treino que ainda nao foram registados 
     * @param pt personal trainer que pretende fazer essa consulta
     * @return devolve lista de planos de treino por concluir.
     */
    public static List<Planotreino> getNewsPlanoTreinoPT(Personaltrainer pt){
        List<Planotreino> lista= new ArrayList<>();
        for(Planotreino plano: (Set<Planotreino>)pt.getPlanotreinos()){
            if(plano.getLinhaplanotreinos().isEmpty()){
                lista.add(plano);
            }
        }
        return lista;
    }
    
     /**
     * Devolve as avaliações fisicas que ainda nao foram registadas 
     * @param pt personal trainer que pretende fazer essa consulta
     * @return devolve lista de avaliações fisicas por concluir.
     */
    public static List<Avaliacaofisica> getNewsAvaliacaoFisicaPT(Personaltrainer pt){
        List<Avaliacaofisica> lista= new ArrayList<>();
        for(Avaliacaofisica avaliacao: (Set<Avaliacaofisica>)pt.getAvaliacaofisicas()){
            if(avaliacao.getAltura()==null){
                lista.add(avaliacao);
            }
        }
        return lista;
    }
    
    /**
     * Método que verifica se um utilizador já existe no sistema
     * @param utilizador nome do novo utilizador
     * @param lista lista de utilizadores existentes
     * @return devolve true se existir e false se nao existir
     */
    public static boolean verificaUtilizador(String utilizador, List<Colaborador> lista){
        String user=utilizador.toLowerCase();
        for(Colaborador c:lista){
            if(user.equals(c.getUtilizador().toLowerCase())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Método que devolve uma lista de colaboradores filtrada por nome, utilizador e tipo de funcionário
     * @param listaColaborador lista que pretende obter os dados filtrados
     * @param pesquisa campo com o texto que pretende procurar 
     * @return devolve uma lista com os colaboradores que contenham o texto passado na pesquisa.
     */
    public static List<Colaborador> getListaColaboradorFiltro(List<Colaborador> listaColaborador, String pesquisa){
        List<Colaborador> lista= new ArrayList<>();
        String procura=pesquisa.toLowerCase();
        for(Colaborador c: listaColaborador){
            String nomeLista= c.getNome().toLowerCase();
            String userLista= c.getUtilizador().toLowerCase();
            String tipoFuncLista=c.getTipofuncionario().toLowerCase();
            if(nomeLista.contains(procura) ||userLista.contains(procura)||tipoFuncLista.contains(procura) ){
                lista.add(c);
            }
        }
        return lista;
    }
    
}