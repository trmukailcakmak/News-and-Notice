package com.cakmak.tutorial.mapper;

import com.cakmak.tutorial.models.entity.NewsEntity;
import com.cakmak.tutorial.payload.request.news.NewsRequest;
import com.cakmak.tutorial.payload.response.news.NewsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mappings({@Mapping(source="id",target="id", qualifiedByName = "requestToEntityConvertLontTolong")})
    NewsEntity  requestToEntity(NewsRequest newsRequest);
    @Mappings({@Mapping(source="id",target="id", qualifiedByName = "entityToResponseConvertIdlongToIdLong")})
    NewsResponse entityToResponse(NewsEntity newsEntity);
    @Mappings({@Mapping(source="newsEntityList.id",target="id", qualifiedByName = "entityToResponseConvertIdlongToIdLong")})
    List<NewsResponse> entityListToResponseList(List<NewsEntity> newsEntityList);



      @Named("requestToEntityConvertLontTolong")
      public static long requestToEntityConvertLontTolong(Long id){
          if(Objects.nonNull(id)) {
              return id.longValue();
          }
          return 0;
      }

    @Named("entityToResponseConvertIdlongToIdLong")
    public static Long requestToEntityConvertIdLongToIdlong(long id){
        return Long.valueOf(id);
    }

    /*@Mappings({@Mapping(source = "price", target = "productPrice", qualifiedByName = "priceToProductPrice"),
              @Mapping(source="id", target="productId")
      })
      public abstract ProductOutput mapProductEntity2Output(ProductEntity productEntity);

      @Named("priceToProductPrice")
      public static  String priceToProductPrice(BigDecimal price) {
            return price.toString()+" ?";
      }*/
}
