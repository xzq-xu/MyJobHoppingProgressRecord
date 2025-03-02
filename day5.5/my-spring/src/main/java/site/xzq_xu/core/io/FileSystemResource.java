package site.xzq_xu.core.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * 文件系统资源
 * 根据path获取文件系统中的资源
 */
public class FileSystemResource implements Resource {

    private final String filePath;

    public FileSystemResource(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 根据文件路径获取输入流，支持相对路径和 "."、".."。
     *
     * @return 文件的输入流
     * @throws IOException 如果文件不存在或无法读取
     */
//    @Override
//    public InputStream getInputStream() throws IOException {
//        // 创建 File 对象，支持相对路径和绝对路径
//        File file = new File(path);
//        // 返回文件输入流
//        return new FileInputStream(file);
//    }


    @Override
    public InputStream getInputStream() throws IOException {
        // 创建 File 对象，支持相对路径和绝对路径
        try {
            // 将 path 转换为 Path 对象
            Path path = new File(filePath).toPath();
            // 返回 Path 对象的输入流
            return Files.newInputStream(path);
        } catch (NoSuchFileException e) {
            // 如果文件不存在，抛出 FileNotFoundException 异常
            throw new FileNotFoundException(e.getMessage());
        }
    }

}


