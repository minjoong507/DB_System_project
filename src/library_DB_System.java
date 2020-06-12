import java.sql.*;
import java.util.*;
import java.sql.Savepoint;

/*
 * 1. 기능 선택 ( 검색, 삽입, 수정, 삭제 , 프로그램 종료)
 * 2. DB Table 선택 ( 1. book, 2. rentbook, 3. student, 4. department )
 * 3. 사용자 입력 받기
 * */


public class library_DB_System {
    static Scanner sc = new Scanner(System.in);
    static int user_command, user_table;
    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    static Savepoint RentbookSelectpoint;
    static Savepoint RentbookUpdatepoint;
    static Savepoint BookSelectpoint;
    static Savepoint BookUpdatepoint;
    static Savepoint StudentUpdatepoint;
    static Savepoint StudentSelectepoint;
    static Savepoint DepartmentSelectpoint;

    public static void main(String[] args) {
        search_DB();
    }

    public static void search_DB() {

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/study_db" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ", "root", "9848");
//            conn.setAutoCommit(false);

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
            BookUpdatepoint = conn.setSavepoint();
            switch (command_idx){
                // select
                case 0:
                    try{
                        BookSelectpoint = conn.setSavepoint();
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
                    }
                    catch (SQLException e){
                        conn.rollback(BookSelectpoint);
                    }

                    break;

                // Insert
                case 1:
                    try{
                        String bookSQL = "INSERT INTO book (book_id, book_name, author, publisher, publication_year) VALUES (?, ?, ?, ?,?)";
                        PreparedStatement pstmt = conn.prepareStatement(bookSQL);
                        String nul_val = sc.nextLine();

                        for(int i = 0; i<5; i++){
                            if (i == 0) System.out.print("book_id :");
                            else if (i == 1) System.out.print("book_name :");
                            else if (i == 2) System.out.print("author :");
                            else if (i == 3) System.out.print("publisher :");
                            else System.out.print("publication year :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }
                        int result = pstmt.executeUpdate();

                        if (result == 1) System.out.println("성공적으로 삽입 되었습니다.");
                        else System.out.println("입력이 실패했습니다.");
                    }

                    // PK 관련 제약사항 위배시
                    catch (SQLIntegrityConstraintViolationException e){
                        System.out.println("PK 입력값을 다시 확인해주세요");
                    }

                    break;

                // update
                case 2:
                    try{
                        String nul_val = sc.nextLine();

                        String bookupdateSQL = "Update book set book_name = ?, author = ?, publisher = ?, publication_year = ? where book_id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(bookupdateSQL);
                        for(int i = 0; i<5; i++){
                            if (i == 0) System.out.print("book_name :");
                            else if (i == 1) System.out.print("author :");
                            else if (i == 2) System.out.print("publisher :");
                            else if (i == 3) System.out.print("publication year :");
                            else System.out.print("book_id :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }

                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                        conn.rollback(BookUpdatepoint);
                    }

                    break;

                //delete
                case 3:
                    try{
                        String nul_val = sc.nextLine();

                        String bookdeleteSQL = "Delete from book where book_id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(bookdeleteSQL);

                        System.out.print("book_id :");
                        String input = sc.nextLine();
                        pstmt.setString(1, input);


                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                    }


                    break;
            }

        }

        // rentbook table
        else if (table_idx == 1){
            rentbook_table();

            stmt = (Statement) conn.createStatement();
            RentbookUpdatepoint = conn.setSavepoint();

            switch (command_idx){
                // select
                case 0:
                    try{
                        RentbookSelectpoint = conn.setSavepoint();
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
                    }
                    catch (SQLException e){
                        conn.rollback(RentbookSelectpoint);
                    }

                    break;

                //insert
                case 1:
                    try{
                        String rentbookSQL = "INSERT INTO rentbook (book_id, student_id, date_year, date_month, date_day) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(rentbookSQL);

                        String nul_val = sc.nextLine();

                        for(int i = 0; i<5; i++){
                            if (i == 0) System.out.print("book_id :");
                            else if (i == 1) System.out.print("student_id :");
                            else if (i == 2) System.out.print("year :");
                            else if (i == 3) System.out.print("month :");
                            else System.out.print("day :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }
                        int result = pstmt.executeUpdate();

                        if (result == 1) System.out.println("성공적으로 삽입 되었습니다.");
                        else System.out.println("입력이 실패했습니다.");
                    }

                    // PK 관련 제약사항 위배시
                    catch (SQLIntegrityConstraintViolationException e){
                        System.out.println("PK 입력값을 다시 확인해주세요");
                    }

                    break;

                //update
                case 2:
                    try{
                        String nul_val = sc.nextLine();

                        String rentbookupdateSQL = "Update rentbook set date_year = ?, date_month = ?, date_day = ? where book_id = ? and student_id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(rentbookupdateSQL);
                        for(int i = 0; i<5; i++){
                            if (i == 0) System.out.print("year :");
                            else if (i == 1) System.out.print("month :");
                            else if (i == 2) System.out.print("day :");
                            else if (i == 3) System.out.print("book_id :");
                            else System.out.print("student_id :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }

                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                        conn.rollback(RentbookUpdatepoint);
                    }


                    break;

                //delete
                case 3:
                    try{
                        String nul_val = sc.nextLine();

                        String rentbookdeleteSQL = "Delete from rentbook where book_id = ? and student_id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(rentbookdeleteSQL);

                        for (int i = 0 ; i < 2; i++){
                            if (i == 0 ) System.out.print("book_id :");
                            else System.out.print("student_id :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }

                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                    }

                    break;
            }
        }


        // student table
        else if (table_idx == 2){
            student_table();

            stmt = (Statement) conn.createStatement();
            StudentUpdatepoint = conn.setSavepoint();
            switch (command_idx){
                // select
                case 0:
                    try{
                        StudentSelectepoint = conn.setSavepoint();
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
                    }
                    catch(SQLException e){
                        conn.rollback(StudentSelectepoint);
                    }



                    break;

                //insert
                case 1:
                    try{
                        String studentSQL = "INSERT INTO student (id, name, dept_name, age) VALUES (?, ?, ?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(studentSQL);

                        String nul_val = sc.nextLine();

                        for(int i = 0; i<4; i++){
                            if (i == 0) System.out.print("student_id :");
                            else if (i == 1) System.out.print("student_name :");
                            else if (i == 2) System.out.print("dept_name :");
                            else System.out.print("age :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }
                        int result = pstmt.executeUpdate();

                        if (result == 1) System.out.println("성공적으로 삽입 되었습니다.");
                        else System.out.println("입력이 실패했습니다.");
                    }

                    // PK 관련 제약사항 위배시
                    catch (SQLIntegrityConstraintViolationException e){
                        System.out.println("PK 입력값을 다시 확인해주세요");
                    }
                    break;

                //update
                case 2:
                    try{
                        String nul_val = sc.nextLine();

                        String studentupdateSQL = "Update student set name = ?, dept_name = ?, age = ? where id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(studentupdateSQL);
                        for(int i = 0; i<4; i++){
                            if (i == 0) System.out.print("student name :");
                            else if (i == 1) System.out.print("dept_name :");
                            else if (i == 2) System.out.print("age :");
                            else System.out.print("student_id :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }

                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                        conn.rollback(StudentUpdatepoint);
                    }

                    break;

                //delete
                case 3:
                    try{
                        String nul_val = sc.nextLine();

                        String studentdeleteSQL = "Delete from student where id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(studentdeleteSQL);

                        System.out.print("student_id : ");
                        String input = sc.nextLine();
                        pstmt.setString(1, input);


                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                    }

                    break;
            }
        }

        // department
        else if (table_idx == 3){
            department_table();

            stmt = (Statement) conn.createStatement();
            DepartmentSelectpoint = conn.setSavepoint();
            switch (command_idx){

                // select
                case 0:
                    try{
                        Savepoint savepoint = conn.setSavepoint();
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
                    }
                    catch(SQLException e){
                        conn.rollback(DepartmentSelectpoint);
                    }


                    break;

                case 1:
                    try{
                        String departmentSQL = "INSERT INTO department (dept_name, building) VALUES (?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(departmentSQL);

                        String nul_val = sc.nextLine();

                        for(int i = 0; i<2; i++){
                            if (i == 0) System.out.print("dept_name :");
                            else System.out.print("building :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }
                        int result = pstmt.executeUpdate();

                        if (result == 1) System.out.println("성공적으로 삽입 되었습니다.");
                        else System.out.println("입력이 실패했습니다.");
                    }

                    // PK 관련 제약사항 위배시
                    catch (SQLIntegrityConstraintViolationException e){
                        System.out.println("PK 입력값을 다시 확인해주세요");
                    }

                    break;
                //update
                case 2:
                    try{
                        String nul_val = sc.nextLine();

                        String departmentupdateSQL = "Update department set building = ? where dept_name = ?";
                        PreparedStatement pstmt = conn.prepareStatement(departmentupdateSQL);
                        for(int i = 0; i<2; i++){
                            if (i == 0) System.out.print("building :");
                            else System.out.print("dept_name :");

                            String input = sc.nextLine();
                            pstmt.setString(i+1, input);
                        }

                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                    }

                    break;

                //delete
                case 3:
                    try{
                        String nul_val = sc.nextLine();

                        String departmentdeleteSQL = "Delete from department where dept_name = ?";
                        PreparedStatement pstmt = conn.prepareStatement(departmentdeleteSQL);

                        System.out.print("dept_name : ");
                        String input = sc.nextLine();
                        pstmt.setString(1, input);


                        int result = pstmt.executeUpdate();
                        if (result == 1) System.out.println("입력사항으로 갱신하였습니다.");
                        else System.out.println("갱신이 실패했습니다.");
                    }
                    catch (SQLException e){
                        System.out.println("SQL error :" + e);
                    }


                    break;
            }
        }

        else
            return;

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
        System.out.println("--------------------------------");

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
        System.out.println("--------------------");

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
