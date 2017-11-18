package com.ccq.framework.jdbc.dao;

import com.ccq.framework.lang.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JdbcTempleteDao extends JdbcTemplate{

    private Logger logger = LoggerFactory.getLogger(JdbcTempleteDao.class);

    public JdbcTempleteDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
        try {
            T r = super.query(sql, rse);
            return r;
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        super.query(sql, rch);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            List<T> r = super.query(sql, rowMapper);
            return r;
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql) throws DataAccessException {
        return super.queryForMap(sql);
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {

         try{
             return super.queryForObject(sql,rowMapper);
         }catch (EmptyResultDataAccessException e) {
             logger.info("EmptyResultDataAccessException : " + e.toString());
             return null;
         }

    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {

        try{
            return super.queryForObject(sql, requiredType);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
        return super.queryForList(sql, elementType);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
        return super.queryForList(sql);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
        return super.queryForRowSet(sql);
    }

    @Override
    public int update(String sql) throws DataAccessException {
        return super.update(sql);
    }

    @Override
    public int[] batchUpdate(String... sql) throws DataAccessException {
        return super.batchUpdate(sql);
    }

    @Override
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
        return super.execute(psc, action);
    }

    @Override
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
        return super.execute(sql, action);
    }

    @Override
    public <T> T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
        try{
            return super.query(psc, pss, rse);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
        try{
            return super.query(psc, rse);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
        try{
            return super.query(sql, pss, rse);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException {
        try{
            return super.query(sql, args, argTypes, rse);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
        try{
            return super.query(sql, args, rse);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
        try{
            return super.query(sql, rse, args);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
        super.query(psc, rch);
    }

    @Override
    public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
        super.query(sql, pss, rch);
    }

    @Override
    public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
        super.query(sql, args, argTypes, rch);
    }

    @Override
    public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
        super.query(sql, args, rch);
    }

    @Override
    public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
        super.query(sql, rch, args);
    }

    @Override
    public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
        return super.query(psc, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
        return super.query(sql, pss, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
        return super.query(sql, args, argTypes, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        return super.query(sql, args, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        return super.query(sql, rowMapper, args);
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
        try{
            return super.queryForObject(sql, args, argTypes, rowMapper);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        try{
            return super.queryForObject(sql, args, rowMapper);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        try{
            return super.queryForObject(sql, rowMapper, args);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType) throws DataAccessException {
        try{
            return super.queryForObject(sql, args, argTypes, requiredType);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
        try{
            return super.queryForObject(sql, args, requiredType);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
        try{
            return super.queryForObject(sql, requiredType, args);
        }catch (EmptyResultDataAccessException e) {
            logger.info("EmptyResultDataAccessException : " + e.toString());
            return null;
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return super.queryForMap(sql, args, argTypes);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
        return super.queryForMap(sql, args);
    }

    @Override
    public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) throws DataAccessException {
        return super.queryForList(sql, args, argTypes, elementType);
    }

    @Override
    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
        return super.queryForList(sql, args, elementType);
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
        return super.queryForList(sql, elementType, args);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return super.queryForList(sql, args, argTypes);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
        return super.queryForList(sql, args);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return super.queryForRowSet(sql, args, argTypes);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
        return super.queryForRowSet(sql, args);
    }


    public int getPageOffset(Page page) {
        return (page.getPageNum()-1)/page.getPageSize();
    }

    public int getPages(Page page,int count) {
        return ((int) (count/page.getPageSize())) + ((count%page.getPageSize()>0) ? 1:0);
    }
}
