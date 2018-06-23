/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import hibernate.HibernateGenericLib;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Inscricao;
import gestaoginasiohibernate.model.Professor;
import gestaoginasiohibernate.model.Sala;
import gestaoginasiohibernate.model.Tipoaula;
import java.time.LocalTime;

/**
 *
 * @author Rui
 */

public class AulaService {
    /**
     * Função que vai criar aulas de grupo de acordo com o numero de semanas 
     * que a aula se vai repetir e adiciona na base de dados
     * @param dataAula Data inicio da aula, primeira semana
     * @param dateSelected data da aula seguinte
     * @param duracaoSemanas Duração em semanas da aula
     * @param descricaoAula Texto com breve descrição da aula
     * @param duracaoHoras Duração em Horas da aula
     * @param horaAula Hora a que se realiza a aula
     * @param professor Professor que vai realizar a aula
     * @param sala Local onde se realiza a aula
     * @param tipoAula Tipo de aula que vai ser criada
     * @return Todas as aulas criadas para adicionar na lista do observable.
     */
    public static List<Aula> addAula(Date dataAula, LocalDate dateSelected,int duracaoSemanas, String descricaoAula,
            int duracaoHoras, String horaAula, Professor professor, Sala sala, Tipoaula tipoAula){
        List<Aula> listaAulas = new ArrayList<>();
        LocalDate localDate=dateSelected;
        for(int i=0; i<duracaoSemanas; i++){
            Aula newAula = new Aula();
            newAula.setData(dataAula);
            newAula.setDescricao(descricaoAula);
            newAula.setDuracao(duracaoHoras);
            newAula.setHora(horaAula);
            newAula.setProfessor(professor);
            newAula.setSala(sala);
            newAula.setTipoaula(tipoAula);
            sala.getAulas().add(newAula);
            professor.getAulas().add(newAula);
            tipoAula.getAulas().add(newAula);
            listaAulas.add(newAula);
            HibernateGenericLib.saveObject(newAula);
            localDate=localDate.plusDays(7);
            dataAula = java.sql.Date.valueOf(localDate);
        }
        return listaAulas;
    }
    
    /**
     * Atualizar aula de grupo
     * @param aula aula de grupo que pretende atualizar
     */
    public static void updateAula(Aula aula){
        HibernateGenericLib.saveObject(aula);
    }
    
    /**
     * Função que elimina a aula de grupo e todas as inscrições associadas a essa aula
     * remove tudo da base de dados.
     * @param aula aula que pretende que seja eliminada
     */
    public static void deleteAula(Aula aula){
        if(aula.getInscritos()>0){
            for(Inscricao ins: (Set<Inscricao>)aula.getInscricaos()){
                HibernateGenericLib.deleteObject(ins);
            }
        }
        HibernateGenericLib.deleteObject(aula);
    }
    
    /**
     * Devolve todas as aulas de grupo existentes no sistema
     * @return lista de aulas de grupo
     */
    public static List<Aula> getAulasAll(){
        List aulasList= HibernateGenericLib.executeHQLQuery("from Aula");
        return aulasList;
    }
    
    /**
     * Devolve todas as aulas de grupo existente no sistema, desde a date atual.
     * @return lista de aulas de grupo desde a data atual.
     */
    public static List<Aula> getAllAulasToDate(){
        LocalDate localDate= LocalDate.now();
        Date date=Date.valueOf(localDate.toString());
        List aulasList= HibernateGenericLib.executeHQLQuery("from Aula where data>='"+date+"'");
        return aulasList;
    }
    
    /**
     * Função que filtra as aulas de grupo por data, sala e tipo de aula
     * @param listaAulas lista de aulas que pretende filtrar
     * @param date data da qual quer os resultados
     * @param sala sala onde quer procurar
     * @param tipoAula tipo de aula que pretende filtrar
     * @return List de aulas de acordo com os filtros.
     */
     public static List<Aula>  filtrarAulas(List<Aula> listaAulas, LocalDate date, Sala sala, Tipoaula tipoAula){
        List<Aula> lista= new ArrayList<>();
        List<Aula> listFinal= new ArrayList<>();
        if(date!=null){
            for(Aula a :listaAulas){
                if(a.getData().toString().equals(date.toString())){
                    if(sala!=null && tipoAula!=null){
                        if(a.getSala().equals(sala) && a.getTipoaula().equals(tipoAula))
                            lista.add(a);
                    }else{
                        if(sala!=null){
                            if(a.getSala().equals(sala))
                               lista.add(a);
                        }
                        if(tipoAula!=null){
                            if(tipoAula.equals(a.getTipoaula()))
                                lista.add(a);
                        }
                        if(sala==null && tipoAula==null)
                            lista.add(a);
                    }
                }
            }
        }
       if(date.isEqual(LocalDate.now())){
            for(Aula a :lista){
                if( LocalTime.parse(a.getHora()).isAfter(LocalTime.now())){
                    listFinal.add(a);
                }
            }
        }else{
          listFinal.addAll(lista);
       }
        return listFinal;
    }
}
