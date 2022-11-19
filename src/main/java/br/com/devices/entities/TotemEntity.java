package br.com.devices.entities;

public class TotemEntity {

    private Integer idTotem;
    private String nomeMaquina;
    private Integer fkHospital;
    private String localizacao;
    private String identificadorUnico;

    public TotemEntity() {
    }

    public TotemEntity(Integer idTotem, String nomeMaquina, Integer fkHospital, String localizacao, String identificadorUnico) {
        this.idTotem = idTotem;
        this.nomeMaquina = nomeMaquina;
        this.fkHospital = fkHospital;
        this.localizacao = localizacao;
        this.identificadorUnico = identificadorUnico;
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

    public String getIdentificadorUnico() {
        return identificadorUnico;
    }

    public void setIdentificadorUnico(String identificadorUnico) {
        this.identificadorUnico = identificadorUnico;
    }

    @Override
    public String toString() {
        return String.format("Dados do totem:"
                + "\nidTotem: %s"
                + "\nNome: %s"
                + "\nfkHospital: %d"
                + "\nLocalização: %s",
                idTotem, nomeMaquina, fkHospital, localizacao);
    }

}
