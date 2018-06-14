
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
    private LocalTime hora;
    private String professor;
    private String sala;
    private String tipoaula;
    private String duracao;
    private int inscritos;


    public AulaProfessor(int cod, LocalTime hora, String sala, String tipoaula, String professor, int inscritos, String duracao) {
        this.codigo=cod;
        this.hora = hora;
        this.sala = sala;
        this.tipoaula = tipoaula;
        this.professor = professor;
        this.duracao = duracao;
        this.inscritos=inscritos;
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

    public String getProfessor() {
        return professor;
    }

    public String getDuracao() {
        return duracao;
    }

    public int getInscritos() {
        return inscritos;
    }
    
    
}
