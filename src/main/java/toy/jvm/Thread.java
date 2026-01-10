package toy.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.*;
import tech.medivh.classpy.classfile.constant.ConstantInfo;
import tech.medivh.classpy.classfile.constant.ConstantMethodrefInfo;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

public class Thread {
    private static final Pattern patternInx = Pattern.compile("#([0-9]+)-");
    private static final Pattern patternType = Pattern.compile("\\((.+)\\)");

    private final String name;
    private final JvmStack stack;
    private final BootstrapClassLoader classLoader;

    private final PcRegister pcRegister;
    public Thread(String name, StackFrame frame, BootstrapClassLoader classLoader,Object... args){
        this.name = name;
        this.stack = new JvmStack();
        this.stack.push(frame);
        this.pcRegister = new PcRegister(this.stack);
        this.classLoader = classLoader;
    }

    public void start() throws Exception {
        for(Instruction instruction:pcRegister){
//            System.out.println(instruction);
            ConstantPool constantPool = stack.peek().constantPool;
            switch (instruction.getOpcode()){
                case getstatic -> {
                    GetStatic getStatic = (GetStatic) instruction;
                    String className = getStatic.getClassName(constantPool);
                    String fieldName = getStatic.getFieldName(constantPool);
                    Object staticField;
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Field declaredField = aClass.getDeclaredField(fieldName);
                        staticField = declaredField.get(null);
                        stack.peek().pushObjectToOperandStack(staticField);
                    }
                }
                case iconst_0->stack.peek().pushObjectToOperandStack(0);
                case iconst_1 -> stack.peek().pushObjectToOperandStack(1);
                case iconst_2 -> stack.peek().pushObjectToOperandStack(2);
                case iconst_3 -> stack.peek().pushObjectToOperandStack(3);
                case iconst_4 -> stack.peek().pushObjectToOperandStack(4);
                case iload_0 -> stack.peek().pushObjectToOperandStack(stack.peek().localVars[0]);
                case iload_1 -> stack.peek().pushObjectToOperandStack(stack.peek().localVars[1]);
                case iload_2-> stack.peek().pushObjectToOperandStack(stack.peek().localVars[2]);

                case invokevirtual -> {
                    InvokeVirtual invokeVirtual = (InvokeVirtual) instruction;
                    ConstantMethodrefInfo methodInfo = invokeVirtual.getMethodInfo(constantPool);
                    String className = methodInfo.className(constantPool);
                    String methodName = methodInfo.methodName(constantPool);
                    List<String> params = methodInfo.paramClassName(constantPool);
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Method declaredMethod = aClass.getDeclaredMethod(methodName,
                                params.stream().map(this::nameToClass).toArray(Class[]::new));
                        Object[] args = new Object[params.size()];
                        for (int index = args.length - 1; index >= 0; index--) {
                            args[index] = stack.peek().operandStack.pop();
                        }
                        Object o =stack.peek().operandStack.pop();
                        Object result = declaredMethod.invoke(o, args);
                        if (!methodInfo.isVoid(constantPool)) {
                            stack.peek().pushObjectToOperandStack(result);
                        }
                        break;
                    }
                    ClassFile classFile = classLoader.loadClass(className);
                    MethodInfo finalMethodInfo = getFinalMethodInfo(classFile, methodName,params);
                    Object[] args = new Object[params.size() + 1];
                    for (int index = args.length - 1; index >= 0; index--) {
                        args[index] = stack.peek().operandStack.pop();
                    }
                    StackFrame stackFrame = new StackFrame(finalMethodInfo, classFile.getConstantPool(), args);
                    stack.push(stackFrame);
                }
                case invokestatic -> {
                    InvokeStatic invokeStatic = (InvokeStatic) instruction;
                    ConstantMethodrefInfo methodInfo = invokeStatic.getMethodInfo(constantPool);
                    String className = methodInfo.className(constantPool);
                    String methodName = methodInfo.methodName(constantPool);
                    List<String> params = methodInfo.paramClassName(constantPool);
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Method declaredMethod = aClass.getDeclaredMethod(methodName,
                                params.stream().map(this::nameToClass).toArray(Class[]::new));
                        Object[] args = new Object[params.size()];
                        for (int index = args.length - 1; index >= 0; index--) {
                            args[index] = stack.peek().operandStack.pop();
                        }
                        Object result = declaredMethod.invoke(null, args);
                        if (!methodInfo.isVoid(constantPool)) {
                            stack.peek().pushObjectToOperandStack(result);
                        }
                        break;
                    }
                    ClassFile classFile = classLoader.loadClass(className);
                    MethodInfo finalMethodInfo = getFinalMethodInfo(classFile, methodName,params);
                    Object[] args = new Object[params.size()];
                    for (int index = args.length - 1; index >= 0; index--) {
                        args[index] = stack.peek().operandStack.pop();
                    }
                    StackFrame stackFrame = new StackFrame(finalMethodInfo, classFile.getConstantPool(), args);
                    stack.push(stackFrame);
                }
                case _return -> {
                    var val = this.stack.pop();
                }
                case ireturn -> {
                    int result = (int) this.stack.pop().operandStack.pop();
                    stack.peek().pushObjectToOperandStack(result);
                }
                case if_icmple -> {
                    int value2 = (int) stack.peek().operandStack.pop();
                    int value1 = (int) stack.peek().operandStack.pop();
                    if (value1 <= value2) {
                        Branch branch = (Branch) instruction;
                        int jumpTo = branch.getJumpTo();
                        stack.peek().jumpTo(jumpTo);
                    }
                }
                case if_icmpge->{
                    int value2 = (int) stack.peek().operandStack.pop();
                    int value1 = (int) stack.peek().operandStack.pop();
                    if (value1 >= value2) {
                        Branch branch = (Branch) instruction;
                        int jumpTo = branch.getJumpTo();
                        stack.peek().jumpTo(jumpTo);
                    }
                }
                case _goto->{
                    Branch branch = (Branch) instruction;
                    int jumpTo = branch.getJumpTo();
                    stack.peek().jumpTo(jumpTo);
                }
                case ldc -> {
                    Ldc ldc = (Ldc) instruction;
                    ConstantInfo constantInfo = ldc.getConstantInfo(constantPool);
                    stack.peek().pushObjectToOperandStack(convConstantType(constantInfo));
                }
                case ifle -> {
                    int value = (int) stack.peek().operandStack.pop();
                    if (value <= 0) {
                        Branch branch = (Branch) instruction;
                        int jumpTo = branch.getJumpTo();
                        stack.peek().jumpTo(jumpTo);
                    }
                }
                case isub -> {
                    int value1 = (int) stack.peek().operandStack.pop();
                    int value2 = (int) stack.peek().operandStack.pop();
                    stack.peek().pushObjectToOperandStack(value2-value1);
                }
                case ineg->{
                    int value = (int) stack.peek().operandStack.pop();
                    value = -value;
                    stack.peek().pushObjectToOperandStack(value);
                }
                case iadd->{
                    int value1 = (int) stack.peek().operandStack.pop();
                    int value2 = (int) stack.peek().operandStack.pop();
                    stack.peek().pushObjectToOperandStack(value1+value2);

                }
                case bipush -> {
                    Bipush bipush = (Bipush) instruction;
                    int value = bipush.getPushByte();
                    stack.peek().pushObjectToOperandStack(value);
                }

