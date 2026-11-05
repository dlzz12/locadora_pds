package controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import View.Cadastro_Carro;
import dao.CarroRepositorioBD;
import dao.LocacaoRepositorioBD;
import model.Carro;
import model.Locacao;
import model.Moeda;

public class CarroController {
    public static final String LOCADO = "LOCADO";

    private static final String DISPONIVEL = "Disponível";

    private Cadastro_Carro view;
    private CarroRepositorioBD repositorio;
    private LocacaoRepositorioBD repositorioLocacao;
    private List<Carro> carros = new ArrayList<>();

    public CarroController(Cadastro_Carro view) {
        this.view = view;
        this.repositorio = new CarroRepositorioBD();
        this.repositorioLocacao = new LocacaoRepositorioBD();
    }

    public void cadastrar() {
        try {
            String renavamTexto = view.getRenavam();
            String marca = view.getMarca();
            String modelo = view.getModelo();
            String anoTexto = view.getAno();
            String placa = view.getPlaca();
            String precoTexto = view.getPrecoDia();

            if (renavamTexto == null || renavamTexto.trim().isEmpty() ||
                marca == null || marca.trim().isEmpty() ||
                modelo == null || modelo.trim().isEmpty() ||
                anoTexto == null || anoTexto.trim().isEmpty() ||
                placa == null || placa.trim().isEmpty() ||
                precoTexto == null || precoTexto.trim().isEmpty()) {
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

            BigDecimal precoDia;

            try {
                precoDia = Moeda.converter(precoTexto);
            } catch (NumberFormatException e) {
                view.mostrarMensagem(
                    "Preço por dia inválido. Use o formato 150,00."
                );

                return;
            }

            Carro carro = new Carro();

            carro.setRenavam(renavam);
            carro.setMarca(marca.trim());
            carro.setModelo(modelo.trim());
            carro.setAno(ano);
            carro.setPlaca(placa.trim());
            carro.setPrecoDia(precoDia);

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
            carregarTabela();
        } catch (SQLException e) {
            view.mostrarMensagem(
                "Erro ao listar os carros:\n"
                + e.getMessage()
            );
        }
    }

    public void atualizarSemAviso() {
        try {
            carregarTabela();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar os carros: " + e.getMessage());
        }
    }

    private void carregarTabela() throws SQLException {
        carros = repositorio.listarPorUsuario(
            view.getUsuarioLogado()
        );

        Set<Integer> locados =
            repositorioLocacao.listarRenavamsLocados();

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
                    carro.getPlaca(),
                    Moeda.formatar(carro.getPrecoDia()),
                    locados.contains(carro.getRenavam()) ? LOCADO : DISPONIVEL
                }
            );
        }
    }

    public void mostrarLocacao(int linha) {
        Carro carro = carros.get(linha);

        try {
            Locacao locacao =
                repositorioLocacao.buscarLocacaoAtiva(carro.getRenavam());

            if (locacao == null) {
                return;
            }

            view.mostrarMensagem(
                carro.getMarca() + " " + carro.getModelo()
                + " - Placa " + carro.getPlaca() + "\n\n"
                + "Locado por: " + locacao.getUsuario() + "\n"
                + "Data de locação: " + LocacaoController.formatar(locacao.getDataInicio()) + "\n"
                + "Data de término: " + LocacaoController.formatar(locacao.getDataTermino()) + "\n"
                + "Período: " + LocacaoController.textoDias(locacao.getDias()) + "\n"
                + "Preço por dia: " + Moeda.formatar(carro.getPrecoDia()) + "\n"
                + "Valor total: " + Moeda.formatar(
                    LocacaoController.calcularTotal(carro.getPrecoDia(), locacao.getDias())
                )
            );
        } catch (SQLException e) {
            view.mostrarMensagem(
                "Erro ao buscar a locação:\n"
                + e.getMessage()
            );
        }
    }
}
