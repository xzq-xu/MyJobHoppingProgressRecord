package site.xzq_xu.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Bean的属性信息
 * 使用lombok简化代码
 */
@Getter
@AllArgsConstructor
public class PropertyValue {
    // 属性名称
    private final String name;
    // 属性值
    private final Object value;
}
