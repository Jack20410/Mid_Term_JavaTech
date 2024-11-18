package com.tdtu.utils;

import com.opencsv.CSVReader;
import com.tdtu.DAO.StudentDAO;

import java.io.FileReader;

public class CSVImporter {
    public static void importStudents(String filePath) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;

            // Skip the header row
            reader.readNext();

            StudentDAO dao = new StudentDAO();
            while ((line = reader.readNext()) != null) {
                dao.addStudent(line[0], Integer.parseInt(line[1]), line[2], line[3]);
            }
        }
    }
}
