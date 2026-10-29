package model;

import java.time.LocalDate;

public class Locacao {

    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private int renavam;

    // Usuário que locou o carro
    private String usuario;

    public Locacao() {
    }

    public Locacao(LocalDate dataInicio, LocalDate dataTermino, int renavam, String usuario) {
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.renavam = renavam;
        this.usuario = usuario;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public int getRenavam() {
        return renavam;
    }

    public void setRenavam(int renavam) {
        this.renavam = renavam;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String validar() {

        if (dataInicio == null) {
            return "Informe a data de início.";
        }

        if (dataTermino == null) {
            return "Informe a data de término.";
        }

        if (dataInicio.isBefore(LocalDate.now())) {
            return "A data de início não pode ser anterior a hoje.";
        }

        if (dataTermino.isBefore(dataInicio)) {
            return "A data de término não pode ser anterior à data de início.";
        }

        return null;
    }
}
