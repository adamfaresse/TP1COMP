import java.io.*;

public class Test {

    File doc;
    File doc2;
    FileReader fileReader;
    BufferedReader buffer;
    FileWriter fw ;

    Test(String pathname) throws IOException {
        this.doc = new File(pathname);
        this.doc2 = new File(pathname+"v2");
        this.fileReader = new FileReader(doc);
        this.buffer = new BufferedReader(fileReader);
        this.fw = new FileWriter(doc2,true);
    }

    public static void main(String[] args) throws IOException {
        Test test = new Test("src/test");
        int c =0;
        while ( (c= test.buffer.read())!= -1){
            System.out.println(c);
        }
        for (int i = 0; i <5 ; i++) {
            test.fw.write(i+"\n");
        }test.fw.close();
    }
}
