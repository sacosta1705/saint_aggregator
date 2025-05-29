package com.saintnet.aggregator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="Stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la tienda no puede estar vacio.")
    @Column(name = "name", nullable = false, unique = true, length = 80)
    private String name;

    @NotBlank(message = "El campo URL es obligatorio.")
    @Column(name = "apiurl", nullable = false, unique = true, length = 255)
    private String apiurl;

    @NotBlank(message = "Es necesario el usuario del Enterprise Administrativo para la conexion con el API.")
    @Column(name = "apiuser", nullable = false, length = 10)
    private String apiuser;

    @NotBlank(message = "Es necesario la clave del Enterprise Administrativo para la conexion con el API.")
    @Column(name = "apipwdhash", nullable = false, length = 255)
    private String apipwdhash;

    @Column(name = "authtoken", length = 1024)
    private String authtoken;

    @Column(name = "authtokendate")
    private LocalDateTime authtokendate;

    @Column(name = "lastsync")
    private LocalDateTime lastsync;

    @NotNull
    @Column(name = "cansync", nullable = false)
    private Boolean cansync = true;

    @CreationTimestamp
    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdat;

    @UpdateTimestamp
    @Column(name = "updatedat", nullable = false)
    private LocalDateTime updatedat;

    public Store(){}

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

    public String getApipwdhash() {
        return apipwdhash;
    }

    public void setApipwdhash(String apipwdhash) {
        this.apipwdhash = apipwdhash;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public LocalDateTime getAuthtokendate() {
        return authtokendate;
    }

    public void setAuthtokendate(LocalDateTime authtokendate) {
        this.authtokendate = authtokendate;
    }

    public LocalDateTime getLastsync() {
        return lastsync;
    }

    public void setLastsync(LocalDateTime lastsync) {
        this.lastsync = lastsync;
    }

    public Boolean getCansync() {
        return cansync;
    }

    public void setCansync(Boolean cansync) {
        this.cansync = cansync;
    }

    public LocalDateTime getCreatedat() {
        return createdat;
    }

    public void setCreatedat(LocalDateTime createdat) {
        this.createdat = createdat;
    }

    public LocalDateTime getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(LocalDateTime updatedat) {
        this.updatedat = updatedat;
    }

    @Override
    public String toString(){
        return String.format("""
                Store{
                    id=%d
                    name=%s
                    api url=%s
                    can sync=%b
                }""", id, name, apiurl, cansync);
    }
}
