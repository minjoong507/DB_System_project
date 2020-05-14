import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class LoadData {
    static Scanner sc = new Scanner(System.in);
    static String path = "src/data.csv";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        PreparedStatement pstmt = null;
        int batchSize = 20;

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/study_db" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ", "root","9848");
            String InsertSQL = "INSERT INTO book (book_id, book_name, author, book_topic) VALUES (?, ?, ?, ?)";

            pstmt = conn.prepareStatement(InsertSQL);
            BufferedReader lineReader = new BufferedReader(new FileReader(path));
            String lineText = null;
            int count = 0;


            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String book_id = data[0];
                String book_name = data[1];
                String author = data[2];
                String book_topic = data[3];

                pstmt.setString(1, book_id);
                pstmt.setString(2, book_name);
                pstmt.setString(3, author);
                pstmt.setString(4, book_topic);

                pstmt.addBatch();

                if (count % batchSize == 0) {
                    pstmt.executeBatch();
                }
                count ++;
            }

            lineReader.close();
            pstmt.executeBatch();
            conn.close();

        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}