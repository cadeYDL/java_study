package toy.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BootstrapClassLoader {

    private final List<String> classPath;

    public BootstrapClassLoader(String classPath) {
        this.classPath = Arrays.asList(classPath.split(File.pathSeparator));
    }

    public ClassFile loadClass(String fqcn) throws ClassNotFoundException {
        return classPath.stream().
                map(path->tryLoad(path,fqcn)).
                filter(Objects::nonNull).
                findAny().
                orElseThrow(()->new ClassNotFoundException(fqcn+"不存在"));
    }

    private ClassFile tryLoad(String path, String mainClass) {
        File classFilePath = new File(path,mainClass.replace(".",File.separator)+".class");
        if(!classFilePath.exists()){
            return null;
        }
        try{
            byte[] bytees = Files.readAllBytes(classFilePath.toPath());
            return  new ClassFileParser().parse(bytees);
        }catch (Exception e){
            return null;
        }

    }

}
