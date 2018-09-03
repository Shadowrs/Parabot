package org.parabot.core.asm.wrappers;

import java.util.Arrays;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.asm.ASMUtils;
import org.parabot.core.asm.adapters.AddCallbackAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class is used for injecting a callback into a methodnode
 *
 * @author Everel
 */
public class Callback implements Injectable {
    private MethodNode method;
    private String     invokeClass;
    private String     invokeMethod;
    private String     desc;
    private int[]      args;
    private boolean    conditional;

    private final String TO_STRING;

    public Callback(final String className, final String methodName,
                    final String methodDesc, final String callbackClass,
                    final String callbackMethod, final String callbackDesc, String args, final boolean conditional) {

        TO_STRING = String.format("[Callback] %s.%s (%s) : %s | %s -> %s.%s %s", callbackClass, callbackMethod, args, callbackDesc, conditional, className, methodName, methodDesc);

        this.method = ASMUtils.getMethod(className, methodName, methodDesc);
        this.invokeClass = callbackClass;
        this.invokeMethod = callbackMethod;
        this.desc = callbackDesc;
        this.conditional = conditional;
        if (args.contains(",")) {
            final String[] strArgs = args.split(",");
            this.args = new int[strArgs.length];
            for (int i = 0; i < this.args.length; i++) {
                this.args[i] = Integer.parseInt(strArgs[i]);
            }
        } else {
            this.args = new int[]{ Integer.parseInt(args) };
        }
    }

    @Override
    public void inject() {
        getAdapter().inject();
    }

    public AddCallbackAdapter getAdapter() {
        return new AddCallbackAdapter(this.method, this.invokeClass,
                this.invokeMethod, this.desc, this.args, this.conditional);
    }

    @Override
    public String toString() {
        return TO_STRING;
    }
}
