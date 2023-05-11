package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.model.task.TaskDto;
import java.util.ArrayList;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-11T02:57:40+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
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
        taskDto.setMethodName( entity.getMethodName() );
        ArrayList<String> arrayList = entity.getMethodArguments();
        if ( arrayList != null ) {
            taskDto.setMethodArguments( new ArrayList<String>( arrayList ) );
        }
        ArrayList<String> arrayList1 = entity.getMethodArgumentTypes();
        if ( arrayList1 != null ) {
            taskDto.setMethodArgumentTypes( new ArrayList<String>( arrayList1 ) );
        }
        taskDto.setReturnType( entity.getReturnType() );
        ArrayList<String[]> arrayList2 = entity.getInputOutput();
        if ( arrayList2 != null ) {
            taskDto.setInputOutput( new ArrayList<String[]>( arrayList2 ) );
        }
        taskDto.setLanguage( entity.getLanguage() );

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
        taskEntity.setMethodName( dto.getMethodName() );
        ArrayList<String> arrayList = dto.getMethodArguments();
        if ( arrayList != null ) {
            taskEntity.setMethodArguments( new ArrayList<String>( arrayList ) );
        }
        ArrayList<String> arrayList1 = dto.getMethodArgumentTypes();
        if ( arrayList1 != null ) {
            taskEntity.setMethodArgumentTypes( new ArrayList<String>( arrayList1 ) );
        }
        taskEntity.setReturnType( dto.getReturnType() );
        ArrayList<String[]> arrayList2 = dto.getInputOutput();
        if ( arrayList2 != null ) {
            taskEntity.setInputOutput( new ArrayList<String[]>( arrayList2 ) );
        }
        taskEntity.setLanguage( dto.getLanguage() );

        return taskEntity;
    }
}
