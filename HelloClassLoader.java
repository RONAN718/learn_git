package jvm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) throws ClassNotFoundException {
        HelloClassLoader loader = new HelloClassLoader();
        Class<?> aClass = loader.findClass("Hello");
        try {
            Object obj = aClass.newInstance();
            Method method = aClass.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //本地文件的绝对路径
        String localPath = "file:///C:/Users/16431/Desktop/jvm/src/resources/Hello.xlass";
        byte[] bytes = null;
        Path path = null;
        try {
            path = Paths.get(new URI(localPath));
            bytes = decode(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    public byte[] decode(Path path) {
        byte[] res = null;
        try {
            res = Files.readAllBytes(path);
            //对每个字节进行与255相减操作
            if (res != null && res.length > 0) {
                for (int i = 0; i < res.length; i++) {
                    res[i] = (byte) (255 - res[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}