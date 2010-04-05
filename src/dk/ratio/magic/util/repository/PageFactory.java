package dk.ratio.magic.util.repository;

public class PageFactory<T>
{
    public Page<T> createNormalPage(Integer pageNumber, Integer resultCount)
    {
        return new Page<T>(10, pageNumber, resultCount);
    }
}
