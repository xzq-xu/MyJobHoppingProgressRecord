package site.xzq_xu.test.ioc;

import cn.hutool.core.io.IoUtil;
import org.junit.jupiter.api.Test;
import site.xzq_xu.core.io.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源与资源加载器测试
 */
public class ResourceAndResourceLoaderTest {

    @Test
    public void testResourceAndResourceLoader() throws IOException {
        //创建资源加载器
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        //加载classpath下的资源
        Resource resource = resourceLoader.getResource("classpath:hello.txt");
        //获取资源输入流
        InputStream inputStream = resource.getInputStream();
        //读取资源内容
        String content = IoUtil.readUtf8(inputStream);
        //打印资源内容
        System.out.println(content);
        //断言资源内容是否为"hello world"
        assert (content) != null;

        //加载文件系统资源
        resource = resourceLoader.getResource("src/test/resources/hello.txt");
        //断言资源类型是否为FileSystemResource
        assert  (resource instanceof FileSystemResource);
        //获取资源输入流
        inputStream = resource.getInputStream();
        //读取资源内容
        content = IoUtil.readUtf8(inputStream);
        //打印资源内容
        System.out.println(content);
        //断言资源内容是否为"hello world"
        assert (content) != null;

        //加载url资源
        resource = resourceLoader.getResource("https://www.baidu.com");
        //断言资源类型是否为UrlResource
        assert (resource instanceof UrlResource);
        //获取资源输入流
        inputStream = resource.getInputStream();
        //读取资源内容
        content = IoUtil.readUtf8(inputStream);
        //打印资源内容
        System.out.println(content);
    }
}
