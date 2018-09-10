package org.parabot.core.asm;

import msplit.SplitMethod;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.core.classpath.ClassPath;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes classnodes into runnable classes
 *
 * @author Everel
 * @author Matt
 */
public class ASMClassLoader extends ClassLoader {

    public  ClassPath             classPath;
    private Map<String, Class<?>> classCache;

    public ASMClassLoader(final ClassPath classPath) {
        this.classCache = new HashMap<String, Class<?>>();
        this.classPath = classPath;
    }

    @Override
    protected URL findResource(String name) {
        if (getSystemResource(name) == null) {
            if (classPath.resources.containsKey(name)) {
                try {
                    return classPath.resources.get(name).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
        return getSystemResource(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return getSystemClassLoader().loadClass(name);
        } catch (Exception ignored) {
            System.err.println("Ignored: "+ignored.toString());
        }
        String key = name.replace('.', '/');
        if (classCache.containsKey(key)) {
            return classCache.get(key);
        }
        ClassNode node = classPath.classes.get(key);
        if (node != null) {
            try {
                Class<?> c = nodeToClass(node, name.equals("com/jagex/b") ? 0 : ClassWriter.COMPUTE_MAXS); // no flags otherwise it'll be too big. they compiled it in the first place, they must be <1% away from the limit
                classPath.classes.remove(key);
                classCache.put(key, c);
                return c;
            } catch (RuntimeException e) {
                System.err.println("[ASM nodeToClass] "+e.toString()+" | target: "+name+" | "+e.getMessage());
                if (e.getMessage().equals("Method code too large!")) {
                    // system class loader .. oh wait if i put it on cp in first place then wont even get here
                    e.printStackTrace();
                    // TODO using the node, do asm magic and ... remove the old method and replace with the updated & new using that cool repo?
                    List<MethodNode> methods = node.methods;
                    MethodNode       big     = null;
                    for (MethodNode method : methods) {
                        System.out.println(method.name+" "+method.desc+" "+method.access+" "+method.signature+" "+method.instructions.size()+" "+method.maxStack+" "+method.maxLocals);
                        if (method.instructions.size() > 30000) {
                            big = method;
                            break;
                        }
                    }
                    if (big != null) {
                        System.out.println("identified big method: "+big+" ... attempting to split");
                        SplitMethod.Result result = new SplitMethod(Opcodes.ASM5).split(node.name, big, 2000, 15000, 15000);
                        if (result == null)
                            throw new NullPointerException("splitmethod failed :(");
                        node.methods.remove(big);
                        node.methods.add(result.trimmedMethod);
                        node.methods.add(result.splitOffMethod);

                        System.out.println("attempting new nodeToClass of customized node!");
                        Class<?> c = nodeToClass(node, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES); // splitting removes both, so needed here
                        classPath.classes.remove(key);
                        classCache.put(key, c);
                        return c;
                    }
                }
            }
        }
        return getSystemClassLoader().loadClass(name);
    }

    private final Class<?> nodeToClass(ClassNode node, int flags) {
        if (super.findLoadedClass(node.name) != null) {
            return findLoadedClass(node.name);
        }
        ClassWriter cw = new ClassWriter(flags);
        node.accept(cw);
        byte[] b = cw.toByteArray();
        return defineClass(node.name.replace('/', '.'), b, 0, b.length,
                getDomain());
    }

    private final ProtectionDomain getDomain() {
        CodeSource code = null;
        try {
            code = new CodeSource(new URL("http://www.url.com/"), (Certificate[]) null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new ProtectionDomain(code, getPermissions());
    }

    private final Permissions getPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }

}

