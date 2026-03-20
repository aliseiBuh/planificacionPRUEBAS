package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.TipoCalculoDao;
import com.usic.planificacion.model.entity.TipoCalculo;

@Service
public class TipoCalculoServiceImpl implements TipoCalculoService{

    @Autowired
    private TipoCalculoDao dao;

    @Override
    public List<TipoCalculo> findAll() {
        return dao.findAll();
    }

    @Override
    public TipoCalculo findById(Long idEntidad) {
       return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(TipoCalculo entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        TipoCalculo tipo = dao.findById(idEntidad).orElse(null);
        tipo.setEstado("ELIMINADO");
        dao.save(tipo);
    }
    
}
