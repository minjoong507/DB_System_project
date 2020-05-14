
import java.io.*;

public class MakeData {

    public static void main(String args[]) throws IOException {
        String createfile="src/data1.csv";
        FileWriter fw = new FileWriter(createfile);
        for(int i = 1; i<=500000; i++){
            fw.append(String.valueOf(i));
            fw.append(',');
            if(i % 5 == 0)
                fw.append('\n');
        }

    }
}