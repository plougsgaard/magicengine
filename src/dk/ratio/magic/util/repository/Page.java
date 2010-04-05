package dk.ratio.magic.util.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class Page<T>
{
    protected final Log logger = LogFactory.getLog(getClass());

    private Integer pageNumber;
    private Integer pageCount;
    private Integer pageSize;
    private List<T> items;

    public Page(Integer pageSize, Integer pageNumber, Integer resultCount)
    {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.items = new ArrayList<T>(pageSize);
        this.pageCount = (int) Math.ceil((double) resultCount / (double) pageSize);
    }

    public Integer getPageNumber()
    {
        return pageNumber;
    }

    public Integer getPageCount()
    {
        return pageCount;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }
    public List<T> getItems()
    {
        return items;
    }

    public void setItems(List<T> items)
    {
        this.items = items;
    }
}
