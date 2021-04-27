package edu.nuist.authority.frame.usermapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "user")
public class UserMapper {
     private List<String> userroles;

    public void setUserroles(List<String> userroles) {
        this.userroles = userroles;
    }

    public List<String> getUserroles() {
        return userroles;
    }
}
