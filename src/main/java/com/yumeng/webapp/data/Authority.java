package com.yumeng.webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
//import org.springframework.data.relational.core.mapping.Table;

@Entity
@Data
@Table(name = "authorities",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "authority"})})
public class Authority {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @OneToOne
//    @NotNull
//    @JoinColumn(name = "username", referencedColumnName = "username")
//    private User user;

    @OneToOne
    @NotNull
    @JoinColumns(value ={@JoinColumn(name = "username", referencedColumnName = "username"), @JoinColumn(name = "authority", referencedColumnName = "id")})
    private User user;

    public Authority(User user) {
        this.user = user;
    }

    public Authority() {

    }
}
