package com.pvp.codingtournament.mapper;

import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.model.TaskDto;
import java.util.ArrayList;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-27T13:38:00+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class TaskMapStructImpl implements TaskMapStruct {

    @Override
    public TaskDto entityToDto(TaskEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( entity.getId() );
        taskDto.setTitle( entity.getTitle() );
        taskDto.setDescription( entity.getDescription() );
        taskDto.setPoints( entity.getPoints() );
        ArrayList<String[]> arrayList = entity.getInputOutput();
        if ( arrayList != null ) {
            taskDto.setInputOutput( new ArrayList<String[]>( arrayList ) );
        }

        return taskDto;
    }

    @Override
    public TaskEntity dtoToEntity(TaskDto dto) {
        if ( dto == null ) {
            return null;
        }

        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setId( dto.getId() );
        taskEntity.setTitle( dto.getTitle() );
        taskEntity.setDescription( dto.getDescription() );
        taskEntity.setPoints( dto.getPoints() );
        ArrayList<String[]> arrayList = dto.getInputOutput();
        if ( arrayList != null ) {
            taskEntity.setInputOutput( new ArrayList<String[]>( arrayList ) );
        }

        return taskEntity;
    }
}
