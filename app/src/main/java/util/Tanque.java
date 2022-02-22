package util;

public class Tanque {
    private String resposta;
    private String comando;
    private String programa;
    private String nivel;
    private String status;

    public Tanque() {
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getStatus() { return status;}

    public void setStatus(String status) {this.status = status; }
}
