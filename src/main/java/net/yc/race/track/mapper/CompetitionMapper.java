package net.yc.race.track.mapper;

import net.yc.race.track.DTO.EmbededDTO.CompetitionEmbeded;
import net.yc.race.track.DTO.RequestDTO.CompetitionRequest;
import net.yc.race.track.DTO.ResponseDTO.CompetitionResponse;
import net.yc.race.track.model.Competition;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface CompetitionMapper extends BaseMapper<Competition, CompetitionRequest, CompetitionResponse,CompetitionEmbeded> {

}
