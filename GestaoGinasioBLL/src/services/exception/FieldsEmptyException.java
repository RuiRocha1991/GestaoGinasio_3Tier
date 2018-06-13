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
public class FieldsEmptyException extends Exception {

    /**
     * Creates a new instance of <code>FieldsEmptyException</code> without
     * detail message.
     */
    public FieldsEmptyException() {
    }

    /**
     * Constructs an instance of <code>FieldsEmptyException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FieldsEmptyException(String msg) {
        super(msg);
    }
}
