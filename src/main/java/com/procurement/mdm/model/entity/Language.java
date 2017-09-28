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
    @Column(name = "id")
    private Long id;

    @Column(name = "s_no")
    private Integer sNo;

    @Column(name = "name")
    private String name;

    @Column(name = "family")
    private String family;

    @Column(name = "iso_639_1")
    private String iso6391;

    @Column(name = "description")
    private String description;
}
