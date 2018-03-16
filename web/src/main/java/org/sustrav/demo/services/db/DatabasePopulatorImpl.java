package org.sustrav.demo.services.db;

import com.vividsolutions.jts.geom.Point;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.sustrav.demo.utils.GeoHandler;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;

@Service
public class DatabasePopulatorImpl implements DatabasePopulator {

    public static final Logger LOGGER = LoggerFactory.getLogger(DatabasePopulatorImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ResourceLoader resourceLoader;

    private HashMap<String, EntityType> entityDescs = new HashMap<>();

    public void populateDatabase() {
        LOGGER.info("Population initial state of database");

        for (EntityType entityType : em.getMetamodel().getEntities()) {
            entityDescs.put(entityType.getJavaType().getCanonicalName(), entityType);
        }

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader.getClassLoader());
        try {
            for (Resource resource : resolver.getResources("classpath*:/**/*-dbpreload.xml")) {
                parseAndSave(resource);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    protected void parseAndSave(Resource resource) throws Exception {
        LOGGER.info("Loading data from {}", resource.getURL().toString());
        SAXReader reader = new SAXReader();
        Element root = reader.read(resource.getURL()).getRootElement();

        truncateTables(root);

        for (Element element : (List<Element>) root.elements()) {
            EntityType entityType = getClassEntity(element.getName());
            if (entityType == null) {
                LOGGER.error("Element {} is not recognized", element.getName());
                continue;
            }

            Object entity = entityType.getJavaType().newInstance();
            for (Attribute attribute : (List<Attribute>) element.attributes()) {
                processAttribute(entityType, attribute, entity);
            }

            LOGGER.info("Persisting " + entity);
            em.persist(entity);
        }

        em.flush();
    }

    protected void truncateTables(Element root) {
        String truncateEntities = root.attribute("truncateEntities").getValue();
        if (StringUtils.isEmpty(truncateEntities))
            return;

        em.createNativeQuery("SET foreign_key_checks = 0;").executeUpdate();

        for (String entityStr : truncateEntities.split(",")) {
            LOGGER.info("Truncate date from " + entityStr);
            em.createQuery("delete " + entityStr).executeUpdate();
        }

        em.createNativeQuery("SET foreign_key_checks = 1;").executeUpdate();

    }

    protected void processAttribute(EntityType entityType, Attribute attribute, Object entity) throws Exception {
        String propertyName = attribute.getName();
        Object value = attribute.getValue();
        boolean loadEntity = false;

        if (propertyName.endsWith(".id")) {
            propertyName = propertyName.split("\\.")[0];
            loadEntity = true;
        }

        PropertyDescriptor propertyDescriptor = BeanUtils.
                getPropertyDescriptor(entityType.getJavaType(), propertyName);

        if (loadEntity) {
            value = em.find(propertyDescriptor.getPropertyType(), value);
        } else {
            value = castToType(value.toString(), propertyDescriptor);
        }

        if (propertyDescriptor.getWriteMethod() == null)
            throw new RuntimeException("Property " + propertyDescriptor.getName() +
                    " of entity " + entityType.getName() + " does not have write method");

        propertyDescriptor.getWriteMethod().invoke(entity, value);
    }

    protected Object castToType(String strValue, PropertyDescriptor propertyDescriptor) {
        Class type = propertyDescriptor.getPropertyType();

        if (type.equals(String.class))
            return strValue;
        else if (type.equals(Integer.class) || type.equals(int.class))
            return Integer.valueOf(strValue);
        else if (type.equals(Long.class) || type.equals(long.class))
            return Long.valueOf(strValue);
        if (type.equals(Point.class))
            return GeoHandler.getPoint(strValue);

        throw new RuntimeException("Can't cast " + strValue + " to " + type.getName());

    }

    protected EntityType getClassEntity(String name) {
        for (String key : entityDescs.keySet())
            if (key.endsWith("." + name))
                return entityDescs.get(key);
        return null;
    }


}
