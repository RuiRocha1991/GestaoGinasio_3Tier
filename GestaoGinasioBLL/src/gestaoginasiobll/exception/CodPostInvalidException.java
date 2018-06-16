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
public class CodPostInvalidException extends Exception {

    /**
     * Creates a new instance of <code>CodigoPostalInvalidoException</code>
     * without detail message.
     */
    public CodPostInvalidException() {
    }

    /**
     * Constructs an instance of <code>CodigoPostalInvalidoException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CodPostInvalidException(String msg) {
        super(msg);
    }
}
