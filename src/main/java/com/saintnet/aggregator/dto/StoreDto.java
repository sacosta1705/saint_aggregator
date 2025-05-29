package com.saintnet.aggregator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class StoreDto {

    private Long id;

    @NotBlank(message = "El nombre de la conexion/tienda es obligatorio.")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres.")
    private String name;

    @NotBlank(message = "La URL del servidor web del Enterprise Administrativo es obligatorio.")
    @Size(max = 255, message = "La URL no debe superar los 255 caracteres.")
    @Pattern(regexp = "^(http|https)://.*", message = "La URL suministrada no es valida. Esta debe incluir el protocolo (http o https).")
    private String apiurl;

    @NotBlank(message = "El usuario del Enterprise Administrativo es obligatorio.")
    @Size(max = 10, message = "El usuario no puede superar los 10 caracteres.")
    private String apiuser;

    @Size(min = 1, max = 100, message = "La clave debe tener entre 1 y 100 caracteres.")
    private String apipwd;
    
    private Boolean cansync=true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiurl() {
        return apiurl;
    }

    public void setApiurl(String apiurl) {
        this.apiurl = apiurl;
    }

    public String getApiuser() {
        return apiuser;
    }

    public void setApiuser(String apiuser) {
        this.apiuser = apiuser;
    }

    public String getApipwd() {
        return apipwd;
    }

    public void setApipwd(String apipwd) {
        this.apipwd = apipwd;
    }

    public Boolean getCansync() {
        return cansync;
    }

    public void setCansync(Boolean cansync) {
        this.cansync = cansync;
    }

    
}
