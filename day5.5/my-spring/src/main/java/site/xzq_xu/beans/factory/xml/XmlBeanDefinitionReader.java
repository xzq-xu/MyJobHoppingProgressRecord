package site.xzq_xu.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.PropertyValue;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.BeanReference;
import site.xzq_xu.beans.factory.support.AbstractBeanDefinitionReader;
import site.xzq_xu.beans.factory.support.BeanDefinitionRegistry;
import site.xzq_xu.core.io.Resource;
import site.xzq_xu.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 从xml文件中读取配置信息
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }


    @Override
    // 重写loadBeanDefinitions方法
    public void loadBeanDefinitions(String location) throws BeansException {
        // 获取资源加载器
        ResourceLoader resourceLoader = getResourceLoader();
        // 根据location获取资源
        Resource resource = resourceLoader.getResource(location);
        // 加载资源
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException | DocumentException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }

    }

    /**
     * 从输入流中读取配置信息
     *
     * @param inputStream
     */
    private void doLoadBeanDefinitions(InputStream inputStream) throws DocumentException {
        // 读取XML文件
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);

        // 获取根元素
        Element beans = document.getRootElement();
        // 获取所有的bean元素
        List<Element> beanList = beans.elements(BEAN_ELEMENT);
        for (Element bean : beanList) {
            // 解析bean元素
            // 获取bean的id和class属性
            String id = bean.attributeValue(ID_ATTRIBUTE);
            String name = bean.attributeValue(NAME_ATTRIBUTE);
            String className = bean.attributeValue(CLASS_ATTRIBUTE);

            //获取init-method 、 destroy-method
            String initMethodName = bean.attributeValue(INIT_METHOD_ATTRIBUTE);
            String destroyMethodName = bean.attributeValue(DESTROY_METHOD_ATTRIBUTE);

            //获取作用域
            String scope = bean.attributeValue(SCOPE_ATTRIBUTE);


            // 创建BeanDefinition对象
            Class<?> clazz = null;
            try {
                // 加载类
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeansException("Could not load class [" + className + "]", e);
            }

            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                // 如果id和name都为空，则使用类名的小写首字母作为beanName
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            //设置init-method、destroy-method
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);

            //设置scope
            if(StrUtil.isNotEmpty(scope)){
                beanDefinition.setScope(scope);
            }

            // 获取bean的属性
            List<Element> propertyList = bean.elements(PROPERTY_ELEMENT);
            for (Element property : propertyList) {

                //解析property标签
                String nameAttr = property.attributeValue(NAME_ATTRIBUTE);
                String valueAttr = property.attributeValue(VALUE_ATTRIBUTE);
                String refAttr = property.attributeValue(REF_ATTRIBUTE);

                if (StrUtil.isEmpty(nameAttr)) {
                    throw new BeansException("The name attribute cannot be empty or null");
                }

                Object value = valueAttr;
                if (StrUtil.isNotEmpty(refAttr)) {
                    // 如果ref属性不为空，则创建BeanReference对象
                    value = new BeanReference(refAttr);
                }
                PropertyValue propertyValue = new PropertyValue(nameAttr, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);


            }


            // 注册BeanDefinition
            if (getRegistry().containsBeanDefinition(beanName)) {
                //不允许 bean重名
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            //使用register注册
            getRegistry().registerBeanDefinition(beanName, beanDefinition);


        }


    }


}
