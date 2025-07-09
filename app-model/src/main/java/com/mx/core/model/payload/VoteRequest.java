package com.mx.core.model.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequest {
    
	@NotNull
    private Long choiceId;

}