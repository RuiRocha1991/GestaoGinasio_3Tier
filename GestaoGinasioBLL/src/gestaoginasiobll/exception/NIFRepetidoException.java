/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.exception;

/**
 *
 * @author ruiro
 */
public class NIFRepetidoException extends Exception {

    /**
     * Creates a new instance of <code>NIFRepetidoException</code> without
     * detail message.
     */
    public NIFRepetidoException() {
    }

    /**
     * Constructs an instance of <code>NIFRepetidoException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NIFRepetidoException(String msg) {
        super(msg);
    }
}
