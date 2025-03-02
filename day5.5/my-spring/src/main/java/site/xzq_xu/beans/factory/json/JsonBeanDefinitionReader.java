package site.xzq_xu.beans.factory.json;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.PropertyValue;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.BeanReference;
import site.xzq_xu.beans.factory.support.AbstractBeanDefinitionReader;
import site.xzq_xu.beans.factory.support.BeanDefinitionRegistry;
import site.xzq_xu.core.io.Resource;
import site.xzq_xu.core.io.ResourceLoader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * JsonBeanDefinitionReader，实现BeanDefinitionReader接口，导入json配置文件
 */
public class JsonBeanDefinitionReader extends AbstractBeanDefinitionReader {
    // 构造函数，传入BeanDefinitionRegistry对象
    public JsonBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    // 构造函数，传入BeanDefinitionRegistry对象和ResourceLoader对象
    public JsonBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    // 从Resource对象中加载BeanDefinition
    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            // 获取输入流
            InputStream inputStream = resource.getInputStream();
            // 调用doLoadBeanDefinitions方法加载BeanDefinition
            doLoadBeanDefinitions(inputStream);
        }catch (Exception e){
            throw new BeansException("IOException parsing JSON document from " + resource, e);
        }
    }

    // 从location中加载BeanDefinition
    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        // 获取ResourceLoader对象
        ResourceLoader resourceLoader = getResourceLoader();
        // 根据location获取Resource对象
        Resource resource = resourceLoader.getResource(location);
        // 调用loadBeanDefinitions方法加载BeanDefinition
        loadBeanDefinitions(resource);
    }


    /**
     * 从输入流中读取配置信息
     * @param inputStream
     */
    private void doLoadBeanDefinitions(InputStream inputStream) {
        // 读取XML文件
        String jsonString = IoUtil.read(inputStream, true).toString(StandardCharsets.UTF_8);
        // 获取根元素
        JSONObject rootObj;
        try {
            rootObj = JSONUtil.parseObj(jsonString);
        } catch (Exception e) {
            throw new BeansException("Failed to parse JSON document: " + e.getMessage(), e);
        }
        //获取beans列表
        JSONArray beans = rootObj.getJSONArray("beans");
        if (CollUtil.isEmpty(beans)) {
            return;
        }

        for (int i = 0; i < beans.size(); i++) {
            //获取bean
            JSONObject bean = beans.getJSONObject(i);
            if (bean == null){
                continue;
            }

            //获取bean的id和class
            String id = bean.getStr(ID_ATTRIBUTE);
            String name = bean.getStr(NAME_ATTRIBUTE);
            String className = bean.getStr(CLASS_ATTRIBUTE);
            //创建BeanDefinition对象

            // 创建BeanDefinition对象
            Class<?> clazz = null;
            try {
                // 加载类
                clazz = Class.forName(className);
            }catch (ClassNotFoundException e){
                throw new BeansException("Could not load class [" + className + "]", e);
            }

            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                // 如果id和name都为空，则使用类名的小写首字母作为beanName
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            // 获取bean的属性
            JSONArray properties = bean.getJSONArray(PROPERTY_ELEMENT);
            if (CollUtil.isNotEmpty(properties)) {
                for (int j = 0; j < properties.size(); j++) {
                    JSONObject property = properties.getJSONObject(j);
                    String propertyName = property.getStr(NAME_ATTRIBUTE);
                    Object value = property.get(VALUE_ATTRIBUTE);
                    String ref = property.getStr(REF_ATTRIBUTE);
                    if (propertyName == null) {
                        // 如果name为空抛出异常
                        throw new BeansException("Property name is required");
                    }
                    if (ref != null) {
                        // 如果ref不为空，则将ref作为依赖注入
                        value = new BeanReference(ref);
                    }
                    PropertyValue propertyValue = new PropertyValue(propertyName, value);
                    beanDefinition.getPropertyValues().addPropertyValue(propertyValue);

                }
            }

            // 注册BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);

        }

    }

}
