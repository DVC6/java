package br.com.devices.entities;

public class TotemEntity {

    private Integer idTotem;

    public TotemEntity() {
    }

    public Integer getIdTotem() {
        return idTotem;
    }

    public void setIdTotem(Integer idHospital) {
        this.idTotem = idTotem;
    }

    @Override
    public String toString() {
        return String.format("%s", idTotem);
    }

}
