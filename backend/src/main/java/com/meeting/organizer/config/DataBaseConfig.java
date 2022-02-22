package com.meeting.organizer.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.springframework.context.annotation.Configuration;

import java.sql.Types;

@Slf4j
@Configuration
public class DataBaseConfig extends PostgreSQL82Dialect {
    public DataBaseConfig() {
        log.info("Init PostgreSQLDialectCustom");
        registerColumnType(Types.BLOB, "bytea");
    }

    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
            return BinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }
}