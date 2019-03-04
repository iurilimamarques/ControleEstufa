package estufa.com.br.estufa.Objetos;

public class Estufa {
    private int id_estufa;
    private int tempoMillis;
    private String status;

    public Estufa() {

    }

    public int getId_estufa() {
        return id_estufa;
    }

    public void setId_estufa(int id_estufa) {
        this.id_estufa = id_estufa;
    }

    public int getTempoMillis() {
        return tempoMillis;
    }

    public void setTempoMillis(int tempoMillis) {
        this.tempoMillis = tempoMillis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
