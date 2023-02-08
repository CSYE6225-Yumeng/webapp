package com.yumeng.webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@EntityListeners(value = AuditingEntityListener.class)
public class User implements UserDetails{  // implements UserDetails
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonProperty("first_name")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @JsonProperty("last_name")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "username", nullable = false)
    @JsonProperty("username")
    private String username;
    @Column(name = "account_created")
    @JsonProperty(value = "account_created",access = JsonProperty.Access.READ_ONLY)
    @CreatedDate
    private Date accountCreated;
    @Column(name = "account_updated")
    @JsonProperty(value = "account_updated",access = JsonProperty.Access.READ_ONLY)
    @LastModifiedDate
    private Date accountUpdated;



    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="enabled")
    private boolean enabled = true;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private Set<GrantedAuthority> authorities;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="account_non_expired")
    private boolean accountNonExpired=true;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="account_non_locked")
    private boolean accountNonLocked=true;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="credentials_non_expired")
    private boolean credentialsNonExpired=true;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Date getAccountCreated() {
        return accountCreated;
    }

    public Date getAccountUpdated() {
        return accountUpdated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean emailValidation() {
        if(this.username == null){
            return true;
        }
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(this.username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountCreated(Date accountCreated) {
        this.accountCreated = accountCreated;
    }

    public void setAccountUpdated(Date accountUpdated) {
        this.accountUpdated = accountUpdated;
    }
}
