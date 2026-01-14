package design_pattern.iterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

public class FileReader<E> implements Iterable<E> {

    private final int readSize;
    private final File objFile;
    private final Function<String,E> buidler;
    public FileReader(Function<String,E> buidler,int readSize,File objFile) {
        this.buidler = buidler;
        this.readSize = readSize;
        this.objFile = objFile;
    }



    @Override
    public Iterator<E> iterator() {
        return new FileIterator();
    }

    class FileIterator implements Iterator<E> {
        private List<E> data;
        private int cursor;
        private int readInx;
        public FileIterator() {
            loadObjFromFile();
        }

        private void loadObjFromFile()  {
            try (Stream<String> s = Files.lines(objFile.toPath())){
                data = s.map(buidler::apply).skip(cursor).limit(readSize).toList();
                readInx = 0;
                cursor += data.size();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext() {
            if (readInx<data.size()){
                return true;
            }
            loadObjFromFile();
            return readInx<data.size();
        }

        @Override
        public E next() {
            if(hasNext()){
                return data.get(readInx++);
            }
            throw new NoSuchElementException();
        }
    }
}
