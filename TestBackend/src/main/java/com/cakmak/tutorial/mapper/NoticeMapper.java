package com.cakmak.tutorial.mapper;

import com.cakmak.tutorial.models.entity.NoticeEntity;
import com.cakmak.tutorial.payload.request.notice.NoticeRequest;
import com.cakmak.tutorial.payload.response.notice.NoticeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NoticeMapper {

    @Mappings({@Mapping(source="id",target="id", qualifiedByName = "requestToEntityConvertById")})
    NoticeEntity requestToEntity(NoticeRequest item);
    @Mappings({@Mapping(source="id",target="id", qualifiedByName = "entityToResponseConvertById")})
    NoticeResponse entityToResponse(NoticeEntity item);
    @Mappings({@Mapping(source="list.id",target="id", qualifiedByName = "entityToResponseConvertById")})
    List<NoticeResponse> entityListToResponseList(List<NoticeEntity> list);

    @Named("requestToEntityConvertById")
    public static long requestToEntityConvertById(Long id){
        if(Objects.nonNull(id)) {
              return id.longValue();
        }
        return 0;
    }

    @Named("entityToResponseConvertById")
    public static Long entityToResponseConvertById(long id){
        return Long.valueOf(id);
    }
}
