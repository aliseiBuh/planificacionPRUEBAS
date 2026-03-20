package com.usic.planificacion.model.entity;

import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "avance")
@Setter
@Getter
public class Avance extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvance;

    private Integer ejecutado;

    @Column(scale = 2)
    private Double ejecutadoDecimal;

    @Column(scale = 2)
    private Double gradoAvance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_meta")
    private Meta meta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad_funcional")
    private UnidadFuncional unidadFuncional;

    private String evaluacionCualitativa;

}
