package site.xzq_xu.core.io;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * 默认的ResourceLoader实现
 */
public class DefaultResourceLoader implements ResourceLoader {

    //定义classpath前缀
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    //实现ResourceLoader接口的getResource方法
    @Override
    public Resource getResource(String location) {
        //如果location以classpath:开头
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            //classpath下的资源
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            try {
                //尝试当成url来处理
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException ex) {
                //当成文件系统下的资源处理
                return new FileSystemResource(location);
            }
        }
    }
}
