package com.javaproject.endtoend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneticRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String firstOP;
    @Column
    private String secondOP;

    public PhoneticRule(String secondOP) {
        this.secondOP = secondOP;
    }
}
