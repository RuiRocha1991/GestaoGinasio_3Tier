/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiobll;

import java.math.BigDecimal;
import gestaoginasiobll.exception.NumericException;

/**
 *
 * @author Rui
 */
public class ConvertType {
    public static BigDecimal stringToBigDecimal(String texto) throws NumericException{
        if(ConvertType.isNumeric(texto)){
            return BigDecimal.valueOf(Double.valueOf(texto));
        }else{
            throw new NumericException();
        }
        
    }
    
    public static int BigDecimalToInteger(BigDecimal num){
        return Integer.valueOf(String.valueOf(num));
    }
    
    private static boolean isNumeric(String texto){
        return texto.matches("-?\\d+(\\.\\d+)?");
    }
}
