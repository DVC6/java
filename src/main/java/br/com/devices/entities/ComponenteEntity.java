package br.com.devices.entities;

public class ComponenteEntity {

    private Integer idComponente;
    private String totalComponente;
    private Integer fkTipoComponente;
    private Integer fkTotem;
    private Integer fkMetricas;

    public ComponenteEntity() {
    }

    public ComponenteEntity(Integer idComponente, String totalComponente, Integer fkTipoComponente, Integer fkTotem, Integer fkMetricas) {
        this.idComponente = idComponente;
        this.totalComponente = totalComponente;
        this.fkTipoComponente = fkTipoComponente;
        this.fkTotem = fkTotem;
        this.fkMetricas = fkMetricas;
    }

    public Integer getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    public String getTotalComponente() {
        return totalComponente;
    }

    public void setTotalComponente(String totalComponente) {
        this.totalComponente = totalComponente;
    }

    public Integer getFkTipoComponente() {
        return fkTipoComponente;
    }

    public void setFkTipoComponente(Integer fkTipoComponente) {
        this.fkTipoComponente = fkTipoComponente;
    }

    public Integer getFkTotem() {
        return fkTotem;
    }

    public void setFkTotem(Integer fkTotem) {
        this.fkTotem = fkTotem;
    }

    public Integer getFkMetricas() {
        return fkMetricas;
    }

    public void setFkMetricas(Integer fkMetricas) {
        this.fkMetricas = fkMetricas;
    }

    @Override
    public String toString() {
        return String.format("Dados do Componente:"
                + "\nidComponente: %d,"
                + "\ntotalComponente: %s"
                + "\nfkTipoComponente: %d"
                + "\nfkTotem: %d"
                + "\nfkMetricas: %d",
                idComponente,totalComponente,fkTipoComponente,fkTotem,fkMetricas);
    }
    
    
}
