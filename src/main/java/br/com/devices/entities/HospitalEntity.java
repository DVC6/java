package br.com.devices.entities;

public class HospitalEntity {

    private Integer idHospital;
    private String nomeFantasia;
    private String cnpj;
    private String email;
    private String senha;
    private String site;
    private String logradouro;
    private Integer numero;
    private String cidade;
    private String estado;
    private String bairro;
    private String telefone;
    private String chave_acesso;
    private String cep;

    public HospitalEntity() {
    }

    public HospitalEntity(Integer idHospital, String nomeFantasia, String cnpj,
            String email, String senha, String site, String logradouro,
            Integer numero, String cidade, String estado, String bairro,
            String telefone, String chave_acesso, String cep) {
        this.idHospital = idHospital;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.email = email;
        this.senha = senha;
        this.site = site;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.bairro = bairro;
        this.telefone = telefone;
        this.chave_acesso = chave_acesso;
        this.cep = cep;
    }

    public Integer getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(Integer idHospital) {
        this.idHospital = idHospital;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getChave_acesso() {
        return chave_acesso;
    }

    public void setChave_acesso(String chave_acesso) {
        this.chave_acesso = chave_acesso;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return String.format("Dados do Hospital:"
                + "\nidHospital: %s"
                + "\nNpme: %s"
                + "\nCNPJ: %s"
                + "\nEmail: %s"
                + "\nSenha: %s"
                + "\nsite: %s"
                + "\nLogradouro: %s"
                + "\nNúmero: %d"
                + "\nCidade: %s"
                + "\nEstado: %s"
                + "\nBairro: %s"
                + "\nTelefone: %s"
                + "\nChave de Acesso: %s"
                + "\nCEP: %s",
                idHospital, nomeFantasia, cnpj, email, senha, site, logradouro,
                numero, cidade, estado, bairro, telefone, chave_acesso, cep);
    }

}
