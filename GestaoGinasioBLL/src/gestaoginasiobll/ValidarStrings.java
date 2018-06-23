/*
 * a criação desta classe tem como objetivo aproveitar codigo ja desenvolvido que esta disponivel para toda a comunidade
 * assim esta classe esta preparada para receber varios tipos de dados desde email, telefone, nif entre outos dados que 
 * necessitam de uma validação previa
 */
package gestaoginasiobll;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author ruiro
 */
public class ValidarStrings {
    private Matcher matcher;
    private Pattern nome;
    private Pattern email;
    private Pattern NIF;
    private Pattern codPostal;
    private Pattern senha;
    private Pattern mensalidade;

    
    private static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String NOME = "[a-zA-Z-çãõáí]+([ '-][a-zA-Z-çãõáí]+)*";
    private static final String VALNIFTEL ="([0-9]{9})";
    private static final String VALCODPOSTAL="(([0-9]{4}+(\\-[0-9]{3})))";
    private static final String VALSENHA="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-_])(?=\\S+$).{6,}$";
    private static final String VALMENSALIDADE="[0-9]*\\.?[0-9]*";
  

    /**
     * Metodo que cria um objeto para poder validar Strings
     */
    
	public ValidarStrings() {
		this.email = Pattern.compile(EMAIL_PATTERN);
                this.nome = Pattern.compile(NOME);
                this.NIF= Pattern.compile(VALNIFTEL);
                this.codPostal=Pattern.compile(VALCODPOSTAL);
                this.senha=Pattern.compile(VALSENHA);
                this.mensalidade=Pattern.compile(VALMENSALIDADE);
	}
        
        /**
	 * Validar o email
         * @param email
         * @return true se a expressao recebida contiver todos os parametros necessários
	 */
	public boolean validateEmail(String email) {
            matcher = this.email.matcher(email);
            return matcher.matches();
	}
        
        /**
	 * Validar o nome utilizador
         * @param nome
         * @return true se a expressao recebida contiver todos os parametros necessários
	 */
        public boolean validarNome(String nome){
            matcher = this.nome.matcher(nome);
            return matcher.matches();
        }
    
        /**
	 * Validar o NIF ou contacto
         * @param numero
         * @return true se a expressao recebida contiver todos os parametros necessários
	 */
        public boolean validarNifTEL(String numero){
            matcher = NIF.matcher(numero);
            return matcher.matches();
        }
        
        /**
	 * Validar o codigo postal
         * @param cod
         * @return true se a expressao recebida contiver todos os parametros necessários
	 */
        public boolean validarCodPostal(String cod){
            matcher = codPostal.matcher(cod);
            return matcher.matches();
        }
        
        /**
	 * Validar senha
         * @param senha
         * @return true se a expressao recebida contiver todos os parametros necessários
	 */
        public boolean validarSenha(String senha){
            matcher = this.senha.matcher(senha);
            return matcher.matches();
        }
        
        /**
	 * Validar Mensalidades
         * @param mensalidade
         * @return true se a expressao recebida contiver todos os parametros necessários
	 */
        public boolean validarMensalidade(String mensalidade){
            matcher = this.mensalidade.matcher(mensalidade);
            return matcher.matches();
        }
        
}
