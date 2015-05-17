package at.mse.walchhofer.utilities.caching;

public interface ITimeCacheElement<K> {

    public K getKey();

    public boolean isValid();

    public void setValid(boolean valid);

}
