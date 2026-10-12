package cl.partnercars.catalogo.repository;

import cl.partnercars.catalogo.model.EstadoVehiculo;
import cl.partnercars.catalogo.model.Vehiculo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    @EntityGraph(attributePaths = {"marca", "imagenes"})
    List<Vehiculo> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = {"marca", "imagenes"})
    List<Vehiculo> findByMarcaIdOrderByIdAsc(Long marcaId);

    @EntityGraph(attributePaths = {"marca", "imagenes"})
    List<Vehiculo> findByEstadoOrderByIdAsc(EstadoVehiculo estado);

    @EntityGraph(attributePaths = {"marca", "imagenes"})
    List<Vehiculo> findByMarcaIdAndEstadoOrderByIdAsc(Long marcaId, EstadoVehiculo estado);

    @EntityGraph(attributePaths = {"marca", "imagenes"})
    Optional<Vehiculo> findWithImagenesById(Long id);

    boolean existsByMarcaId(Long marcaId);
}
