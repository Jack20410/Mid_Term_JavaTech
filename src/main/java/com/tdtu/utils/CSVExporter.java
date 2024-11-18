package com.tdtu.utils;

import com.opencsv.CSVWriter;
import com.tdtu.DAO.StudentDAO;
import com.tdtu.models.Student;

import java.io.FileWriter;
import java.util.List;

public class CSVExporter {
    public static void exportStudents(String filePath) throws Exception {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            StudentDAO dao = new StudentDAO();
            List<Student> students = dao.getAllStudents();
            writer.writeNext(new String[]{"Name", "Age", "Phone", "Status"});
            for (Student student : students) {
                writer.writeNext(new String[]{
                        student.getName(),
                        String.valueOf(student.getAge()),
                        student.getPhoneNumber(),
                        student.getStatus()
                });
            }
        }
    }
}
