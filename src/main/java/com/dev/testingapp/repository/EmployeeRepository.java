package com.dev.testingapp.repository;

import com.dev.testingapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    //Define custom query using JPQL with index params
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    //Define custom query using JPQL with named params
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //Define custom query uwith native SQL with index params
    @Query(
            value = "select * from employees e where e.first_name =?1 and e.last_name =?2",
            nativeQuery = true
    )
    Employee findByNativeSQL(String firstName, String lastName);

    //Define custom query uwith native SQL with named params
    @Query(
            value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName",
            nativeQuery = true
    )
    Employee findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
