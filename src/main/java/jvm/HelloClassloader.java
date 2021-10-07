package jvm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class HelloClassloader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = new HelloClassloader().findClass("Hello");
        Object instance = clazz.newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(instance);
    }

    @Override
    protected Class<?> findClass(String name) {
        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("Hello.xlass");


        byte[] fileBytes = readStream(inStream);
        byte[] bytes = decode(fileBytes);
        return defineClass(name, bytes, 0, bytes.length);

    }

    //解码
    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }

    //读取
    public static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while (true) {

                if (!((len = inStream.read(buffer)) != -1)) break;

                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outStream.toByteArray();
    }

}
