package com.mx.core.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivoDTO {
	
    private String id;
    private String name;
    private List<ArchivoDTO> children;

    public ArchivoDTO(String id, String name, List<ArchivoDTO> children) {
        this.id = id;
        this.name = name;
        this.children = (children != null && !children.isEmpty()) ? children : null;
    }

    public ArchivoDTO(String id, String name) {
        this.id = id;
        this.name = name;
        this.children = null;
    }

}