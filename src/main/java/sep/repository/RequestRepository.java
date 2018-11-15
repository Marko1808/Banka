package sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sep.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long>{

}
