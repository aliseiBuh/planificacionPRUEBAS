package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.MetaDao;
import com.usic.planificacion.model.entity.Meta;

@Service
public class MetaServiceImpl implements MetaService{

    @Autowired
    private MetaDao dao;

    @Override
    public List<Meta> findAll() {
        return(List<Meta>) dao.findAll();
    }

    @Override
    public Meta findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Meta entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Meta meta = dao.findById(idEntidad).orElse(null);
        meta.setEstado("ELIMINADO");
        dao.save(meta);
    }

    @Override
    public List<Meta> listarMetasPorIndicador(Long idIndicador) {
        return dao.listarMetasPorIndicador(idIndicador);
    }

    @Override
    public List<Meta> listaMetasP() {
        return(List<Meta>) dao.listaMetasP();
    }
    
}
