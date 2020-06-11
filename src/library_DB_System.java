import java.sql.*;
import java.io.*;
import java.util.*;

/*
 * 1. 기능 선택 ( 검색, 삽입, 수정, 삭제 , 프로그램 종료)
 * 2. DB Table 선택 ( 1. book, 2. rentbook, 3. student, 4. department )
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
            if (user_command > 5 || user_command < 1){
                while (user_command > 5 || user_command < 1 ){
                    System.out.println("지원하지 않는 기능입니다. 다시 입력해주세요.");
                    user_command = InitialPage();
                }
            }

            if (user_command == 5) return;

            user_table = SelectTablePage();
            if (user_table > 4 || user_table < 1){
                while (user_table > 4 || user_table < 1 ) {
                    System.out.println("지원하지 않는 기능입니다. 다시 입력해주세요.");
                    user_table = SelectTablePage();
                }
            }

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

                // Insert
                case 1:
                    break;

                // update
                case 2:
                    break;

                //delete
                case 3:
                    break;
            }

        }

        // rentbook table
        else if (table_idx == 1){
            rentbook_table();

            stmt = (Statement) conn.createStatement();
            switch (command_idx){

                // select
                case 0:
                    rs = stmt.executeQuery("select * from rentbook");
                    ArrayList<String> book_id = new ArrayList<>();
                    ArrayList<String> student_id = new ArrayList<>();
                    ArrayList<String> date_year = new ArrayList<>();
                    ArrayList<String> date_month = new ArrayList<>();
                    ArrayList<String> date_day = new ArrayList<>();

                    while (rs.next()){
                        book_id.add(rs.getString("book_id"));
                        student_id.add(rs.getString("student_id"));
                        date_year.add(rs.getString("date_year"));
                        date_month.add(rs.getString("date_month"));
                        date_day.add(rs.getString("date_day"));
                    }

                    int now_page = 0;
                    while (now_page != -1){
                        select_rentbooktable(now_page, book_id, student_id, date_year, date_month, date_day);
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


        // student table
        else if (table_idx == 2){
            student_table();

            stmt = (Statement) conn.createStatement();
            switch (command_idx){

                // select
                case 0:
                    rs = stmt.executeQuery("select * from student");
                    ArrayList<String> student_id = new ArrayList<>();
                    ArrayList<String> name = new ArrayList<>();
                    ArrayList<String> dept_name = new ArrayList<>();
                    ArrayList<String> age = new ArrayList<>();

                    while (rs.next()){
                        student_id.add(rs.getString("id"));
                        name.add(rs.getString("name"));
                        dept_name.add(rs.getString("dept_name"));
                        age.add(rs.getString("age"));
                    }

                    int now_page = 0;
                    while (now_page != -1){
                        select_studenttable(now_page, student_id, name, dept_name, age);
                        System.out.printf("총 페이지 수 : %d \n", student_id.size() / 100);
                        System.out.println("검색결과를 마무리려면 '-1' 를 입력하세요.");
                        System.out.print("페이지 번호 :");
                        now_page = sc.nextInt();
                        while (now_page >= student_id.size() / 100){
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

        // department
        else if (table_idx == 3){
            visited_table();

            stmt = (Statement) conn.createStatement();
            switch (command_idx){

                // select
                case 0:
                    rs = stmt.executeQuery("select * from department");
                    ArrayList<String> dept_name = new ArrayList<>();
                    ArrayList<String> building = new ArrayList<>();

                    while (rs.next()){
                        dept_name.add(rs.getString("dept_name"));
                        building.add(rs.getString("building"));
                    }

                    int now_page = 0;
                    while (now_page != -1){
                        select_departmenttable(now_page, dept_name, building);
                        System.out.printf("총 페이지 수 : %d \n", dept_name.size() / 100);
                        System.out.println("검색결과를 마무리려면 '-1' 를 입력하세요.");
                        System.out.print("페이지 번호 :");
                        now_page = sc.nextInt();
                        while (now_page > dept_name.size() / 100){
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

        else{

        }

        // 실행이 마치면 처음 상태로 돌아갑니다.
        search_DB();

    }

    // 시작화면 안내 메시지
    public static int InitialPage () {
        System.out.println("--- Library Database System ---");
        System.out.println("1. Search");
        System.out.println("2. Insert");
        System.out.println("3. Update");
        System.out.println("4. Delete");
        System.out.println("5. Exit");
        System.out.println("\n Command : ");

        return sc.nextInt();
    }

    // 테이블 선택 메시지
    public static int SelectTablePage(){
        System.out.println("\n--- Table List ---");
        System.out.println("1. Book");
        System.out.println("2. Rentbook");
        System.out.println("3. Student");
        System.out.println("4. department");
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

    // Student 테이블 안내 메시지
    public static void student_table(){
        System.out.println("\n--- Student Table ---");
        System.out.println("1. student_id (PK)");
        System.out.println("2. name");
        System.out.println("3. dept_name");
        System.out.println("4. age");
    }

    // Student 테이블 안내 메시지
    public static void visited_table(){
        System.out.println("\n--- Visited Table ---");
        System.out.println("1. student_id (PK)");
        System.out.println("3. date year");
        System.out.println("4. date month");
        System.out.println("5. date day");
    }

    // Student 테이블 안내 메시지
    public static void department_table(){
        System.out.println("\n--- Department Table ---");
        System.out.println("1. dept_name (PK)");
        System.out.println("2. building");
    }








    public static void select_booktable (int pg_num, ArrayList<String> book_id, ArrayList<String> book_name, ArrayList<String> author,ArrayList<String> publisher, ArrayList<String> publication_year) {
        System.out.println("\n -- Book Table Result Set --");
        System.out.printf(" -- Now Page : %d --\n", pg_num);
        System.out.println("------------------------");
        for(int i = pg_num * 100; i < (pg_num + 1) * 100; i++){
            System.out.printf("%s, %s, %s, %s, %s", book_id.get(i), book_name.get(i), author.get(i), publisher.get(i), publication_year.get(i));
            System.out.println("");
        }
    }

    public static void select_rentbooktable (int pg_num, ArrayList<String> book_id, ArrayList<String> student_id, ArrayList<String> date_year,ArrayList<String> date_month, ArrayList<String> date_day) {
        System.out.println("\n -- Rentbook Table Result Set --");
        System.out.printf("-- Now Page : %d --\n", pg_num);
        System.out.println("------------------------");
        for(int i = pg_num * 100; i < (pg_num + 1) * 100; i++){
            System.out.printf("%s, %s, %s, %s, %s", book_id.get(i), student_id.get(i), date_year.get(i), date_month.get(i), date_day.get(i));
            System.out.println("");
        }
    }

    public static void select_studenttable (int pg_num, ArrayList<String> student_id, ArrayList<String> name, ArrayList<String> dept_name, ArrayList<String> age){
        System.out.println("\n -- Student Table Result Set --");
        System.out.printf(" -- Now Page : %d --\n", pg_num);
        System.out.println("------------------------");
        for(int i = pg_num * 100; i < (pg_num + 1) * 100; i++){
            System.out.printf("%s, %s, %s, %s", student_id.get(i), name.get(i), dept_name.get(i), age.get(i));
            System.out.println("");
        }
    }

    public static void select_departmenttable (int pg_num, ArrayList<String> dept_name, ArrayList<String> building){
        System.out.println("\n -- Department Table Result Set --");
        System.out.printf(" -- Now Page : %d --\n", pg_num);
        System.out.println("------------------------");
        if (dept_name.size() < 100){
            for(int i = pg_num * 100; i < dept_name.size(); i++){
                System.out.printf("%s, %s", dept_name.get(i), building.get(i));
                System.out.println("");
            }
        }
        else{
            for(int i = pg_num * 100; i < (pg_num + 1) * 100; i++){
                System.out.printf("%s, %s", dept_name.get(i), building.get(i));
                System.out.println("");
            }
        }

    }
}
