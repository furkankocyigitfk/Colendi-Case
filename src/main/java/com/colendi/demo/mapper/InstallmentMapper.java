package com.colendi.demo.mapper;

import com.colendi.demo.controller.response.ResponseUpdateInstallment;
import com.colendi.demo.model.Installment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InstallmentMapper {
    InstallmentMapper INSTANCE = Mappers.getMapper(InstallmentMapper.class);

    @Mapping(source = "installmentStatus", target = "status")
    ResponseUpdateInstallment toResponseUpdateInstallment(Installment installment);
}
