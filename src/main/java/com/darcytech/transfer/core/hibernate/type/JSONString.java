package com.darcytech.transfer.core.hibernate.type;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.darcytech.transfer.core.factory.ObjectMapperBeanFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONString implements UserType, ParameterizedType {

    private Class<?> targetClass;

    private Boolean isArray = false;

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.CLOB};
    }

    @Override
    public Class<?> returnedClass() {
        return isArray ? targetClass : List.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {

        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        try {
            String value = rs.getString(names[0]);
            if (value == null) {
                return null;
            }

            Object object = null;
            if (isArray) {
                ObjectMapper objectMapper = ObjectMapperBeanFactory.getObjectMapper();

                JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass);
                object = objectMapper.readValue(value, type);
            } else {
                ObjectMapper objectMapper = ObjectMapperBeanFactory.getObjectMapper();
                object = objectMapper.readValue(value, targetClass);
            }

            return object;
        } catch (IOException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        try {
            if (value == null) {
                st.setNull(index, Types.CLOB);
                return;
            }
            ObjectMapper objectMapper = ObjectMapperBeanFactory.getObjectMapper();
            String stringValue = objectMapper.writeValueAsString(value);
            st.setString(index, stringValue);
        } catch (JsonProcessingException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return null;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return null;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        String targetClassName = parameters.getProperty("targetClass");
        try {
            targetClass = Class.forName(targetClassName);
        } catch (ClassNotFoundException cnfe) {
            throw new HibernateException("Target class not found", cnfe);
        }

        String isArrayParam = parameters.getProperty("isArray");
        if (StringUtils.isNotEmpty(isArrayParam)) {
            isArray = Boolean.valueOf(isArrayParam);
        }
    }

}
