import com.tdtu.utils.CSVImporter;

public class ImportTest {
    public static void main(String[] args) {
        try {
            // Use class loader to locate the file
            String filePath = (ImportTest.class.getResource("/sample_students.csv")).getPath();
            CSVImporter.importStudents(filePath);
            System.out.println("Students imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
