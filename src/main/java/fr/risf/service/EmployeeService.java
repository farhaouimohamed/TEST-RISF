package fr.risf.service;

import java.util.List;

import fr.risf.entities.Employee;

public interface EmployeeService {
	
	List<Employee> getAllEmployees();

	Employee createEmployee(Employee post);

	Employee updateEmployee(long id, Employee post);

	void deleteEmployee(long id);

	Employee getEmployeeById(long id);

}
