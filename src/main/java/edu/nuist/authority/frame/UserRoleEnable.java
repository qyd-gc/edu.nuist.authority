package edu.nuist.authority.frame;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class UserRoleEnable {
    private String roles;
    public String[] getRoles() {
        String[] roleList=this.roles.split(",");
        return roleList;
    }

    public void setRoles(String[] roles) {
        this.roles="";
        for(int i=0;i<roles.length;i++)
        {
            this.roles=this.roles+roles[i]+",";
        }
    }
}
