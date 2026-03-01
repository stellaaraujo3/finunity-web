package com.br.finunity.api.controller;

import com.br.finunity.api.model.Movimentacao;
import com.br.finunity.api.model.TipoMovimentacao;
import com.br.finunity.api.repository.MovimentacaoRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoRepository repository;

    public MovimentacaoController(MovimentacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String cliente,
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        List<Movimentacao> movimentacoes = repository.filtrar(
                descricao, cliente, tipo, dataInicio, dataFim);

        model.addAttribute("movimentacoes", movimentacoes);
        model.addAttribute("movimentacao", new Movimentacao());

        return "admin/movimentacoes";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Movimentacao mov) {
        repository.save(mov);
        return "redirect:/admin/movimentacoes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Movimentacao mov = repository.findById(id).orElseThrow();
        model.addAttribute("movimentacao", mov);
        model.addAttribute("movimentacoes", repository.findAll());
        return "admin/movimentacoes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin/movimentacoes";
    }
}