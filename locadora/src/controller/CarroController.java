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

    // =========================================================
    // CADASTRAR CARRO
    // =========================================================

    public void cadastrar() {

        try {

            // Pega os valores digitados na tela
            String renavamTexto = view.getRenavam();
            String marca = view.getMarca();
            String modelo = view.getModelo();
            String anoTexto = view.getAno();
            String placa = view.getPlaca();

            // Verifica se os campos estão preenchidos
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

            // Converte RENAVAM e ANO para inteiro
            int renavam = Integer.parseInt(
                renavamTexto.trim()
            );

            int ano = Integer.parseInt(
                anoTexto.trim()
            );

            // Cria o objeto Carro
            Carro carro = new Carro();

            // Preenche os dados do carro
            carro.setRenavam(renavam);
            carro.setMarca(marca.trim());
            carro.setModelo(modelo.trim());
            carro.setAno(ano);
            carro.setPlaca(placa.trim());

            // Guarda quem cadastrou o carro
            carro.setUsuario(view.getUsuarioLogado());

            // Salva no banco
            repositorio.salvar(carro);

            // Mensagem de sucesso
            view.mostrarMensagem(
                "Carro cadastrado com sucesso!"
            );

            // Limpa os campos
            view.limparCampos();

            // Atualiza a tabela
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

    // =========================================================
    // LISTAR CARROS (SOMENTE OS DO USUÁRIO LOGADO)
    // =========================================================

    public void listar() {

        try {

            List<Carro> carros =
                repositorio.listarPorUsuario(
                    view.getUsuarioLogado()
                );

            DefaultTableModel modelo =
                (DefaultTableModel) view.getTable().getModel();

            // Limpa a tabela antes de carregar novamente
            modelo.setRowCount(0);

            // Adiciona os carros na tabela
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

