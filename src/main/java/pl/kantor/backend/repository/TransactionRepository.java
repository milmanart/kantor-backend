package pl.kantor.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantor.backend.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
