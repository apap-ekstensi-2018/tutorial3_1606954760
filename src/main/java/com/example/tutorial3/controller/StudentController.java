package com.example.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tutorial3.model.StudentModel;
import com.example.tutorial3.service.InMemoryStudentService;
import com.example.tutorial3.service.StudentService;

@Controller
public class StudentController {
	private final StudentService studentService;
	
	public StudentController() {
			studentService = new InMemoryStudentService();
		
	}
	
	@RequestMapping("/add")
	public String index() {
		return "add";
	}
	
	@RequestMapping("/student/add")
	public String add(
			@RequestParam(value = "npm", required = true) String npm,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "gpa", required = true) double gpa
			) {
		StudentModel student = new StudentModel(npm, name, gpa);
		studentService.addStudent(student);
		return "add";
	}
	
	@RequestMapping("/student/view")
	public String view(
			Model  model, 
			@RequestParam(value="npm", required = true) String npm
			
			) {
		StudentModel student = studentService.selectStudent(npm);
		model.addAttribute("student", student);
		return "view";
	}
	
	@RequestMapping ( "/student/viewall")
	public String viewAll ( Model model ) {
		List < StudentModel > students = studentService . selectAllStudents ();
		model.addAttribute ( "students" , students );
		return "viewall";
	}
	
	@RequestMapping ( "/student/view/{npm}")
	public String viewSelectedStudent ( @PathVariable Optional<String> npm, Model model ) {
		if(npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			if(student != null) {
				model.addAttribute ( "student" , student );
			}else {
				model.addAttribute ( "error" , "Cannot find the student with npm " +  npm.get() + ".");
			}
		}
		
		return "view";
	}
	
	@RequestMapping ( "/student/delete/{npm}")
	public String deleteStudent ( @PathVariable Optional<String> npm, Model model ) {
		if(npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			if(student != null) {
				studentService.deleteStudent(student);
				model.addAttribute ( "success" , "Student with npm " + npm.get() + " has already been deleted" );
			}else {
				model.addAttribute ( "error" , "Cannot find the student with npm " +  npm.get() + "."
						+ "\n. Delete operation is canceled");
			}
		}
		
		return "delete";
	}
	
	

}
