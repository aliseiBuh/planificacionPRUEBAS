package com.usic.planificacion.model.entity;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "indicador")
@Setter
@Getter
public class Indicador extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIndicador;
    private Integer ponderacion;
    private Integer codigo;
    private String denominacion;

    private Integer lineaBase;
    private Integer gestionBase;
    private Integer total;
    @Column(scale = 2)
    private Double totalDecimal;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_objetivo")
    private Objetivo objetivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_indicador")
    private CategoriaIndicador categoriaIndicador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_indicador")
    private TipoIndicador tipoIndicador;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad_funcional")
    private UnidadFuncional unidadFuncional;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "indicador_unidad", joinColumns = @JoinColumn(name = "id_indicador"), inverseJoinColumns = @JoinColumn(name = "id_unidad"))
    private Set<UnidadFuncional> responsables;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_calculo")
    private TipoCalculo tipoCalculo;

    @JsonIgnore
    @OneToMany(mappedBy = "indicador", fetch = FetchType.EAGER)
    private List<Meta> metas;

    @JsonIgnore
    @OneToMany(mappedBy = "indicador",fetch = FetchType.LAZY)
    private List<Resultado> resultados;

    @JsonIgnore
    public List<Meta> getMetasOrdenadas() {
        return metas.stream()
            .filter(meta -> !"ELIMINADO".equals(meta.getEstado())) // Filtrar estados eliminados
            .sorted(Comparator.comparingInt(Meta::getGestion)) // Ordenar por gestión
            .collect(Collectors.toList());
    }

}
