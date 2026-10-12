package cl.partnercars.catalogo.controller;

import cl.partnercars.catalogo.dto.VehiculoRequest;
import cl.partnercars.catalogo.model.EstadoVehiculo;
import cl.partnercars.catalogo.model.Vehiculo;
import cl.partnercars.catalogo.model.VehiculoImagen;
import cl.partnercars.catalogo.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar(@RequestParam(required = false) Long marcaId,
                                                 @RequestParam(required = false) EstadoVehiculo estado) {
        log.debug("GET /vehiculos con marcaId={} y estado={}", marcaId, estado);
        return ResponseEntity.ok(vehiculoService.listar(marcaId, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtener(@PathVariable Long id) {
        log.debug("GET /vehiculos/{}", id);
        return ResponseEntity.ok(vehiculoService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody VehiculoRequest request) {
        log.debug("POST /vehiculos para la marca {}", request.getMarcaId());
        Vehiculo creado = vehiculoService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoRequest request) {
        log.debug("PUT /vehiculos/{}", id);
        return ResponseEntity.ok(vehiculoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /vehiculos/{}", id);
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/imagenes")
    public ResponseEntity<VehiculoImagen> agregarImagen(@PathVariable Long id, @Valid @RequestBody VehiculoImagen imagen) {
        log.debug("POST /vehiculos/{}/imagenes", id);
        VehiculoImagen creada = vehiculoService.agregarImagen(id, imagen);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{idImagen}")
                .buildAndExpand(creada.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @DeleteMapping("/{id}/imagenes/{idImagen}")
    public ResponseEntity<Void> eliminarImagen(@PathVariable Long id, @PathVariable Long idImagen) {
        log.debug("DELETE /vehiculos/{}/imagenes/{}", id, idImagen);
        vehiculoService.eliminarImagen(id, idImagen);
        return ResponseEntity.noContent().build();
    }
}
