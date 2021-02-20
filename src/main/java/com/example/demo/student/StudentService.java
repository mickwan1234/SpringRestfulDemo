package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudent(){
        return studentRepository.findAll();
    }


    public void addNewStudent(Student student) {
        Optional<Student> studentOptional =studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists) {
            throw new IllegalStateException("student with id "+ studentId +" is not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudentById(Long studentId, String name, String email) {
       Student student = studentRepository.findById(studentId)
               .orElseThrow(()-> new IllegalStateException("student with id "+ studentId +" is not exists"));
        if(name != null && !name.isEmpty() && !Objects.equals(student.getName(),name)){
            student.setName(name);
        }
        if(email != null && !email.isEmpty() && !Objects.equals(student.getEmail(),email)){
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("Email taken");
            }
            student.setEmail(email);
        }
    }
}
