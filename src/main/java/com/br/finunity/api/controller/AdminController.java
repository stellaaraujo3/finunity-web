package com.br.finunity.api.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.br.finunity.api.model.Movimentacao;
import com.br.finunity.api.model.Usuario;
import com.br.finunity.api.repository.MovimentacaoRepository;
import com.br.finunity.api.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final MovimentacaoRepository repository;

    // 🔥 CONSTRUTOR ÚNICO
    public AdminController(UsuarioRepository usuarioRepository,
                           MovimentacaoRepository repository) {

        this.usuarioRepository = usuarioRepository;
        this.repository = repository;
    }

    @GetMapping("/menu")
    public String dashboard(Model model) {

        Double entradas = repository.totalEntradas();
        Double saidas = repository.totalSaidas();
        Double saldo = entradas - saidas;

        model.addAttribute("saldo", saldo);
        model.addAttribute("entradas", entradas);
        model.addAttribute("saidas", saidas);

        List<Movimentacao> movimentacoes = repository.findAll();

        Map<Integer, Double> valoresPorMes = movimentacoes.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getData().getMonthValue(),
                        Collectors.summingDouble(Movimentacao::getValor)
                ));

        model.addAttribute("valoresPorMes", valoresPorMes);

        // 🔥 enviar lista real para tabela
        model.addAttribute("movimentacoes", movimentacoes);

        return "admin/menu";
    }
    
    

    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/cadastro-usuario";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@RequestParam String nome,
                                @RequestParam String usuario,
                                @RequestParam String senha,
                                @RequestParam String perfil) {

        Usuario novo = new Usuario();
        novo.setNome(nome);
        novo.setUsuario(usuario);
        novo.setSenha(senha);
        novo.setPerfil(perfil);

        usuarioRepository.save(novo);

        return "redirect:/admin/menu";
    }
}