package com.app.controllers;

import com.app.entities.Department;
import com.app.entities.Employee;
import com.app.repositories.EmployeeRepository;
import com.app.repositories.DepartmentRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Controller
@RequestMapping("/xml")
public class XmlController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public XmlController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @ResponseBody
    @GetMapping(path = "/departments", produces = MediaType.APPLICATION_XML_VALUE)
    private ModelAndView getDepartments() throws JsonProcessingException {
        Iterable<Department> list = departmentRepository.findAll();
        return getModelAndView(list, "departmentsXSL");
    }

    @ResponseBody
    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_XML_VALUE)
    private ModelAndView getEmployees() throws JsonProcessingException {
        Iterable<Employee> list = employeeRepository.findAll();
        return getModelAndView(list, "employeesXSL");
    }

    private ModelAndView getModelAndView(Iterable<?> list, String viewName) throws JsonProcessingException {
        String str = new XmlMapper().writeValueAsString(list);
        ModelAndView mod = new ModelAndView(viewName);
        Source src = new StreamSource(new StringReader(str));
        mod.addObject("ArrayList", src);
        return mod;
    }
}
