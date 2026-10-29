package model;

import java.util.ArrayList;
import java.util.List;

import model.Carro;

public class CarroRepositorio {

    private List<Carro> carros = new ArrayList<>();

    public void salvar(Carro carro) {

        if (carro.validar() != null) {
            throw new IllegalArgumentException(carro.validar());
        }

        if (existe(carro.getPlaca())) {
            throw new IllegalArgumentException("Já existe um carro cadastrado com essa placa.");
        }

        carros.add(carro);
    }

    public boolean existe(String placa) {

        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }

        for (Carro carro : carros) {
            if (placa.equalsIgnoreCase(carro.getPlaca())) {
                return true;
            }
        }

        return false;
    }

    public List<Carro> listarTodos() {
        return carros;
    }

    public int contar() {
        return carros.size();
    }
}