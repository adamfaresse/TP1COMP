import java.io.*;

public class Test {

    File doc;
    FileReader fileReader;
    BufferedReader buffer;

    Test(String pathname) throws FileNotFoundException {
        this.doc = new File(pathname);
        this.fileReader = new FileReader(doc);
        this.buffer = new BufferedReader(fileReader);
    }

    public static void main(String[] args) throws IOException {
        Test test = new Test("/amuhome/f18010428/Bureau/S4/TP1COMP/src/test");
        int c =0;
        while ( (c= test.buffer.read())!= -1){
            System.out.println(c);
        }
    }
}
