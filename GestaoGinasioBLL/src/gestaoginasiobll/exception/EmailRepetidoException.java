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
public class EmailRepetidoException extends Exception {

    /**
     * Creates a new instance of <code>EmailRepetidoException</code> without
     * detail message.
     */
    public EmailRepetidoException() {
    }

    /**
     * Constructs an instance of <code>EmailRepetidoException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmailRepetidoException(String msg) {
        super(msg);
    }
}
