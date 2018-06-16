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
public class EmailInvalidException extends Exception {

    /**
     * Creates a new instance of <code>EmailInvalidoException</code> without
     * detail message.
     */
    public EmailInvalidException() {
    }

    /**
     * Constructs an instance of <code>EmailInvalidoException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmailInvalidException(String msg) {
        super(msg);
    }
}
