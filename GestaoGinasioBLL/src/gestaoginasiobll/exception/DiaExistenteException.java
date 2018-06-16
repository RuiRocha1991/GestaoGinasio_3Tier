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
public class DiaExistenteException extends Exception {

    /**
     * Creates a new instance of <code>DiaExistenteException</code> without
     * detail message.
     */
    public DiaExistenteException() {
    }

    /**
     * Constructs an instance of <code>DiaExistenteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DiaExistenteException(String msg) {
        super(msg);
    }
}
