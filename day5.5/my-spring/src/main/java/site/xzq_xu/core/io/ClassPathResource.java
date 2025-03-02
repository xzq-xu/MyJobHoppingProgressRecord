package site.xzq_xu.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {

    // 定义路径
    private final String path;

    // 构造函数，传入路径
    public ClassPathResource(String path) {
        this.path = path;
    }

    // 获取输入流
    @Override
    public InputStream getInputStream() throws IOException {

        // 从类加载器中获取资源
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        // 如果资源不存在，抛出异常
        if (inputStream == null) {
            throw new FileNotFoundException("can not find resource: " + path);
        }
        // 返回输入流
        return inputStream;

    }
}
