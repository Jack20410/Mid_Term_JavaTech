import com.tdtu.utils.CSVExporter;

public class ExportTest {
    public static void main(String[] args) {
        try {
            CSVExporter.exportStudents("F:\\Java Tech\\Mid_Term\\src\\main\\resources\\exported_students.csv");
            System.out.println("Students exported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
