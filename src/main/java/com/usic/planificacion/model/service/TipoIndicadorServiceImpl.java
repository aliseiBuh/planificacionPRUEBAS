package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.TipoIndicadorDao;
import com.usic.planificacion.model.entity.TipoIndicador;

@Service
public class TipoIndicadorServiceImpl implements TipoIndicadorService{

    @Autowired
    private TipoIndicadorDao dao;

    @Override
    public List<TipoIndicador> findAll() {
        return dao.findAll();
    }

    @Override
    public TipoIndicador findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(TipoIndicador entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        TipoIndicador tipoIndicador = dao.findById(idEntidad).orElse(null);
        tipoIndicador.setEstado("ELIMINADO");
        dao.save(tipoIndicador);
    }
    
}
