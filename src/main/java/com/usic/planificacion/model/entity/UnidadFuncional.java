package com.usic.planificacion.model.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "unidad_funcional")
@Setter
@Getter
public class UnidadFuncional extends AuditoriaConfig {

    private static final long serialVersionUID = 2629195288020321924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUnidadFuncional;

    private String nombre;

    private String sigla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_poa")
    private Poa poa;

    @OneToMany(mappedBy = "unidadFuncional", fetch = FetchType.LAZY)
    private List<Persona> personas;

    @JsonIgnore
    @ManyToMany(mappedBy = "responsables", fetch = FetchType.LAZY)
    private Set<Indicador> indicadores = new HashSet<>();

}
