package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.CategoriaIndicadorDao;
import com.usic.planificacion.model.entity.CategoriaIndicador;

@Service
public class CategoriaIndicadorServiceImpl implements CategoriaIndicadorService{

    @Autowired
    private CategoriaIndicadorDao dao;

    @Override
    public List<CategoriaIndicador> findAll() {
        return dao.findAll();
    }

    @Override
    public CategoriaIndicador findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(CategoriaIndicador entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        CategoriaIndicador categoriaIndicador = dao.findById(idEntidad).orElse(null);
        categoriaIndicador.setEstado("ELIMINADO");
        dao.save(categoriaIndicador);
    }
    
}
