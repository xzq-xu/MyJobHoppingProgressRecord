package site.xzq_xu.beans;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存Bean的多个属性信息
 * 内部使用List保存PropertyValue
 */
@NoArgsConstructor
public class PropertyValues {
    // 使用List保存PropertyValue
    private final List<PropertyValue> propertyValues = new ArrayList<>();

    // 添加PropertyValue
    public void addPropertyValue(PropertyValue pv) {
        //key 这里可以方便的在记录属性之前执行某些操作
        this.propertyValues.add(pv);
    }


    // 获取所有PropertyValue
    public PropertyValue[] getPropertyValues() {
        return this.propertyValues.toArray(new PropertyValue[0]);
    }

    // 根据属性名获取PropertyValue
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValues) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }


}
