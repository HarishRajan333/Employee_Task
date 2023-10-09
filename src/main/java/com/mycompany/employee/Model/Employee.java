package com.mycompany.employee.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table
@Entity(name = "employee_task")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "ph_no", nullable = false, unique = true)
    private String phNo;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "joined_on")
    private Timestamp joinedOn;

    @Column(name = "added_on")
    private Timestamp addedOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserCredientails userCredientails;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_role", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> roles;
    
    @OneToMany(mappedBy = "employee")
    private List<Documents> documents;
 
}
