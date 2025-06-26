package com.mx.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@MappedSuperclass
@Getter
@Setter
@JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowGetters = true)
public abstract class UserDateAudit extends DateAudit {

	private static final long serialVersionUID = 1L;

	@CreatedBy
	private Long createdBy;

	@LastModifiedBy
	private Long updatedBy;

}