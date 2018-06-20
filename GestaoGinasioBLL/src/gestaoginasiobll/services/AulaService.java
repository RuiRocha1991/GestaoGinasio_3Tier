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
    
    public static void updateAula(Aula aula){
        HibernateGenericLib.saveObject(aula);
    }
    
    public static void deleteAula(Aula aula){
        if(aula.getInscritos()>0){
            for(Inscricao ins: (Set<Inscricao>)aula.getInscricaos()){
                HibernateGenericLib.deleteObject(ins);
            }
        }
        HibernateGenericLib.deleteObject(aula);
    }
    
    public static List<Aula> getAulasAll(){
        List aulasList= HibernateGenericLib.executeHQLQuery("from Aula");
        return aulasList;
    }
    
    public static List<Aula> getAllAulasToDate(){
        LocalDate localDate= LocalDate.now();
        Date date=Date.valueOf(localDate.toString());
        List aulasList= HibernateGenericLib.executeHQLQuery("from Aula where data>='"+date+"'");
        return aulasList;
    }
    
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
