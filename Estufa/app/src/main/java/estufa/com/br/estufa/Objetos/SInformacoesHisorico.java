package estufa.com.br.estufa.Objetos;

import java.util.List;

public class SInformacoesHisorico {

    private List<String> hora_alteracao, duracao, idestufa, status,data_alteracao,comentario;
    private int posicao;
    private static SInformacoesHisorico INSTANCE;

    public static SInformacoesHisorico getInstance() {
        if (SInformacoesHisorico.INSTANCE == null){
            SInformacoesHisorico.INSTANCE = new SInformacoesHisorico();
        }
        return SInformacoesHisorico.INSTANCE;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public List<String> getComentario() {
        return comentario;
    }

    public void setComentario(List<String> comentario) {
        this.comentario = comentario;
    }

    public List<String> getHora_alteracao() {
        return hora_alteracao;
    }

    public void setHora_alteracao(List<String> hora_alteracao) {
        this.hora_alteracao = hora_alteracao;
    }

    public List<String> getDuracao() {
        return duracao;
    }

    public void setDuracao(List<String> duracao) {
        this.duracao = duracao;
    }

    public List<String> getIdestufa() {
        return idestufa;
    }

    public void setIdestufa(List<String> idestufa) {
        this.idestufa = idestufa;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<String> getData_alteracao() {
        return data_alteracao;
    }

    public void setData_alteracao(List<String> data_alteracao) {
        this.data_alteracao = data_alteracao;
    }

}
