package com.procurement.mdm.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "cpv")
public class CpvEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "group")
    private int group;

    @Column(name = "parent")
    private String parent;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_cpv_language_id"))
    private LanguageEntity language;

}
