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
public class PasswordInvalidException extends Exception {

    /**
     * Creates a new instance of <code>PasswordInvalidException</code> without
     * detail message.
     */
    public PasswordInvalidException() {
    }

    /**
     * Constructs an instance of <code>PasswordInvalidException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PasswordInvalidException(String msg) {
        super(msg);
    }
}
