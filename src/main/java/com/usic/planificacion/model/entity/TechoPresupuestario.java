package com.usic.planificacion.model.entity;

import java.util.Date;

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
@Table(name = "techo_presupuestario")
@Setter
@Getter
public class TechoPresupuestario extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTechoPresupuesto;

     @Column(scale = 2)
    private Double montoAsignado;
    @Column(scale = 2)
    private Double montoEjecutado;

    private Integer gestion;
    
    private Date fechaEjecucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad_funcional")
    private UnidadFuncional unidadFuncional;
}
