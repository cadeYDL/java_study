package toy.jvm;


import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;
import tech.medivh.classpy.classfile.MethodInfo;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HotSpot {
    private final   String mainClass;
    private final BootstrapClassLoader classLoader;
    public HotSpot(String mainClass,String classPathString){
        this.mainClass = mainClass;
        this.classLoader = new BootstrapClassLoader(classPathString);
    }



    public void start() throws Exception {
        ClassFile classFile = classLoader.loadClass(mainClass);
        MethodInfo methodInfo = classFile.getMainMethod();
        StackFrame mainFrame = new StackFrame(classFile.getMainMethod(),classFile.getConstantPool());
        Thread mainThread = new Thread("main",mainFrame,classLoader);
        mainThread.start();
    }


}
