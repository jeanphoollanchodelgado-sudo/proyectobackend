package com.epiis.apirfds220261.entity;

import java.time.Instant;
import java.time.LocalDate;
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

@Entity
@Table(name = "tcomplaint")
@Setter
@Getter
public class EntityComplaint {

    @Id
    @Column(name = "idComplaint")
    private String idComplaint;

    @Column(name = "idOffice")
    private String idOffice;

    @Column(name = "idProfessor")
    private String idProfessor;

    @Column(name = "code")
    private String code;

    @Column(name = "personFullName")
    private String personFullName;

    @Column(name = "complaintFullName")
    private String complaintFullName;

    @Column(name = "description")
    private String description;

    @Column(name = "issueDate")
    private LocalDate issueDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProfessor", insertable = false, updatable = false)
    private EntityProfessor parentProfessor;

    @OneToMany(mappedBy = "parentComplaint", cascade = CascadeType.ALL)
    private List<EntityComplaintFile> childComplateFile;

    @OneToMany(mappedBy = "parentComplaint", cascade = CascadeType.ALL)
    private List<EntityComplaintComment> childComplaintComment;
}