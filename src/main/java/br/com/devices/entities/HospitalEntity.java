package br.com.devices.entities;

public class HospitalEntity {

    private Integer idHospital;

    public HospitalEntity() {
    }

    public Integer getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(Integer idHospital) {
        this.idHospital = idHospital;
    }

    @Override
    public String toString() {
        return String.format("%s", idHospital);
    }

}
