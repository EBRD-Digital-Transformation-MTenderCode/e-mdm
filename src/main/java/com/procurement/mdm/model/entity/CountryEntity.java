package com.procurement.mdm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class CountryEntity {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "language_id")
    private long languageId;

    @Column(name = "description")
    private String description;

    @Column(name = "phone_code")
    private String phoneCode;

}
