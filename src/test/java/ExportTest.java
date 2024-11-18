import com.tdtu.utils.CSVExporter;

public class ExportTest {
    public static void main(String[] args) {
        try {
            CSVExporter.exportStudents(ExportTest.class.getResource("/exported_students.csv").getPath());
            System.out.println("Students exported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
