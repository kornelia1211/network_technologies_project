package edu.bi.springdemo.Repositories;

import edu.bi.springdemo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    //User findUserByUsername(String username);

    //@Query(value = "SELECT u FROM User u WHERE u.status = 1", nativeQuery = true)
    //Collection<User> findUserByUsername(String username);


    @Query("SELECT u FROM User u WHERE u.username = :username")
    Collection<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Collection<User> findUserByEmail(String email);

}
