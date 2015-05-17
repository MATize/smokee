package at.mse.walchhofer.utilities.caching;

public class TimeFramedElement<E> {

    private Long timestamp;

    private E element;

    /**
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the element
     */
    public E getElement() {
        return element;
    }

    /**
     * @param element
     *            the element to set
     */
    public void setElement(E element) {
        this.element = element;
    }

}