                default -> {
                    throw new Exception("unsupported opcode"+instruction);
                }
            }
        }
    }
    private final static Pattern patternMethod = Pattern.compile("\\((.*)\\)");
    private static MethodInfo getFinalMethodInfo(ClassFile classFile, String methodName, List<String> params) {
        var methods = classFile.getMethods(methodName);
        for(var m:methods){
            var matcher = patternMethod.matcher(m.getDesc());
            if(matcher.find()){
                String val = matcher.group(1);
                if (val.isEmpty()){
                    if (params.isEmpty()){
                        return m;
                    }else{
                        continue;
                    }
                }

                String[] items = matcher.group(1).split(";");
                boolean is=true;
                int inx =0;
                for(String i:items){
                    if(inx>=params.size()){
                        is = false;
                        break;
                    }
                    if (i.contains("/")){
                        if(!i.contains(params.get(inx).replaceAll("\\.", File.separator))){
                            is = false;
                            break;
                        }
                        inx++;
                    }else{
                         for(var innerI :i.split("")){
                             if(inx>=params.size()){
                                 is = false;
                                 break;
                             }
                             switch (innerI){
                                 case "I"->{
                                     if (!params.get(inx).equals("int")){
                                         is = false;
                                         break;
                                     }
                                     inx++;
                                 }
                                 default -> throw new RuntimeException("未支持的type");
                             }
                         }
                         if(!is){
                             break;
                         }
                    }

                }
                if (is&&inx==params.size()){
                    return m;
                }
            }
        }
        return null;
    }


    private Object convConstantType(ConstantInfo instruction){
        var matcher = patternType.matcher(instruction.getName());
        if (matcher.find()){
            switch (matcher.group(1)){
                case "String"->{
                    return instruction.getDesc();
                }
                case "int"->{
                    return Integer.valueOf(instruction.getDesc());
                }
                case "float"->{
                    return Float.valueOf(instruction.getDesc());
                }
                default -> throw new RuntimeException("未实现的类型");
            }
        }
        return null;
    }

    private Object nameToClass(String className) {
        if (className.equals("int")) {
            return int.class;
        }else if(className.equals("float")){
            return float.class;
        }else if(className.equals("boolean")){
            return boolean.class;
        }
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

}
