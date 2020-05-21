import java.sql.*;
import java.io.*;

public class LoadData {
    public static void main(String[] args) {
        String bookSQL = "INSERT INTO book (book_id, book_name, author, publisher, publication_year) VALUES (?, ?, ?, ?,?)";
        String studentSQL = "INSERT INTO student (id, name, dept_name, age) VALUES (?, ?, ?, ?)";
        String rentbookSQL = "INSERT INTO rentbook (book_id, student_id, date_year, date_month, date_day) VALUES (?, ?, ?, ?, ?)";
        String departmentSQL = "INSERT INTO department (dept_name, building) VALUES (?, ?)";

        String[] SQL_list = {bookSQL, departmentSQL, studentSQL, rentbookSQL};
        String[] path_list = {"src/book_data.csv", "src/department_data.csv", "src/student_data.csv", "src/rentbook_data.csv"};
        for (int i = 0; i<4; i++)
            InsertData(SQL_list[i], path_list[i]);

        System.out.println("Finish!");
    }

    public static void InsertData(String InsertSQL, String path){
        int batchsize = 100;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/study_db" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ", "root","9848");
            String SQL = InsertSQL;

            PreparedStatement pstmt = conn.prepareStatement(SQL);
            BufferedReader br = new BufferedReader(new FileReader(path));
            String lineText = null;
            int cnt = 0;

            while((lineText = br.readLine()) != null){
                String[] data = lineText.split(",");

                for(int i = 0; i<data.length; i++){
                    pstmt.setString(i+1, data[i]);
                }
                pstmt.addBatch();

                if(cnt % batchsize == 0){
                    pstmt.executeBatch();
                }
                cnt++;
            }

            pstmt.executeBatch();
            br.close();

            conn.close();

        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}