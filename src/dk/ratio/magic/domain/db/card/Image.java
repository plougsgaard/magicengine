package dk.ratio.magic.domain.db.card;

public class Image
{
    private byte[] data;

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    public String toString()
    {
        return "Image{" +
               "data=" + (data == null ? null : "Has Data (not null)") +
               '}';
    }
}
