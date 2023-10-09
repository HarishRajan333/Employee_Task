package com.mycompany.employee.Mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    
    public static List<String> findEntityKey(Class<?> entityClass) {
        
        return Arrays.asList(entityClass.getDeclaredFields()).stream()
                .map(Field::getName)
                .collect(Collectors.toList());
    }
    
    public void copyFieldValue(Object entity, Object targetEntity) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        List<String> keyNames = findEntityKey(entity.getClass());
        for (String fieldName : keyNames) {
            
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object sourceValue = field.get(entity);
            
            Field targetFiled = targetEntity.getClass().getDeclaredField(fieldName);
            if (Objects.nonNull(sourceValue)) {
                
                targetFiled.setAccessible(true);
                targetFiled.set(targetEntity, sourceValue);
            }
        }
    }
    
    public static String[] getNullProertyNames(Object src) {
        BeanWrapper sr = new BeanWrapperImpl(src);
        
        return Arrays.asList(sr.getPropertyDescriptors()).stream()
                .filter(p -> sr.getPropertyValue(p.getName()) == null)
                .map(PropertyDescriptor::getName)
                .toArray(String[]::new);
    }
    
    public void copyNonNullPropertys(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullProertyNames(src));
    }
    
}
