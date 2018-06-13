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
public class PagamentoEmAtrasoException extends Exception {

    /**
     * Creates a new instance of <code>PagamentoEmAtrasoException</code> without
     * detail message.
     */
    public PagamentoEmAtrasoException() {
    }

    /**
     * Constructs an instance of <code>PagamentoEmAtrasoException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PagamentoEmAtrasoException(String msg) {
        super(msg);
    }
}
