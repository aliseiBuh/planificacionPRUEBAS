package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.PoaDao;
import com.usic.planificacion.model.entity.Poa;

@Service
public class PoaServiceImpl implements PoaService{

    @Autowired
    private PoaDao dao;

    @Override
    public List<Poa> findAll() {
        return dao.findAll();
    }

    @Override
    public Poa findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Poa entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Poa poa = dao.findById(idEntidad).orElse(null);
        poa.setEstado("ELIMINADO");
        dao.save(poa);
    }
    
}
