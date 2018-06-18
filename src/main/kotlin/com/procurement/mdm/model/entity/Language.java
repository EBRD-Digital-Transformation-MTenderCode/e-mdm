package com.procurement.mdm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "languages")
public class Language {

    @Id
    @Column(name = "iso6391")
    private String iso6391;

    @Column(name = "name")
    private String name;

    @Column(name = "family")
    private String family;

    @Column(name = "description")
    private String description;
}
