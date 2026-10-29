package controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import View.Pesquisa_Carro;
import dao.CarroRepositorioBD;
import model.Carro;

public class PesquisaCarroController {

    private Pesquisa_Carro view;
    private CarroRepositorioBD repositorio;

    public PesquisaCarroController(Pesquisa_Carro view) {
        this.view = view;
        this.repositorio = new CarroRepositorioBD();
    }

    // =========================================================
    // PESQUISAR CARROS
    // =========================================================

    public void pesquisar() {

        try {

            String termo = view.getTermo();

            // Campo vazio mostra todos os carros disponíveis.
            // Carros já locados não aparecem na pesquisa.
            List<Carro> carros = termo.isEmpty()
                ? repositorio.listarDisponiveis()
                : repositorio.pesquisarDisponiveis(termo);

            DefaultTableModel modelo =
                (DefaultTableModel) view.getTable().getModel();

            // Limpa a tabela antes de carregar novamente
            modelo.setRowCount(0);

            for (Carro carro : carros) {

                modelo.addRow(
                    new Object[] {
                        carro.getRenavam(),
                        carro.getMarca(),
                        carro.getModelo(),
                        carro.getAno(),
                        carro.getPlaca(),
                        carro.getUsuario()
                    }
                );
            }

            view.mostrarTotal(carros.size());

        } catch (SQLException e) {

            view.mostrarMensagem(
                "Erro ao pesquisar os carros:\n"
                + e.getMessage()
            );
        }
    }
}
