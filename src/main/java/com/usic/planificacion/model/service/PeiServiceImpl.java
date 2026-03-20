package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.PeiDao;
import com.usic.planificacion.model.entity.Pei;

@Service
public class PeiServiceImpl implements PeiService {

    @Autowired
    private PeiDao dao;

    @Override
    public List<Pei> findAll() {
        return dao.findAll();
    }

    @Override
    public Pei findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Pei entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Pei pei = dao.findById(idEntidad).orElse(null);
        pei.setEstado("ELIMINADO");
        dao.save(pei);
    }
    
}
