package controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import View.Cadastro_Carro;
import dao.CarroRepositorioBD;
import model.Carro;

public class CarroController {
    private Cadastro_Carro view;
    private CarroRepositorioBD repositorio;

    public CarroController(Cadastro_Carro view) {
        this.view = view;
        this.repositorio = new CarroRepositorioBD();
    }

    public void cadastrar() {
        try {
            String renavamTexto = view.getRenavam();
            String marca = view.getMarca();
            String modelo = view.getModelo();
            String anoTexto = view.getAno();
            String placa = view.getPlaca();

            if (renavamTexto == null || renavamTexto.trim().isEmpty() ||
                marca == null || marca.trim().isEmpty() ||
                modelo == null || modelo.trim().isEmpty() ||
                anoTexto == null || anoTexto.trim().isEmpty() ||
                placa == null || placa.trim().isEmpty()) {
                view.mostrarMensagem(
                    "Por favor, preencha todos os campos obrigatórios."
                );

                return;
            }

            int renavam = Integer.parseInt(
                renavamTexto.trim()
            );

            int ano = Integer.parseInt(
                anoTexto.trim()
            );

            Carro carro = new Carro();

            carro.setRenavam(renavam);
            carro.setMarca(marca.trim());
            carro.setModelo(modelo.trim());
            carro.setAno(ano);
            carro.setPlaca(placa.trim());

            carro.setUsuario(view.getUsuarioLogado());

            repositorio.salvar(carro);

            view.mostrarMensagem(
                "Carro cadastrado com sucesso!"
            );

            view.limparCampos();

            listar();
        } catch (NumberFormatException e) {
            view.mostrarMensagem(
                "RENAVAM e Ano devem conter apenas números."
            );
        } catch (IllegalArgumentException e) {
            view.mostrarMensagem(
                e.getMessage()
            );
        } catch (SQLException e) {
            view.mostrarMensagem(
                "Erro ao acessar o banco de dados:\n"
                + e.getMessage()
            );
        }
    }

    public void listar() {
        try {
            List<Carro> carros =
                repositorio.listarPorUsuario(
                    view.getUsuarioLogado()
                );

            DefaultTableModel modelo =
                (DefaultTableModel) view.getTable().getModel();

            modelo.setRowCount(0);

            for (Carro carro : carros) {
                modelo.addRow(
                    new Object[] {
                        carro.getRenavam(),
                        carro.getMarca(),
                        carro.getModelo(),
                        carro.getAno(),
                        carro.getPlaca()
                    }
                );
            }
        } catch (SQLException e) {
            view.mostrarMensagem(
                "Erro ao listar os carros:\n"
                + e.getMessage()
            );
        }
    }
}
