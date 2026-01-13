package design_pattern.decorator;

import java.io.IOException;
import java.io.InputStream;

public class BufferInputStream extends InputStream {
    private InputStream inputStream;

    private byte[] buffer = new byte[1024];
    private int capacity = -1;
    private int posited = -1;
    public BufferInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int read() throws IOException {
        if(canReadFromBuffer()){
            return readFromBuffer();
        }
        refreshBuffer();
        if(canReadFromBuffer()){
            return readFromBuffer();
        }
        return -1;
    }

    private int readFromBuffer() {
        return buffer[posited++]& 0xFF;
    }

    private void refreshBuffer() throws IOException {
        capacity = this.inputStream.read(buffer);
        posited = 1;
    }

    public boolean canReadFromBuffer(){
        if(posited==-1||capacity==-1){
            return false;
        }
        if(posited==capacity){
            return false;
        }
        return true;
    }

    public void close() throws IOException {
        inputStream.close();
    }
}
