package kz.simple.project.repository;

import jakarta.transaction.Transactional;
import kz.simple.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
}
