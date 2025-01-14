package com.javatalent.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.javatalent.model.Employee;
import com.javatalent.service.EmployeeService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    // Home page to display list of employees
    @GetMapping("/")
    public String home(Model model) {
        List<Employee> list = service.getAllEmployee();
        model.addAttribute("list", list);
        return "index";
    }

    // POST method for file upload (used for uploading files)
    @PostMapping("/upload")  // Corrected to POST for file upload
    public String fileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("error", "File is empty. Please upload a valid file.");
            return "index";
        }

        Employee employee = new Employee();
        String fileName = file.getOriginalFilename();
        employee.setProfilePicture(fileName);
        employee.setContent(file.getBytes());
        employee.setSize(file.getSize());
        service.createEmployee(employee);
        model.addAttribute("success", "File Uploaded Successfully");
        return "index";
    }

    // Method for downloading files
    @GetMapping("/downloadfile")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<Employee> temp = service.findEmployeeById(id);
        if (temp.isPresent()) {
            Employee employee = temp.get();
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + employee.getProfilePicture();
            response.setHeader(headerKey, headerValue);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(employee.getContent());
            outputStream.close();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found.");
        }
    }

    // Method for displaying image files
    @GetMapping("/image")
    public void showImage(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<Employee> employee = service.findEmployeeById(id);
        if (employee.isPresent()) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(employee.get().getContent());
            response.getOutputStream().close();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found.");
        }
    }
}
