/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.exception;

/**
 *
 * @author Rui
 */
public class UtilizadorInvalidoException extends Exception {

    /**
     * Creates a new instance of <code>UtilizadorInvalidoException</code>
     * without detail message.
     */
    public UtilizadorInvalidoException() {
    }

    /**
     * Constructs an instance of <code>UtilizadorInvalidoException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UtilizadorInvalidoException(String msg) {
        super(msg);
    }
}
