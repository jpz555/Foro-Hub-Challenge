package com.alurachallenges.forohub.modelo.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Page<Topico> findByStatusTrue(Pageable paginacion);

    @Query("""
            SELECT  t 
            FROM Topico t
            WHERE t.estado IN ('ACTIVO','ACTUALIZADO')
            """)
    Page<Topico> findByEstado(Pageable paginacion);

    @Query("""
            SELECT t
            FROM Topico t
            WHERE (:titulo IS NULL OR t.titulo LIKE %:titulo%)
                AND
                (:fechaDeInicio IS NULL OR t.fechaDeCreacion >= :fechaDeInicio)
            """)
    Page<Topico> findByTituloAndFecha(Pageable paginacion,
                                      @Param("titulo") String titulo,
                                      @Param("fechaDeInicio") LocalDateTime fechaDeInicio
                                      );

    @Query("""
            SELECT t.fechaDeCreacion 
            FROM Topico t
            ORDER BY t.fechaDeCreacion
            LIMIT 1
            """)
    LocalDateTime findByFechaDeCreacion();

    @Query("""
            SELECT  t 
            FROM Topico t
            WHERE t.estado IN ('ACTIVO','ACTUALIZADO') 
            AND  t.usuario.id = :id
            """)
    Page<Topico> findByEstadoAndId(Long id, Pageable paginacion);
}
