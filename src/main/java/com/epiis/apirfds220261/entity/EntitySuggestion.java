package com.epiis.apirfds220261.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
@Entity
@Table(name = "tsuggestion")
@Setter
@Getter
public class EntitySuggestion {

    @Id
    @Column(name = "idSuggestion")
    private String idSuggestion;

    @Column(name = "idOffice")
    private String idOffice;

    @Column(name = "code")
    private String code;

    @Column(name = "personFullName")
    private String personFullName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "createdAt")
    private Instant createdAt;

    @Column(name = "updatedAt")
    private Instant updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOffice", insertable = false, updatable = false)
    private EntityOffice parentOffice;

    @OneToMany(mappedBy = "parentSuggestion", cascade = CascadeType.ALL)
    private List<EntitySuggestionFile> childSuggestionFile;
}