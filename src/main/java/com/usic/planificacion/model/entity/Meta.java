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
@Table(name = "meta")
@Setter
@Getter
public class Meta extends AuditoriaConfig{

    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMeta;
    private Integer resultado;

    @Column(scale = 2)
    private Double resultadoDecimal;
    private Integer gestion;

    @Column(scale = 2)
    private Double gradoAvance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_indicador")
    private Indicador indicador;

    @Override
public String toString() {
    return "Meta{id=" + idMeta + ", resultado=" + resultado + ", resultadoDecimal=" + resultadoDecimal + ", gestion=" + gestion + ", gradoAvance=" + gradoAvance + "}";
}
    
}
