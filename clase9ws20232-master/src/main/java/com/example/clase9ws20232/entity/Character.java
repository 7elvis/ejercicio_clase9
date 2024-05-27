package com.example.clase9ws20232.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "characters")
@Getter
@Setter
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "identity", length = 45)
    private String identity;

    @Column(name = "align", length = 45)
    private String align;

    @Column(name = "eye", length = 45)
    private String eye;

    @Column(name = "hair", length = 45)
    private String hair;

    @Column(name = "sex", length = 45)
    private String sex;

    @Column(name = "gsm", length = 45)
    private String gsm;

    @Column(name = "alive", length = 45)
    private String alive;

    @Column(name = "appearances")
    private Integer appearances;

    @Column(name = "first_appearance", length = 45)
    private String firstAppearance;

    @Column(name = "year")
    private Integer year;
}
