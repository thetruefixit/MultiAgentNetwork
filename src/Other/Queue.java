package Other;

import java.util.Collection;

/**
 * Created by Fixit on 30.05.2014.
 */
public interface Queue<E> extends Collection<E> {
    E element();
    boolean offer(E e);
    E add();
    E peek();
    E poll();
    E remove();
}
