package fr.risf.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.risf.dtos.EmployeeDto;
import fr.risf.entities.Employee;
import fr.risf.payload.ApiResponse;
import fr.risf.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	private ModelMapper modelMapper;
	private EmployeeService employeeService;
	public EmployeeController(ModelMapper modelMapper, EmployeeService employeeService) {
		super();
		this.modelMapper = modelMapper;
		this.employeeService = employeeService;
	}
	@GetMapping
	public ResponseEntity<?> getAllEmployees(){
		try {
			List<EmployeeDto> employees = employeeService.getAllEmployees().stream().map(
					employee -> modelMapper.map(employee, EmployeeDto.class))
					.collect(Collectors.toList());
			return new ResponseEntity<>(employees, HttpStatus.OK);
		}catch (Exception e) {
			return ResponseEntity
					.internalServerError()
					.body(new ApiResponse(false, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable(name="id") Long id){
		try {
			if (null == id || id.equals(0L)) {
				return ResponseEntity
						.badRequest()
						.body(new ApiResponse(false, "Invalid input id { " +id+" }", HttpStatus.BAD_REQUEST));
			}
			Employee employee = employeeService.getEmployeeById(id);
			EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);
			return ResponseEntity.ok().body(employeeResponse);
		}catch (Exception e) {
			return ResponseEntity
					.internalServerError()
					.body(new ApiResponse(false, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}
	
	@PostMapping
	public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employeeDto){
		try {
			Employee employeeRequest = modelMapper.map(employeeDto, Employee.class);
			Employee employee = employeeService.createEmployee(employeeRequest);
			EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);
			return new ResponseEntity<EmployeeDto>(employeeResponse, HttpStatus.CREATED);
		}catch (Exception e) {
			ApiResponse apiResponse = new ApiResponse(false, "Invalid input"+e.getMessage(), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto){
		try {
			if (null == id || id.equals(0L)) {
				return ResponseEntity
						.badRequest()
						.body(new ApiResponse(false, "Invalid input id { " +id+" }", HttpStatus.BAD_REQUEST));
			}
			Employee employeeRequest = modelMapper.map(employeeDto, Employee.class);
			Employee employee = employeeService.updateEmployee(id, employeeRequest);
			EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);
			return ResponseEntity.ok().body(employeeResponse);
		}catch (Exception e) {
			return ResponseEntity
					.internalServerError()
					.body(new ApiResponse(false, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Long id){
		try {
			if (null == id || id.equals(0L)) {
				return ResponseEntity
						.badRequest()
						.body(new ApiResponse(false, "Invalid input id { " +id+" }", HttpStatus.BAD_REQUEST));
			}
			employeeService.deleteEmployee(id);
			ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Employee deleted successfully", HttpStatus.OK);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		}catch (Exception e) {
			return ResponseEntity
					.internalServerError()
					.body(new ApiResponse(false, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
		}
		
	}
	

}
