package sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sep.model.PaymentUrlAndId;

public interface PaymentUrlAndIdRepository extends JpaRepository<PaymentUrlAndId, Long> {

}
