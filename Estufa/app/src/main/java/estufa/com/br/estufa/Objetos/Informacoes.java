package estufa.com.br.estufa.Objetos;


public class Informacoes {

    private int idEstufa;
    private int idData;
    private int valor;
    private static Informacoes instance;

    public static Informacoes getInstance() {
        if ( Informacoes.instance == null ) {
            Informacoes.instance = new Informacoes();
        }
        return Informacoes.instance;
    }

    public int getIdEstufa() {
        return idEstufa;
    }

    public void setIdEstufa(int idEstufa) {
        this.idEstufa = idEstufa;
    }

    public void setIdData(int idData) {
        this.idData = idData;

        if (this.idData == 0){
            valor = 30;
        }
        if (this.idData == 1){
            valor = 7;
        }
        if (this.idData == 2){
            valor = 1;
        }
    }

    public int getValor() {
        return valor;
    }

    public int getIdData() {
        return idData;
    }
}
