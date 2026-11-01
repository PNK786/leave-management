package com.project.Leave.Service.validation;

import com.project.Leave.Service.DTO.LeaveRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class DateRangeValidator implements ConstraintValidator<ValidDateRange, LeaveRequestDTO> {

    @Override
    public boolean isValid(LeaveRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) return true;

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

