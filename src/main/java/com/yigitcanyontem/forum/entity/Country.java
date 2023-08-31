package com.yigitcanyontem.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "country")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Country {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "iso")
    private String iso;
    @Column(name = "name")
    private String name;
    @Column(name = "nicename")
    private String nicename;
    @Column(name = "iso3")
    private String iso3;
    @Column(name = "numcode")
    private Integer numcode;
    @Column(name = "phonecode")
    private Integer phonecode;
}
