package com.mx.core.model.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PollLength {
	
    @NotNull
    @Max(7)
    private Integer days;

    @NotNull
    @Max(23)
    private Integer hours;

}

