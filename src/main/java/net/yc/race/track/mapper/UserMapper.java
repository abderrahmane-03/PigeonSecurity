package net.yc.race.track.mapper;

import net.yc.race.track.DTO.EmbededDTO.UserEmbeded;
import net.yc.race.track.DTO.RequestDTO.UserRequest;
import net.yc.race.track.DTO.ResponseDTO.UserResponse;
import net.yc.race.track.model.User;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface UserMapper extends BaseMapper<User, UserResponse, UserRequest, UserEmbeded> {
}
