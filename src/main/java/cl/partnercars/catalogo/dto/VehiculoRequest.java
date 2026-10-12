package cl.partnercars.catalogo.dto;

import cl.partnercars.catalogo.model.EstadoVehiculo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoRequest {

    @NotNull(message = "La marca es obligatoria")
    private Long marcaId;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 100, message = "El modelo no puede superar los 100 caracteres")
    private String modelo;

    @Size(max = 100, message = "La versión no puede superar los 100 caracteres")
    private String version;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1950, message = "El año no puede ser anterior a 1950")
    @Max(value = 2100, message = "El año no puede ser posterior a 2100")
    private Integer anio;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Long precio;

    @NotNull(message = "El kilometraje es obligatorio")
    @PositiveOrZero(message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    @NotBlank(message = "La transmisión es obligatoria")
    @Size(max = 30, message = "La transmisión no puede superar los 30 caracteres")
    private String transmision;

    @NotBlank(message = "El combustible es obligatorio")
    @Size(max = 30, message = "El combustible no puede superar los 30 caracteres")
    private String combustible;

    @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres")
    private String descripcion;

    private EstadoVehiculo estado;
}
