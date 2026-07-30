package com.example.keitpanel.branch;

import com.example.keitpanel.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "branch")
public class Branch extends BaseEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 30)
    private String phone;

    @Column(nullable = false)
    private boolean active = true;;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Branch parent;

    @OneToMany(mappedBy = "parent")
    @Setter(AccessLevel.NONE)
    private List<Branch> children = new ArrayList<>();


    public void addChild(Branch child){
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Branch child){
        children.remove(child);
        child.setParent(null);
    }
}
