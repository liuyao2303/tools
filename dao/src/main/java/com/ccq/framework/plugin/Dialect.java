package com.ccq.framework.plugin;

public abstract class Dialect {

	public abstract String builderSql(String rawSql, int OFFSET, int LIMIT);
}
