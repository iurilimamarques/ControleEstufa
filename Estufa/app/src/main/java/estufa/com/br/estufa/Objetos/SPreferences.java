package estufa.com.br.estufa.Objetos;

public class SPreferences {

    private static SPreferences INSTANCE;
    private static String ARQUIVO_LOGIN_PREFERENCIA;
    private static String ARQUIVO_GRAFICO_PREFERENCIA;

    private SPreferences(){

    }

    public static SPreferences getInstance(){
        if (SPreferences.INSTANCE == null){
            SPreferences.INSTANCE = new SPreferences();
        }
        return SPreferences.INSTANCE;
    }

    public static String getARQUIVO_LOGIN_PREFERENCIA() {
        return ARQUIVO_LOGIN_PREFERENCIA;
    }

    public static String getARQUIVO_GRAFICO_PREFERENCIA() {
        return ARQUIVO_GRAFICO_PREFERENCIA;
    }
}
