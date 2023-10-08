package com.mycompany.employee.Repository;

import com.mycompany.employee.Model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    public Roles findByRoleName(String roleName);

}
