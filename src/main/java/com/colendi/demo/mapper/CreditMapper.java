package com.colendi.demo.mapper;

import com.colendi.demo.controller.response.ResponseCreateCredit;
import com.colendi.demo.model.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class);

    @Mapping(source = "id", target = "creditId")
    @Mapping(source = "installments", target = "installments")
    ResponseCreateCredit toResponseCreateCredit(Credit credit);
}
