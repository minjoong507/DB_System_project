import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class example {
    public static void main(String[] args)
    {
        search();
    }

    private static void search(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int r = 0;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/board?" +
                    "characterEncoding=utf-8", "root","apmsetup");
            //System.out.println("db connect");

            Scanner sc = new Scanner(System.in);
            System.out.print("0:검색, 1:입력, 2:수정, 3:삭제  ---> ");
            int sn = Integer.parseInt( sc.nextLine() );

            if( sn == 0 ){

                // 데이터베이스 검색
                System.out.print("검색항목(예 : id) : ");
                String en = sc.nextLine();

                System.out.print(en + "를(을) 입력해주세요. : ");
                String cn = sc.nextLine();

                stmt = (Statement) conn.createStatement();
                rs = stmt.executeQuery("select * from freeboard where " + en +
                        " like '%" + cn + "%' ");

                if( rs == null ) System.out.println("해당  내용을 찾을 수 없습니다.");

                while( rs.next() )
                {
                    int id = rs.getInt("id");
                    String user = rs.getString("name");
                    String title = rs.getString("title");
                    int hit = rs.getInt("hit");
                    String contents = rs.getString("contents");

                    System.out.printf("%d %s \t%s %d %s",id,user,title,hit,contents);
                    System.out.println("");
                }

                System.out.println("");
                search();

            }else if( sn == 1 ){

                // 내용 입력
                System.out.print("name : ");
                String name = sc.nextLine();
                System.out.print("title : ");
                String title = sc.nextLine();
                System.out.print("contents : ");
                String contents = sc.nextLine();
                System.out.print("hit : ");
                int hit = sc.nextInt();

                stmt = (Statement) conn.createStatement();
                r = stmt.executeUpdate("insert into freeboard " +
                        "(name,title,contents,hit) value ('" +
                        name + "','" + title + "','" + contents + "'," + hit + ")" );

                if( r == 1 )
                {
                    System.out.println("성공적으로 글이 입력되었습니다.");
                }else{
                    System.out.println("입력 실패!");
                }

                System.out.println("");
                search();

            }else if( sn == 2 )
            {
                // 내용 수정
                System.out.print("(1/2) 수정항목(예 : id=12) : ");
                String en = sc.nextLine();

                System.out.print("(2/2) 수정내용(예 : name='exName', " +
                        "title='exTitle', contents='exContents') : ");
                String ev = sc.nextLine();

                stmt = (Statement) conn.createStatement();
                r = stmt.executeUpdate("update freeboard set " + ev + " where " + en );

                if( r == 0 ){
                    System.out.println("수정 할  내용을 찾을 수 없습니다.");
                }else{
                    System.out.println("수정 되었습니다.");
                }
                System.out.println("");
                search();

                return;
            }else if( sn == 3 )
            {
                // 내용 삭제
                System.out.print("삭제 할 내용(예 : id=12) : ");
                String cn = sc.nextLine();

                stmt = (Statement) conn.createStatement();
                r = stmt.executeUpdate("delete from freeboard where " + cn );

                if( r == 0 ){
                    System.out.println("삭제 할  내용을 찾을 수 없습니다.");
                }else{
                    System.out.println("삭제 되었습니다.");
                }
                System.out.println("");
                search();

                return;

            }else{
                // 입력 오류시.
                System.out.println("입력 오류!!");
                System.out.println("");
                search();
                return;
            }
        }

        catch(ClassNotFoundException cnfe){
            System.out.println("해당 클래스를 찾을 수 없습니다." + cnfe.getMessage());
        }

        catch(SQLException se){
            System.out.println(se.getMessage());
        }

        try{

            conn.close();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}


