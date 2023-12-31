package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PlanSaveRequestDto {

    private String planName;
    private LocalDate date;

}
