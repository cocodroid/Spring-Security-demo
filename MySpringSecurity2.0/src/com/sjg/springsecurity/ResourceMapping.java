package com.sjg.springsecurity;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.object.MappingSqlQuery;

public class ResourceMapping extends MappingSqlQuery {
	protected ResourceMapping(DataSource dataSource, String resourceQuery) {
		super(dataSource, resourceQuery);
		compile();
	}

	protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
		String url = rs.getString(1);
		String role = rs.getString(2);
		Resource resource = new Resource(url, role);
		return resource;
	}
	
}
