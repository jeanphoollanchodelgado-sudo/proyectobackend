package com.epiis.apirfds220261.entity;


import java.time.Instant;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "tSuggestioncomment")
@Setter
@Getter
public class EntitySuggestionComment {
    @Id
    @Column(name = "idSuggestioncomment")
    private String idSuggestioncomment;
    
    @Column(name = "idSuggestion")
    private String idSuggestion;
    
    @Column(name = "idUser")
    private String idUser;

    @Column(name = "description")
    private String description;

    @Column(name = "createdAt")
    private Instant createdAt;

    @Column(name = "updatedAt")
    private Instant updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSuggestion", insertable = false, updatable = false)
    private EntitySuggestion parentSuggestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", insertable = false, updatable = false)
    private EntityUser parentUser;
    
    
    
}