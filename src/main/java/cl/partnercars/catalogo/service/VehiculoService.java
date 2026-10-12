package cl.partnercars.catalogo.service;

import cl.partnercars.catalogo.dto.VehiculoRequest;
import cl.partnercars.catalogo.exception.RecursoNoEncontradoException;
import cl.partnercars.catalogo.model.EstadoVehiculo;
import cl.partnercars.catalogo.model.Marca;
import cl.partnercars.catalogo.model.Vehiculo;
import cl.partnercars.catalogo.model.VehiculoImagen;
import cl.partnercars.catalogo.repository.VehiculoImagenRepository;
import cl.partnercars.catalogo.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final VehiculoImagenRepository vehiculoImagenRepository;
    private final MarcaService marcaService;

    @Transactional(readOnly = true)
    public List<Vehiculo> listar(Long marcaId, EstadoVehiculo estado) {
        if (marcaId != null && estado != null) {
            return vehiculoRepository.findByMarcaIdAndEstadoOrderByIdAsc(marcaId, estado);
        }
        if (marcaId != null) {
            return vehiculoRepository.findByMarcaIdOrderByIdAsc(marcaId);
        }
        if (estado != null) {
            return vehiculoRepository.findByEstadoOrderByIdAsc(estado);
        }
        return vehiculoRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Vehiculo obtener(Long id) {
        return vehiculoRepository.findWithImagenesById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo con id " + id));
    }

    @Transactional
    public Vehiculo crear(VehiculoRequest request) {
        Marca marca = marcaService.obtener(request.getMarcaId());
        Vehiculo vehiculo = Vehiculo.builder()
                .marca(marca)
                .estado(request.getEstado())
                .imagenes(new ArrayList<>())
                .build();
        copiarDatos(request, vehiculo);
        Vehiculo creado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo creado: {} {} {} (id {})", marca.getNombre(), creado.getModelo(), creado.getAnio(), creado.getId());
        return creado;
    }

    @Transactional
    public Vehiculo actualizar(Long id, VehiculoRequest request) {
        Vehiculo vehiculo = obtener(id);
        vehiculo.setMarca(marcaService.obtener(request.getMarcaId()));
        if (request.getEstado() != null) {
            vehiculo.setEstado(request.getEstado());
        }
        copiarDatos(request, vehiculo);
        Vehiculo actualizado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo actualizado: id {} con estado {}", actualizado.getId(), actualizado.getEstado());
        return actualizado;
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = obtener(id);
        vehiculoRepository.delete(vehiculo);
        log.info("Vehículo eliminado: id {} con {} imágenes", id, vehiculo.getImagenes().size());
    }

    @Transactional
    public VehiculoImagen agregarImagen(Long idVehiculo, VehiculoImagen imagen) {
        Vehiculo vehiculo = obtener(idVehiculo);
        imagen.setId(null);
        imagen.setVehiculo(vehiculo);
        vehiculo.getImagenes().add(imagen);
        VehiculoImagen creada = vehiculoImagenRepository.save(imagen);
        log.info("Imagen {} agregada al vehículo {}", creada.getId(), idVehiculo);
        return creada;
    }

    @Transactional
    public void eliminarImagen(Long idVehiculo, Long idImagen) {
        VehiculoImagen imagen = vehiculoImagenRepository.findByIdAndVehiculoId(idImagen, idVehiculo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la imagen con id " + idImagen + " en el vehículo " + idVehiculo));
        vehiculoImagenRepository.delete(imagen);
        log.info("Imagen {} eliminada del vehículo {}", idImagen, idVehiculo);
    }

    private void copiarDatos(VehiculoRequest request, Vehiculo vehiculo) {
        vehiculo.setModelo(request.getModelo());
        vehiculo.setVersion(request.getVersion());
        vehiculo.setAnio(request.getAnio());
        vehiculo.setPrecio(request.getPrecio());
        vehiculo.setKilometraje(request.getKilometraje());
        vehiculo.setTransmision(request.getTransmision());
        vehiculo.setCombustible(request.getCombustible());
        vehiculo.setDescripcion(request.getDescripcion());
    }
}
