package cl.partnercars.catalogo.repository;

import cl.partnercars.catalogo.model.VehiculoImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculoImagenRepository extends JpaRepository<VehiculoImagen, Long> {

    Optional<VehiculoImagen> findByIdAndVehiculoId(Long id, Long vehiculoId);
}
