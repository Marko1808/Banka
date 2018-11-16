package sep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sep.model.Request;
import sep.repository.RequestRepository;

@Service
public class RequestService {
	
	@Autowired
	private RequestRepository requestRepository;
	
	public void save(Request toSave){
		requestRepository.save(toSave);
	}
	
	public List<Request> getAll(){
		return requestRepository.findAll();
	}

}
