package com.br.finunity.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.br.finunity.api.model.Usuario;
import com.br.finunity.api.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    public LoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String usuario,
                             @RequestParam String senha,
                             HttpSession session) {

        Usuario user = usuarioRepository
                .findByUsuarioAndSenha(usuario, senha);

        if (user != null) {

            session.setAttribute("usuarioLogado", user);

            if (user.getPerfil().equals("ADMIN")) {
                return "redirect:/admin/menu";
            } else {
                return "redirect:/usuario/menu";
            }
        }

        return "redirect:/login?erro";
    }
}
