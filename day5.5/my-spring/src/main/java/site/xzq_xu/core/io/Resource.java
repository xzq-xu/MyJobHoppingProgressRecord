package site.xzq_xu.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源的抽象和访问接口
 */
public interface Resource {
    /**
     * 获取资源的输入流
     * @return 输入流
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;
}
