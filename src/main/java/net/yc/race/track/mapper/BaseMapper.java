package net.yc.race.track.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@MapperConfig(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BaseMapper<E, D, S,B> {

    // Mapping to and from entity and DTO
    S toRequestDTO(E entity);
    D toResponseDTO(E entity);
    B toEmbededDTO(E entity);
    // Mapping from request DTO to entity and vice versa
    E toEntityD(S request);
    E toEntityS(D response);
    E toEntityB(B embeded);

    // Handle list of DTOs and entities
    List<D> toDTOs(List<E> entities);
    List<E> toEntities(List<D> dtos);
}
