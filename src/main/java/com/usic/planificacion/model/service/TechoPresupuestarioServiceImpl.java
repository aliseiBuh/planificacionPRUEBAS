package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.TechoPresupuestarioDao;
import com.usic.planificacion.model.entity.TechoPresupuestario;

@Service
public class TechoPresupuestarioServiceImpl implements TechoPresupuestarioService{
    @Autowired
    private TechoPresupuestarioDao dao;

    @Override
    public List<TechoPresupuestario> findAll() {
        return dao.findAll();
    }

    @Override
    public TechoPresupuestario findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(TechoPresupuestario entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        TechoPresupuestario techoPresupuestario = dao.findById(idEntidad).orElse(null);
        techoPresupuestario.setEstado("ELIMINADO");
        dao.save(techoPresupuestario);
    }


}
