import java.sql.*;
import java.io.*;


/*
* 1. 기능 선택 ( 검색, 수정, 삭제 )
* 2. DB Table 선택 ( 1. book, 2. rentbook, 3. student, 4. visited, 5. department )
* 3. 사용자 입력 받기
* */


public class library_DB_System {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) {
        search_DB();
    }

    public static void search_DB(){
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