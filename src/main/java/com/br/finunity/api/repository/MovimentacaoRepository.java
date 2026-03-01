package com.br.finunity.api.repository;

import com.br.finunity.api.model.Movimentacao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.br.finunity.api.model.TipoMovimentacao;

import java.time.LocalDate;
import java.util.List;

public interface MovimentacaoRepository 
extends JpaRepository<Movimentacao, Long> {

List<Movimentacao> findByData(LocalDate data);
List<Movimentacao> findTop5ByOrderByDataDesc();

List<Movimentacao> findByTipo(TipoMovimentacao tipo);

@Query("SELECT COALESCE(SUM(m.valor),0) FROM Movimentacao m WHERE m.tipo = 'ENTRADA'")
Double totalEntradas();

@Query("SELECT COALESCE(SUM(m.valor),0) FROM Movimentacao m WHERE m.tipo = 'SAIDA'")
Double totalSaidas();

@Query("""
   SELECT m FROM Movimentacao m
   WHERE (:descricao IS NULL OR LOWER(m.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
   AND (:cliente IS NULL OR LOWER(m.cliente) LIKE LOWER(CONCAT('%', :cliente, '%')))
   AND (:tipo IS NULL OR m.tipo = :tipo)
   AND (:dataInicio IS NULL OR m.data >= :dataInicio)
   AND (:dataFim IS NULL OR m.data <= :dataFim)
   ORDER BY m.data DESC
   """)
List<Movimentacao> filtrar(
    @Param("descricao") String descricao,
    @Param("cliente") String cliente,
    @Param("tipo") TipoMovimentacao tipo,
    @Param("dataInicio") LocalDate dataInicio,
    @Param("dataFim") LocalDate dataFim
);
}