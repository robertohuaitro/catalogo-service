package cl.partnercars.catalogo.service;

import cl.partnercars.catalogo.exception.ConflictoException;
import cl.partnercars.catalogo.exception.RecursoNoEncontradoException;
import cl.partnercars.catalogo.model.Marca;
import cl.partnercars.catalogo.repository.MarcaRepository;
import cl.partnercars.catalogo.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final VehiculoRepository vehiculoRepository;

    @Transactional(readOnly = true)
    public List<Marca> listar() {
        return marcaRepository.findAll(Sort.by("nombre"));
    }

    @Transactional(readOnly = true)
    public Marca obtener(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la marca con id " + id));
    }

    @Transactional
    public Marca crear(Marca marca) {
        if (marcaRepository.existsByNombreIgnoreCase(marca.getNombre())) {
            throw new ConflictoException("Ya existe la marca " + marca.getNombre());
        }
        marca.setId(null);
        Marca creada = marcaRepository.save(marca);
        log.info("Marca creada: {} (id {})", creada.getNombre(), creada.getId());
        return creada;
    }

    @Transactional
    public Marca actualizar(Long id, Marca datos) {
        Marca marca = obtener(id);
        if (marcaRepository.existsByNombreIgnoreCaseAndIdNot(datos.getNombre(), id)) {
            throw new ConflictoException("Ya existe otra marca con el nombre " + datos.getNombre());
        }
        marca.setNombre(datos.getNombre());
        marca.setPaisOrigen(datos.getPaisOrigen());
        marca.setLogoUrl(datos.getLogoUrl());
        Marca actualizada = marcaRepository.save(marca);
        log.info("Marca actualizada: {} (id {})", actualizada.getNombre(), actualizada.getId());
        return actualizada;
    }

    @Transactional
    public void eliminar(Long id) {
        Marca marca = obtener(id);
        if (vehiculoRepository.existsByMarcaId(id)) {
            throw new ConflictoException("No se puede eliminar la marca " + marca.getNombre() + " porque tiene vehículos asociados");
        }
        marcaRepository.delete(marca);
        log.info("Marca eliminada: {} (id {})", marca.getNombre(), id);
    }
}
