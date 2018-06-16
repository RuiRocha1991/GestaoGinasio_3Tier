package gestaoginasiobll;


import java.time.LocalDate;
import java.time.LocalTime;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rui
 */
public class AulaProfessor {
    private int codigo;
    private LocalDate data;
    private LocalTime hora;
    private String sala;
    private String descricao;
    private String tipoaula;
    private String duracao;
    private int inscritos;


    public AulaProfessor(int cod, LocalDate data, LocalTime hora, String descricao, String sala, String tipoaula, int inscritos, String duracao) {
        this.codigo=cod;
        this.data=data;
        this.hora = hora;
        this.descricao=descricao;
        this.sala = sala;
        this.tipoaula = tipoaula;
        this.duracao = duracao;
        this.inscritos=inscritos;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public LocalDate getData() {
        return data;
    }

    public int getCodigo() {
        return codigo;
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

    public String getDuracao() {
        return duracao;
    }

    public int getInscritos() {
        return inscritos;
    }
    
    
}
