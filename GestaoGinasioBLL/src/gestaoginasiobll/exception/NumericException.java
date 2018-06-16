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
public class NumericException extends Exception {

    /**
     * Creates a new instance of <code>NumericException</code> without detail
     * message.
     */
    public NumericException() {
    }

    /**
     * Constructs an instance of <code>NumericException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NumericException(String msg) {
        super(msg);
    }
}
