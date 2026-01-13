package design_pattern.decorator;

import java.io.*;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        File file = new File("./src/main/java/design_pattern/decorator/Main.java");
        long l = Instant.now().toEpochMilli();
        try(InputStream bufferInputStream = new BufferInputStream(new FileInputStream(file))){
            while (true){
                int bufferRead = bufferInputStream.read();
                if (bufferRead != -1) {
                    break;
                }
            }
            System.out.println("用时:"+ (Instant.now().toEpochMilli() - l)+"ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(InputStream bufferInputStream =  new FileInputStream(file)){
            while (true){
                int bufferRead = bufferInputStream.read();
                if (bufferRead != -1) {
                    break;
                }
            }
            System.out.println("用时:"+ (Instant.now().toEpochMilli() - l)+"ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(ReadCountInputStream bufferInputStream = new ReadCountInputStream(new BufferInputStream(new FileInputStream(file)))){
            while (true){
                int bufferRead = bufferInputStream.read();
                if (bufferRead != -1) {
                    break;
                }
            }
            System.out.println("用时:"+ (Instant.now().toEpochMilli() - l)+"ms"+"count:"+bufferInputStream.getCount());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
