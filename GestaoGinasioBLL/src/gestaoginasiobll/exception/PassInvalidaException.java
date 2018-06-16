/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll.exception;

/**
 *
 * @author cristina
 */
public class PassInvalidaException extends Exception {

    /**
     * Creates a new instance of <code>SenhaInvalidaException</code> without
     * detail message.
     */
    public PassInvalidaException() {
    }

    /**
     * Constructs an instance of <code>SenhaInvalidaException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PassInvalidaException(String msg) {
        super(msg);
    }
}
