import java.io.*;
import java.util.*;

public class MakeData {
    static String[] namebox = {"Bob", "Max", "Harry", "Adam", "Tom", "Min"};
    static String[] dept_n = {"Physics", "Comp.sci", "Biology", "English", "Economics"};
    static String[] publisher = {"Fox", "Wolf", "Dog", "CAT"};
    static Random rand = new Random();

    public static void main(String[] args) throws IOException {
        MakeDepartment();
        MakeStudentData();
        MakeBookData();
        MakeRentbookdata();
    }

    public static void MakeDepartment() throws IOException{
        String createfile = "src/department_data.csv";
        FileWriter fw = new FileWriter(createfile);
        int building = 100;
        for(String dept_name : dept_n){
            fw.append(dept_name).append(",");
            fw.append(String.valueOf(building)).append("\n");
            building++;

        }
        fw.flush();
        fw.close();

    }


    public static void MakeRentbookdata() throws IOException{
        String createfile="src/rentbook_data.csv";
        FileWriter fw = new FileWriter(createfile);
        for(int i = 0; i<10000; i++){
            for(int j = 0; j<5; j++){
                if (j == 0) {
                    int idx = rand.nextInt(15000);
                    fw.append("CAU").append(String.valueOf(idx));
                }
                else if (j == 1) {
                    int idx = rand.nextInt(13000);
                    fw.append("S").append(String.valueOf(idx));
                }
                else if (j == 2){
                    int year = rand.nextInt(5) + 2015;
                    fw.append(String.valueOf(year));
                }
                else if (j==3){
                    int month = rand.nextInt(11) + 1;
                    fw.append(String.valueOf(month));
                }
                else {
                    int day = rand.nextInt(25) + 1;
                    fw.append(String.valueOf(day));
                }
                fw.append(",");
            }
            fw.append("\n");
        }
        fw.flush();
        fw.close();
    }


    public static void MakeStudentData() throws IOException {
        String createfile="src/student_data.csv";
        FileWriter fw = new FileWriter(createfile);
        for(int i = 0; i<13000; i++){
            for(int j = 0; j<4; j++){
                if (j == 0)
                    fw.append("S").append(String.valueOf(i));

                else if (j == 1) {
                    int idx = rand.nextInt(6);
                    String name = namebox[idx];
                    fw.append(name).append(String.valueOf(i));
                }
                else if (j == 2){
                    int idx = rand.nextInt(5);
                    String dept = dept_n[idx];
                    fw.append(dept);
                }
                else {
                    int age = rand.nextInt(10) + 20;
                    fw.append(String.valueOf(age));
                }
                fw.append(",");
            }
            fw.append("\n");
        }
        fw.flush();
        fw.close();
    }


    public static void MakeBookData() throws IOException {
        String createfile="src/book_data.csv";
        FileWriter fw = new FileWriter(createfile);
        for(int i = 0; i<15000; i++){
            for(int j = 0; j<5; j++){
                if (j == 0)
                    fw.append("CAU").append(String.valueOf(i));
                else if (j == 1)
                    fw.append("B").append(String.valueOf(i));
                else if (j == 2) {
                    int idx = rand.nextInt(100);
                    fw.append("author").append(String.valueOf(idx));
                }
                else if (j == 3) {
                    int idx = rand.nextInt(4);
                    fw.append(publisher[idx]);
                }
                else {
                    int publication_year = rand.nextInt(30) + 2000;
                    fw.append(String.valueOf(publication_year));
                }
                fw.append(",");
            }
            fw.append("\n");
        }
        fw.flush();
        fw.close();
    }
}