package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.UnidadFuncionalDao;
import com.usic.planificacion.model.entity.UnidadFuncional;

@Service
public class UnidadFuncionalServiceImpl implements UnidadFuncionalService{
    
    @Autowired
    private UnidadFuncionalDao dao;

    @Override
    public List<UnidadFuncional> findAll() {
        return dao.findAll();
    }

    @Override
    public UnidadFuncional findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(UnidadFuncional entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        UnidadFuncional unidadFuncional = dao.findById(idEntidad).orElse(null);
        unidadFuncional.setEstado("ELIMINADO");
        dao.save(unidadFuncional);
    }

    @Override
    public UnidadFuncional buscarUnidadPorNombre(String nombre) {
        return dao.buscarUnidadPorNombre(nombre);
    }

    @Override
    public List<UnidadFuncional> listarUnidadPorIdIndicador(Long idIndicador) {
        // TODO Auto-generated method stub
        return dao.listarUnidadPorIdIndicador(idIndicador);
    }

}
