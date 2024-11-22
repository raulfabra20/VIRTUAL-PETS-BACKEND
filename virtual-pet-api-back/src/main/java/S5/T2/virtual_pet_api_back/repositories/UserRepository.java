package S5.T2.virtual_pet_api_back.repositories;

import S5.T2.virtual_pet_api_back.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    User findByUserId(Long userId);
}
