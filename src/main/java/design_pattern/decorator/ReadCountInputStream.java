package design_pattern.decorator;

import java.io.IOException;
import java.io.InputStream;

public class ReadCountInputStream extends InputStream {
    private InputStream inputStream;
    private int readCount=0;

    public ReadCountInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        readCount++;
        return inputStream.read();
    }

    public int getCount(){
        return readCount;
    }
}
