/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Rui
 */
public class AulaContrato {
    private int codigo;
    private LocalDate data;
    private LocalTime hora;
    private String sala;
    private String tipoaula;
    private String professor;
    private String custo;
    private String duracao;

    public AulaContrato(int cod, LocalDate data, LocalTime hora, String sala, String tipoaula, String professor, String custo, String duracao) {
        this.codigo=cod;
        this.data = data;
        this.hora = hora;
        this.sala = sala;
        this.tipoaula = tipoaula;
        this.professor = professor;
        this.custo = custo;
        this.duracao = duracao;
    }

    public int getCodigo() {
        return codigo;
    }
    
    public LocalDate getData() {
        return data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getSala() {
        return sala;
    }

    public String getTipoaula() {
        return tipoaula;
    }

    public String getProfessor() {
        return professor;
    }

    public String getCusto() {
        return custo;
    }

    public String getDuracao() {
        return duracao;
    }
    
    
}
