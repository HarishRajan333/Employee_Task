package com.mycompany.employee.Repository;

import com.mycompany.employee.Model.UserCredientails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredientailsRepository extends JpaRepository<UserCredientails, Integer> {

    public UserCredientails findByUsername(String username);

    public boolean existsByUsername(String username);

}
