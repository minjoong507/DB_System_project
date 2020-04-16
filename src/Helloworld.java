import java.sql.*;
import java.util.Scanner;

public class Helloworld {
    static Scanner sc = new Scanner(System.in);
    static int balance, new_balance;

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        String updatesql = "update account set balance =? where acct_no =?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/study_db" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ", "root","9848");
            Statement stmt= conn.createStatement();
            pstmt = conn.prepareStatement(updatesql);
            ResultSet rset;

            System.out.println("");
            System.out.print("1. 출금계좌번호를 입력하세요: ");
            String Withdrawal_account_num = sc.nextLine();
            System.out.println("");

            System.out.print("2. 입금계좌번호를 입력하세요: ");
            String Deposit_account_num = sc.nextLine();
            System.out.println("");

            System.out.print("3. 송금액 입력하세요: ");
            int Remittance = sc.nextInt();
            System.out.println("");

            rset = stmt.executeQuery("select balance from account where acct_no=" + Withdrawal_account_num);
            while (rset.next()) {
                balance = rset.getInt("balance");
            }

            if (balance - Remittance <= 0){
                System.out.println("송금액이 잔고보다 큽니다!");
            }
            else{
                rset = stmt.executeQuery("select balance from account where acct_no=" + Deposit_account_num);
                while (rset.next()) {
                    new_balance = rset.getInt("balance");
                }

                pstmt.setInt(1, balance - Remittance);
                pstmt.setString(2, Withdrawal_account_num);
                pstmt.executeUpdate();

                pstmt.setInt(1, new_balance + Remittance);
                pstmt.setString(2, Deposit_account_num);
                pstmt.executeUpdate();
            }

            stmt.close();
            pstmt.close();
            conn.close();
        }
        catch(SQLException sqle) {
            System.out.println("SQLException: "+sqle);
        }
    }
}