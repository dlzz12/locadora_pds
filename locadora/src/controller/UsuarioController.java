package controller;

import java.sql.SQLException;

import dao.UsuarioRepositorioBD;
import model.Usuario;

public class UsuarioController {

    private UsuarioRepositorioBD repositorio;

    public UsuarioController() throws SQLException {
        this.repositorio = new UsuarioRepositorioBD();
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public boolean usuarioExiste(String usuario) throws SQLException {
        return repositorio.existe(usuario);
    }

    public boolean autenticar(String usuario, String senha) throws SQLException {
        return repositorio.autenticar(usuario, senha);
    }

    // =========================================================
    // CADASTRO
    // =========================================================

    public void cadastrar(String usuario, String senha) throws SQLException {
        repositorio.salvar(new Usuario(usuario, senha));
    }
}
