package estufa.com.br.estufa.Objetos;

public class SUsuario {
    private int idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private static SUsuario instance;

    public static SUsuario getInstance() {
        if ( SUsuario.instance == null ) {
            SUsuario.instance = new SUsuario();
        }
        return SUsuario.instance;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

}
