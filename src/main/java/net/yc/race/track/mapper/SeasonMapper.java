package net.yc.race.track.mapper;

import net.yc.race.track.DTO.EmbededDTO.SeasonEmbeded;
import net.yc.race.track.DTO.RequestDTO.SeasonRequest;
import net.yc.race.track.model.Season;
import net.yc.race.track.DTO.ResponseDTO.SeasonResponse;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface SeasonMapper extends BaseMapper<Season, SeasonResponse, SeasonRequest, SeasonEmbeded> {
}
