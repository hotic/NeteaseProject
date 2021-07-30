package me.asgard.neteaseproject.config;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * 自定义方言策略
 * 
 * @author lee
 * @date 2018/11/8 14:46
 */
public class CustomMySQL57Dialect extends MySQL57Dialect {

	public CustomMySQL57Dialect() {
		super();
		registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName());
	}
}
