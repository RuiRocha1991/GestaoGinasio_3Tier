/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.exception;

/**
 *
 * @author Rui
 */
public class InscricaoAtrasadaException extends Exception {

    /**
     * Creates a new instance of <code>InscricaoAtrasadaException</code> without
     * detail message.
     */
    public InscricaoAtrasadaException() {
    }

    /**
     * Constructs an instance of <code>InscricaoAtrasadaException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InscricaoAtrasadaException(String msg) {
        super(msg);
    }
}
