package site.xzq_xu.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.PropertyValue;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.BeanReference;
import site.xzq_xu.beans.factory.support.AbstractBeanDefinitionReader;
import site.xzq_xu.beans.factory.support.BeanDefinitionRegistry;
import site.xzq_xu.core.io.Resource;
import site.xzq_xu.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

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
        } catch (IOException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }

    }

    /**
     * 从输入流中读取配置信息
     * @param inputStream
     */
    private void doLoadBeanDefinitions(InputStream inputStream) {
        // 读取XML文件
        Document document = XmlUtil.readXML(inputStream);
        // 获取根元素
        Element root = document.getDocumentElement();
        // 获取所有的bean元素
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element bean) {
                if (BEAN_ELEMENT.equals(bean.getNodeName())) {
                    // 解析bean元素
                    // 获取bean的id和class属性
                    String id = bean.getAttribute(ID_ATTRIBUTE);
                    String name = bean.getAttribute(NAME_ATTRIBUTE);
                    String className = bean.getAttribute(CLASS_ATTRIBUTE);


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

                    for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                        if (bean.getChildNodes().item(j) instanceof Element property) {
                            if (PROPERTY_ELEMENT.equals(property.getNodeName())) {
                                //解析property标签
                                String nameAttr = property.getAttribute(NAME_ATTRIBUTE);
                                String valueAttr = property.getAttribute(VALUE_ATTRIBUTE);
                                String refAttr = property.getAttribute(REF_ATTRIBUTE);

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
                        }
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

    }


}
