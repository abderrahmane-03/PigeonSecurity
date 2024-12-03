package net.yc.race.track.mapper;

import net.yc.race.track.DTO.EmbededDTO.CompetitionEmbeded;
import net.yc.race.track.DTO.EmbededDTO.ResultEmbeded;
import net.yc.race.track.DTO.RequestDTO.ResultRequest;
import net.yc.race.track.DTO.ResponseDTO.ResultResponse;
import net.yc.race.track.model.Result;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface ResultMapper extends BaseMapper<Result, ResultRequest, ResultResponse, ResultEmbeded> {

}
