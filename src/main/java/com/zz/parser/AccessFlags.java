package com.zz.parser;

public class AccessFlags {

    protected int access_flags;

    public AccessFlags() {
    }

    public AccessFlags(int a) {
        access_flags = a;
    }

    public int getAccessFlags() {
        return access_flags;
    }

    public int getModifiers() {
        return access_flags;
    }

    public void setAccessFlags(int access_flags) {
        this.access_flags = access_flags;
    }

    public void setModifiers(int access_flags) {
        setAccessFlags(access_flags);
    }

    private void setFlag(int flag, boolean set) {
        if ((access_flags & flag) != 0) {
            if (!set) {
                access_flags ^= flag;
            }
        } else {
            if (set) {
                access_flags |= flag;
            }
        }
    }

    public void isPublic(boolean flag) {
        setFlag(Constants.ACC_PUBLIC, flag);
    }

    public boolean isPublic() {
        return (access_flags & Constants.ACC_PUBLIC) != 0;
    }

    public void isPrivate(boolean flag) {
        setFlag(Constants.ACC_PRIVATE, flag);
    }

    public boolean isPrivate() {
        return (access_flags & Constants.ACC_PRIVATE) != 0;
    }

    public void isProtected(boolean flag) {
        setFlag(Constants.ACC_PROTECTED, flag);
    }

    public boolean isProtected() {
        return (access_flags & Constants.ACC_PROTECTED) != 0;
    }

    public void isStatic(boolean flag) {
        setFlag(Constants.ACC_STATIC, flag);
    }

    public boolean isStatic() {
        return (access_flags & Constants.ACC_STATIC) != 0;
    }

    public void is(boolean flag) {
        setFlag(Constants.ACC_FINAL, flag);
    }

    public boolean is() {
        return (access_flags & Constants.ACC_FINAL) != 0;
    }

    public void isSynchronized(boolean flag) {
        setFlag(Constants.ACC_SYNCHRONIZED, flag);
    }

    public boolean isSynchronized() {
        return (access_flags & Constants.ACC_SYNCHRONIZED) != 0;
    }

    public void isVolatile(boolean flag) {
        setFlag(Constants.ACC_VOLATILE, flag);
    }

    public boolean isVolatile() {
        return (access_flags & Constants.ACC_VOLATILE) != 0;
    }

    public void isTransient(boolean flag) {
        setFlag(Constants.ACC_TRANSIENT, flag);
    }

    public boolean isTransient() {
        return (access_flags & Constants.ACC_TRANSIENT) != 0;
    }

    public void isNative(boolean flag) {
        setFlag(Constants.ACC_NATIVE, flag);
    }

    public boolean isNative() {
        return (access_flags & Constants.ACC_NATIVE) != 0;
    }

    public void isInterface(boolean flag) {
        setFlag(Constants.ACC_INTERFACE, flag);
    }

    public boolean isInterface() {
        return (access_flags & Constants.ACC_INTERFACE) != 0;
    }

    public void isAbstract(boolean flag) {
        setFlag(Constants.ACC_ABSTRACT, flag);
    }

    public boolean isAbstract() {
        return (access_flags & Constants.ACC_ABSTRACT) != 0;
    }

    public void isStrictfp(boolean flag) {
        setFlag(Constants.ACC_STRICT, flag);
    }

    public boolean isStrictfp() {
        return (access_flags & Constants.ACC_STRICT) != 0;
    }
}
