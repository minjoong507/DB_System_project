import java.sql.*;
import java.io.*;
import java.util.*;

/*
 * 1. 기능 선택 ( 검색, 삽입, 수정, 삭제 , 프로그램 종료)
 * 2. DB Table 선택 ( 1. book, 2. rentbook, 3. student, 4. visited, 5. department )
 * 3. 사용자 입력 받기
 * */


public class library_DB_System {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static Scanner sc = new Scanner(System.in);
    static String[] Command_list = {"Select", "Update", "Delete"};
    static String[] Table_list = {"book", "rentbook", "student", "visited", "department"};
    static int user_command, user_table;
    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    public static void main(String[] args) {
        search_DB();
    }

    public static void search_DB() {

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/study_db" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ", "root", "9848");

            user_command = InitialPage();
            if (user_command > 4 || user_command < 1) user_command = InitialPage();

            else if (user_command == 4) return;

            user_table = SelectTablePage();
            if (user_table > 5 || user_table < 0) user_table = SelectTablePage();

            Main_System(user_command, user_table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Main_System(int x, int y) throws SQLException {
        int command_idx = x-1;
        int table_idx = y-1;

        // book table
        if (table_idx == 0){
            book_table();

            stmt = (Statement) conn.createStatement();
            switch (command_idx){

                // select
                case 0:
                    rs = stmt.executeQuery("select * from book");
                    ArrayList<String> book_id = new ArrayList<>();
                    ArrayList<String> book_name = new ArrayList<>();
                    ArrayList<String> author = new ArrayList<>();
                    ArrayList<String> publisher = new ArrayList<>();
                    ArrayList<String> publication_year = new ArrayList<>();

                    while (rs.next()){
                        book_id.add(rs.getString("book_id"));
                        book_name.add(rs.getString("book_name"));
                        author.add(rs.getString("author"));
                        publisher.add(rs.getString("publisher"));
                        publication_year.add(rs.getString("publication_year"));
                    }

                    int now_page = 0;
                    while (now_page != -1){
                        select_booktable(now_page, book_id, book_name, author, publisher, publication_year);
                        System.out.printf("총 페이지 수 : %d \n", book_id.size() / 100);
                        System.out.println("검색결과를 마무리려면 '-1' 를 입력하세요.");
                        System.out.print("페이지 번호 :");
                        now_page = sc.nextInt();
                        while (now_page >= book_id.size() / 100){
                            System.out.println("존재하지 않는 페이지 입니다. 다시 입력해주세요");
                            System.out.print("페이지 번호 :");
                            now_page = sc.nextInt();
                        }
                    }

                    break;

                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;
            }

        }

        // rentbook table
        else if (table_idx == 1){

        }


        // student table
        else if (table_idx == 2){

        }


        // visited table
        else if (table_idx == 3){

        }


        // department
        else if (table_idx == 4){

        }

        else{

        }
        search_DB();

    }

    // 시작화면 안내 메시지
    public static int InitialPage () {
        System.out.println("--- Library Database System ---");
        System.out.println("1. Search");
        System.out.println("2. Update");
        System.out.println("3. Delete");
        System.out.println("4. Exit");
        System.out.println("\n Command : ");

        return sc.nextInt();
    }

    // 테이블 선택 메시지
    public static int SelectTablePage(){
        System.out.println("\n--- Table List ---");
        System.out.println("1. Book");
        System.out.println("2. Rentbook");
        System.out.println("3. Student");
        System.out.println("4. Visited");
        System.out.println("5. department");
        System.out.println("\n Command : ");

        return sc.nextInt();

    }

    // Book 테이블 안내 메시지
    public static void book_table(){
        System.out.println("\n--- Book Table ---");
        System.out.println("1. book_id (PK)");
        System.out.println("2. book_name");
        System.out.println("3. author");
        System.out.println("4. publisher");
        System.out.println("5. publication year");
    }

    // Rentbook 테이블 안내 메시지
    public static void rentbook_table(){
        System.out.println("\n--- Rentbook Table ---");
        System.out.println("1. book_id (PK)");
        System.out.println("2. student_id (PK)");
        System.out.println("3. date year");
        System.out.println("4. date month");
        System.out.println("5. date day");
    }

    public static void select_booktable (int pg_num, ArrayList<String> book_id, ArrayList<String> book_name, ArrayList<String> author,ArrayList<String> publisher, ArrayList<String> publication_year) {
        System.out.println("-- Book Table Result Set --");
        System.out.printf("\n -- Now Page : %d --\n", pg_num);
        System.out.println("------------------------");
        for(int i = pg_num * 100; i < (pg_num + 1) * 100; i++){
            System.out.printf("%s, %s, %s, %s, %s", book_id.get(i), book_name.get(i), author.get(i), publisher.get(i), publication_year.get(i));
            System.out.println("");
        }
    }
}
