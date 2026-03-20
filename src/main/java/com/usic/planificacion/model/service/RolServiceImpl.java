package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.RolDao;
import com.usic.planificacion.model.entity.Rol;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolDao rolDao;

    @Override
    public List<Rol> findAll() {
        return rolDao.findAll();
    }

    @Override
    public void save(Rol rol) {
        rolDao.save(rol);
    }

    @Override
    public Rol findById(Long id) {
        return rolDao.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        Rol rol = rolDao.findById(id).orElse(null);
        rol.setEstado("ELIMINADO");
        rolDao.save(rol);
    }

    @Override
    public Rol buscaPorNombre(String nombre) {
        return rolDao.findByNombre(nombre);
    }
    
}
