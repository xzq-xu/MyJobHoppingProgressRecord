package site.xzq_xu.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlResource implements Resource {

    // 定义一个URL类型的变量
    private final URL url;

    // 构造函数，传入一个URL类型的参数
    public UrlResource(URL url) {
        this.url = url;
    }

    // 实现Resource接口的getInputStream方法，返回一个InputStream类型的输入流
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            // 打开URL连接
            URLConnection connection = this.url.openConnection();
            // 返回输入流
            return connection.getInputStream();
        } catch (IOException e) {
            // 抛出异常
            throw e;
        }
    }
}
