/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.services;

import gestaoginasiohibernate.model.Aula;
import gestaoginasiohibernate.model.Contrato;
import hibernate.HibernateGenericLib;
import gestaoginasiohibernate.model.Inscricao;
import gestaoginasiohibernate.model.InscricaoId;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Rui
 */
public class InscricaoService {
    public static void createInscricao(Aula aula, Contrato contrato) {
        Inscricao insc= new Inscricao();
        InscricaoId id = new InscricaoId();
        id.setAula(aula.getCodigo());
        id.setUtente(contrato.getIdcontrato());
        insc.setData(Date.from(Instant.now()));
        insc.setId(id);
        insc.setAula(aula);
        insc.setContrato(contrato);
        aula.setInscritos((byte) (aula.getInscritos()+1));
        aula.getInscricaos().add(insc);
        contrato.getInscricaos().add(insc);
        HibernateGenericLib.saveObject(insc);
    }
    
    public static void deleteInscricao(Inscricao inscricao){
        HibernateGenericLib.deleteObject(inscricao);
    }
}
