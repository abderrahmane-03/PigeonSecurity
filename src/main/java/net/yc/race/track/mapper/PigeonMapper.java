package net.yc.race.track.mapper;

import net.yc.race.track.DTO.EmbededDTO.CompetitionEmbeded;
import net.yc.race.track.DTO.EmbededDTO.PigeonEmbeded;
import net.yc.race.track.DTO.RequestDTO.PigeonRequest;
import net.yc.race.track.DTO.ResponseDTO.PigeonResponse;
import net.yc.race.track.model.Pigeon;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface PigeonMapper extends BaseMapper<Pigeon, PigeonRequest, PigeonResponse, PigeonEmbeded> {

}
