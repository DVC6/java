package br.com.devices.entities;

public class TotemEntity {

    private Integer idTotem;
    private String nomeMaquina;
    private String statusAtual;
    private Integer fkHospital;
    private String localizacao;

    public TotemEntity() {
    }

    public TotemEntity(Integer idTotem, String nomeMaquina, String statusAtual, Integer fkHospital, String localizacao) {
        this.idTotem = idTotem;
        this.nomeMaquina = nomeMaquina;
        this.statusAtual = statusAtual;
        this.fkHospital = fkHospital;
        this.localizacao = localizacao;
    }

    public Integer getIdTotem() {
        return idTotem;
    }

    public void setIdTotem(Integer idTotem) {
        this.idTotem = idTotem;
    }

    public String getNomeMaquina() {
        return nomeMaquina;
    }

    public void setNomeMaquina(String nomeMaquina) {
        this.nomeMaquina = nomeMaquina;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    public Integer getFkHospital() {
        return fkHospital;
    }

    public void setFkHospital(Integer fkHospital) {
        this.fkHospital = fkHospital;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {
        return String.format("Dados do totem:"
                + "\nidTotem: %s"
                + "\nNome: %s"
                + "\nStatus: %s"
                + "\nfkHospital: %d"
                + "\nLocalização: %s",
                idTotem, nomeMaquina, statusAtual, fkHospital, localizacao);
    }

}
