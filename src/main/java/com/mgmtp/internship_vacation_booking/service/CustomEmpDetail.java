package com.mgmtp.internship_vacation_booking.service;

import com.mgmtp.internship_vacation_booking.model.EmployeeEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomEmpDetail extends EmployeeEntity implements UserDetails {

    private List<String> empRoles;
    public CustomEmpDetail(EmployeeEntity emp, List<String> empRoles){
        super(emp);
        this.empRoles = empRoles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roles = org.springframework.util.StringUtils.collectionToCommaDelimitedString(empRoles);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    @Override
    public String getUsername() {
        return super.getEmail();
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
}
