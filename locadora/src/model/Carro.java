package model;

public class Carro {

    private int renavam;
    private String marca;
    private String modelo;
    private int ano;
    private String placa;

    // Usuário que cadastrou o carro
    private String usuario;

    public Carro() {
    }

    public Carro(String marca, String modelo, int ano, String placa) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.placa = placa;
    }

    public Carro(int renavam, String marca, String modelo, int ano, String placa) {
        this.renavam = renavam;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.placa = placa;
    }

    public int getRenavam() {
        return renavam;
    }

    public void setRenavam(int renavam) {
        this.renavam = renavam;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String validar() {

        if (marca == null || marca.trim().isEmpty()) {
            return "Preencha a marca do veículo.";
        }

        if (modelo == null || modelo.trim().isEmpty()) {
            return "Preencha o modelo do veículo.";
        }

        if (ano <= 0) {
            return "Preencha um ano válido.";
        }

        if (placa != null && !placa.trim().isEmpty() && placa.length() > 7) {
            return "A placa deve possuir no máximo 7 caracteres.";
        }

        return null;
    }
}