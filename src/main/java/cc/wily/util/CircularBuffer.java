package cc.wily.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class CircularBuffer<E> extends AbstractList<E> {
	private Object buffer[];
	private int head = 0;
	private int count = 0;

	/**
	 * <p>
	 * Create a circular buffer with <code>count</code> capacity. If an element
	 * is added to the buffer when its {@link #size()} ==
	 * <code>count<code>, the oldest entry is overwritten.
	 * </p>
	 * 
	 * <p>
	 * Entries may only be added to the end of the buffer, using the
	 * {@link #add(Object)} method.
	 * </p>
	 * 
	 * <p>
	 * This class is multi-thread safe.
	 * </p>
	 * 
	 * @param count
	 *            The capacity of the buffer. The buffer does not support
	 *            resizing.
	 */
	public CircularBuffer(int count) {
		if (count <= 0) {
			throw new IllegalArgumentException("Positive capacity is required");
		}

		buffer = new Object[count];
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized E get(int index) {
		if (index >= 0 && index < count) {
			return (E) buffer[(head + index) % buffer.length];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public synchronized int size() {
		return count;
	}

	@Override
	public synchronized boolean add(E e) {
		int ix = (head + count) % buffer.length;
		buffer[ix] = e;
		if (count < buffer.length) {
			count++;
		} else {
			head++;
		}
		return true;
	}

	@Override
	public synchronized void clear() {
		for (int i = head; i < head + count; i++) {
			buffer[i % buffer.length] = null;
		}
		head = 0;
		count = 0;
	}
	
	public synchronized List<E> snapshot() {
		return new ArrayList<E>(this);
	}

}
