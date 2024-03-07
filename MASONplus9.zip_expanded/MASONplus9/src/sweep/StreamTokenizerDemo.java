package sweep;

import java.io.*;

public class StreamTokenizerDemo {

   public static void main(String[] args) {

      String text = "Hello: This is a text \n that will be split "
              + "into tokens. 1+1=2" + "%.3f";
      try {
         // create a new file with an ObjectOutputStream
         FileOutputStream out = new FileOutputStream("test.txt");
         ObjectOutputStream oout = new ObjectOutputStream(out);

         // write something in the file
         oout.writeUTF(text);
         oout.flush();

         // create an ObjectInputStream for the file we created before
         ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream("test.txt"));

         // create a new tokenizer
         Reader r = new BufferedReader(new InputStreamReader(ois));
         StreamTokenizer st = new StreamTokenizer(r);

         // print the stream tokens
         boolean eof = false;
         do {

            int token = st.nextToken();
            switch (token) {
               case StreamTokenizer.TT_EOF:
                  System.out.println("End of File encountered.");
                  eof = true;
                  break;
               case StreamTokenizer.TT_EOL:
                  System.out.println("End of Line encountered.");
                  break;
               case StreamTokenizer.TT_WORD:
                  System.out.println("Word: " + st.sval);
                  break;
               case StreamTokenizer.TT_NUMBER:
                  System.out.println("Number: " + st.nval);
                  break;
               default:
                  System.out.println((char) token + " encountered.");
                  if (token == '!') {
                     eof = true;
                  }
            }
         } while (!eof);


      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}

 
