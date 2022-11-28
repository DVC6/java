package br.com.devices.entities;

public class ComponenteEntity {

    private Integer idComponente;
    private String modelo;
    private String totalComponente;
    private Integer fkTipoComponente;
    private Integer fkTotem;

    public ComponenteEntity() {
    }

    public ComponenteEntity(Integer idComponente, String modelo, String totalComponente, Integer fkTipoComponente, Integer fkTotem) {
        this.idComponente = idComponente;
        this.modelo = modelo;
        this.totalComponente = totalComponente;
        this.fkTipoComponente = fkTipoComponente;
        this.fkTotem = fkTotem;
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

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return String.format("Dados do Componente:"
                + "\nidComponente: %d,"
                + "\ntotalComponente: %s"
                + "\nfkTipoComponente: %d"
                + "\nfkTotem: %d"
                + "\nmodelo: %s",
                idComponente,totalComponente,fkTipoComponente,fkTotem,modelo);
    }
    
    
}
