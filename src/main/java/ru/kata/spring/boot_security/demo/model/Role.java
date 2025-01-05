package ru.kata.spring.boot_security.demo.model;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import java.util.List;


@Entity
@Table(name = "table_roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role() {
    }


    public Role(String roleName) {
        this.roleName = roleName;
    }



    @Override
    public String getAuthority() {
        return roleName;
    }

    @Override
    public String toString() {
        return this.roleName;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
