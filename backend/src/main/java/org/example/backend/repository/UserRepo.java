package org.example.backend.repository;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByFullName(String fullName);

    Optional<User> findByPhoneNumber(String fullName);
    Optional<User> findAllByFullName(String fullName);

    List<User> findAllByChatId(Long chatId);
    Optional<User> findByChatId(Long chatId);
    @Query("SELECT u.id FROM users u WHERE u.chatId = :chatId")
    List<UUID> findAllUserIdsByChatId(@Param("chatId") Long chatId);

    List<User> findAllById(UUID id);

    List<User> findAllByFullNameContainingIgnoreCaseAndRoles(String fullName, List<Role> roles);
    Integer countAllByRoles(List<Role> roles);
    List<User> findAllByRolesAndIsDriver(List<Role> roles, Boolean isDriver);


    Collection<Object> findAllByChatIdIsNotNull();
}
