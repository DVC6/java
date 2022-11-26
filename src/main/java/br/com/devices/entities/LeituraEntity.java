package br.com.devices.entities;

import java.util.Date;

public class LeituraEntity {

    private Integer idLeitura;
    private Date dataHoraAtual;
    private Double consumo;
    private Double consumoQtd;
    private String tipo;
    private Integer fkComponente;

    // CONSTRUTOR
    public LeituraEntity(Integer idLeitura, Date dataHoraAtual, Double consumo, Double consumoQtd, String tipo, Integer fkComponente) {
        this.idLeitura = null;
        this.dataHoraAtual = new Date();
        this.consumo = consumo;
        this.consumoQtd = consumoQtd;
        this.tipo = tipo;
        this.fkComponente = fkComponente;
    }

    public LeituraEntity(Integer fkComponente) {
        this.idLeitura = null;
        this.dataHoraAtual = new Date();
        this.fkComponente = fkComponente;
    }

    // GET E SET
    public Integer getIdLeitura() {
        return idLeitura;
    }

    public void setIdLeitura(Integer idLeitura) {
        this.idLeitura = idLeitura;
    }

    public Date getDataHoraAtual() {
        return dataHoraAtual;
    }

    public void setDataHoraAtual(Date dataHoraAtual) {
        this.dataHoraAtual = dataHoraAtual;
    }

    public Double getConsumo() {
        return consumo;
    }

    public void setConsumo(Double consumo) {
        this.consumo = consumo;
    }

    public Double getConsumoQtd() { return consumoQtd; }

    public void setConsumoQtd(Double consumoQtd) { this.consumoQtd = consumoQtd; }

    public Integer getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(Integer fkComponente) {
        this.fkComponente = fkComponente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // TO STRING
    @Override
    public String toString() {
        return String.format("Histórico:"
                + "\nData e Horário: %s"
                + "\nConsumo Atual: %.2f",
                dataHoraAtual.toString(), consumo);
    }

}
