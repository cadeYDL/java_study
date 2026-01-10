package toy.jvm;

import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.Instruction;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class StackFrame {

     MethodInfo methodInfo;
     Object[] localVars;
     Deque<Object> operandStack;
     List<Instruction> codes;
      ConstantPool constantPool;
      int currentInx;
    public StackFrame(MethodInfo methodInfo, ConstantPool constantPool,Object... args){
        this.methodInfo = methodInfo;
        this.localVars = new Object[methodInfo.getMaxLocals()];
        this.operandStack = new ArrayDeque<>() ;
        this.constantPool = constantPool;
        this.codes = methodInfo.getCodes();
        System.arraycopy(args,0,localVars,0,args.length);

    }

    public Instruction getNextInstruction() {
        return codes.get(currentInx++);
    }

    public void pushObjectToOperandStack(Object staticField) {
        operandStack.push(staticField);
    }

    public void jumpTo(int toIndex){
        for(int i=0;i<codes.size();i++){
            if (toIndex==codes.get(i).getPc()){
                currentInx = i;
                return;
            }
        }
    }

}


