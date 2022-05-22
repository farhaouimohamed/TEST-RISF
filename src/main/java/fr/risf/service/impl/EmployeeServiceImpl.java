package fr.risf.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.risf.entities.Employee;
import fr.risf.exception.ResourceNotFoundException;
import fr.risf.repositories.EmployeeRepository;
import fr.risf.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	private EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee createEmployee(Employee post) {
		return employeeRepository.save(post);
	}

	@Override
	public Employee updateEmployee(long id, Employee employeeRequest) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Error : there is no employee with id "+id));
		employee.setEmail(employeeRequest.getEmail());
		employee.setFirstName(employeeRequest.getFirstName());
		employee.setLastName(employeeRequest.getLastName());
		return employeeRepository.save(employee);
	}

	@Override
	public void deleteEmployee(long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Error : there is no employee with id "+id));
		employeeRepository.delete(employee);
	}

	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent()) {
			return employee.get();
		}else {
			throw new ResourceNotFoundException("Error : there is no employee with id "+id);
		}
	}

}
