package model;

public class Usuario {

    private int id;
    private String usuario;
    private String senha;

    public Usuario() {
    }

    public Usuario(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String validar() {

        if (usuario == null || usuario.trim().isEmpty()) {
            return "Informe o usuário.";
        }

        if (usuario.trim().length() > 50) {
            return "O usuário deve possuir no máximo 50 caracteres.";
        }

        if (senha == null || senha.isEmpty()) {
            return "Informe a senha.";
        }

        if (senha.length() < 4) {
            return "A senha deve possuir pelo menos 4 caracteres.";
        }

        return null;
    }
}
