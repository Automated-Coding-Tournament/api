package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.model.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapStruct {

    TaskDto entityToDto(TaskEntity entity);

    TaskEntity dtoToEntity(TaskDto dto);
}
