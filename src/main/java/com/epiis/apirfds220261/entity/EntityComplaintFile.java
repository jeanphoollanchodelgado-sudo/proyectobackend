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

@Entity
@Table(name = "tcomplaintfile")
@Setter
@Getter
public class EntityComplaintFile {

    @Id
    @Column(name = "idComplaintfile")
    private String idComplaintfile;

    @Column(name = "idComplaint")
    private String idComplaint;

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    @Column(name = "createdAt")
    private Instant createdAt;

    @Column(name = "updatedAt")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idComplaint", insertable = false, updatable = false)
    private EntityComplaint parentComplaint;
}