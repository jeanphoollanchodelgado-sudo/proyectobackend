package com.epiis.apirfds220261.entity;






import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tsuggestionfile")
@Setter
@Getter
public class EntitySuggestionFile {
    @Id
    @Column(name = "idSuggestionfile")
    private String idSuggestionfile;

    @Column(name = "idSuggestion")
    private String idSuggestion;

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    @Column(name = "createdAt")
    private Instant createdAt;

    @Column(name = "updatedAt")
    private Instant updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSuggestion", insertable = false, updatable = false)
    private EntitySuggestion parentSuggestion;
    
   
   
}