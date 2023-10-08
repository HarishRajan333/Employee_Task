package com.mycompany.employee.Repository;

import com.mycompany.employee.Model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Documents, Integer> {

}
