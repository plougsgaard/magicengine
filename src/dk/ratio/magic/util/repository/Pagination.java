package dk.ratio.magic.util.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;

public class Pagination<T>
{
    public Page<T> fetchPage(Integer pageNumber, SimpleJdbcTemplate template,
                             String selectClause, String fromWhereClause,
                             MapSqlParameterSource arguments, RowMapper<T> rowMapper)
    {
        return fetchPage(pageNumber, template, selectClause, fromWhereClause,
                         arguments, rowMapper, 10);
    }

    public Page<T> fetchPage(Integer pageNumber, SimpleJdbcTemplate template,
                             String selectClause, String fromWhereClause,
                             MapSqlParameterSource arguments, RowMapper<T> rowMapper,
                             int pageSize)
    {
        Integer resultCount = template.queryForInt("SELECT COUNT(*) " + fromWhereClause, arguments);
        Page<T> page = new Page<T>(pageSize, pageNumber, resultCount);

        Integer offset = (pageNumber - 1) * page.getPageSize();
        Integer limit = page.getPageSize();

        List<T> items = template.query(
                selectClause + " " + fromWhereClause + " " +
                "LIMIT :limit OFFSET :offset", rowMapper,
                arguments.addValue("limit", limit).addValue("offset", offset)
        );
        page.setItems(items);
        return page;
    }
}

