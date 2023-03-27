package com.pvp.codingtournament.mapper;

import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.model.TaskDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapStruct {

    TaskDto entityToDto(TaskEntity entity);

    TaskEntity dtoToEntity(TaskDto dto);
}
