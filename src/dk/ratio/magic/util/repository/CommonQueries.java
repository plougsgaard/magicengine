package dk.ratio.magic.util.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

class CommonQueries
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private SimpleJdbcTemplate simpleJdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Integer getResultCount(String fromWhereClause)
    {
        List<Integer> result = simpleJdbcTemplate.query(
                "SELECT COUNT(*) " + fromWhereClause,
                new RowMapper<Integer>()
                {
                    public Integer mapRow(ResultSet rs, int i) throws SQLException
                    {
                        return rs.getInt(1);
                    }
                });
        if (result.size() != 1) {
            logger.error("Count query failed to complete normally.");
            return -1;
        }
        return result.get(0);
    }
}
