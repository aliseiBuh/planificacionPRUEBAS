package com.usic.planificacion.model.entity;

import java.util.List;

import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_calculo")
@Setter
@Getter
public class TipoCalculo extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoCalculo;

    private String nombre;

    @OneToMany(mappedBy = "tipoCalculo", fetch = FetchType.LAZY)
    private List<Indicador> indicadores;
}
