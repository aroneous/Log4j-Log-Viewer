package cc.wily.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class CircularBufferTests {
	private CircularBuffer<Integer> buf10;
	private CircularBuffer<Integer> bufOvflw;

	@Before
	public void init() {
		buf10 = new CircularBuffer<Integer>(10);

		buf10.add(1);
		buf10.add(2);
		buf10.add(3);

		bufOvflw = new CircularBuffer<Integer>(5);
		bufOvflw.add(1);
		bufOvflw.add(2);
		bufOvflw.add(3);
		bufOvflw.add(4);
		bufOvflw.add(5);
		bufOvflw.add(6);
		bufOvflw.add(7);
	}

	@Test
	public void testGet() {
		assertEquals(2L, (long) buf10.get(1));
		assertEquals(1L, (long) buf10.get(0));
		assertEquals(3L, (long) buf10.get(2));
	}

	@Test
	public void testIteration() {
		Iterator<Integer> it = buf10.iterator();

		assertTrue(it.hasNext());
		assertEquals(1L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(2L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(3L, (long) it.next());

		assertFalse(it.hasNext());

		try {
			it.next();
			fail("Iterator didn't throw exception at end");
		} catch (NoSuchElementException e) {
			// ...
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testInsertFail() {
		buf10.add(1, 666);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetFail() {
		buf10.set(1, 666);
	}

	@Test
	public void testOverwrite() {
		assertEquals(3L, (long) bufOvflw.get(0));
		assertEquals(4L, (long) bufOvflw.get(1));
		assertEquals(5L, (long) bufOvflw.get(2));
		assertEquals(6L, (long) bufOvflw.get(3));
		assertEquals(7L, (long) bufOvflw.get(4));
	}

	@Test
	public void testOverwriteIteration() {
		Iterator<Integer> it = bufOvflw.iterator();

		assertTrue(it.hasNext());
		assertEquals(3L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(4L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(5L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(6L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(7L, (long) it.next());

		assertFalse(it.hasNext());

		try {
			it.next();
			fail("Iterator didn't throw exception at end");
		} catch (NoSuchElementException e) {
			// ...
		}
	}

	@Test
	public void testOverwriteListIteration() {
		ListIterator<Integer> it = bufOvflw.listIterator(2);

		assertTrue(it.hasNext());
		assertEquals(5L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(6L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(7L, (long) it.next());

		assertFalse(it.hasNext());

		try {
			it.next();
			fail("Iterator didn't throw exception at end");
		} catch (NoSuchElementException e) {
			// ...
		}
	}

	@Test
	public void testOverwriteListIterationBackwards() {
		ListIterator<Integer> it = bufOvflw.listIterator(2);

		assertTrue(it.hasPrevious());
		assertEquals(4L, (long) it.previous());

		assertTrue(it.hasPrevious());
		assertEquals(3L, (long) it.previous());

		assertFalse(it.hasPrevious());

		try {
			it.previous();
			fail("Iterator didn't throw exception at beginning");
		} catch (NoSuchElementException e) {
			// ...
		}
	}
	
	@Test
	public void testOverwriteListIterationForwardBack() {
		ListIterator<Integer> it = bufOvflw.listIterator(2);

		assertTrue(it.hasNext());
		assertEquals(5L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(6L, (long) it.next());

		assertTrue(it.hasNext());
		assertEquals(7L, (long) it.next());

		assertFalse(it.hasNext());

		try {
			it.next();
			fail("Iterator didn't throw exception at end");
		} catch (NoSuchElementException e) {
			// ...
		}
		
		assertTrue(it.hasPrevious());
		assertEquals(7L, (long) it.previous());
		
		assertTrue(it.hasPrevious());
		assertEquals(6L, (long) it.previous());

		assertTrue(it.hasPrevious());
		assertEquals(5L, (long) it.previous());

		assertTrue(it.hasPrevious());
		assertEquals(4L, (long) it.previous());

		assertTrue(it.hasPrevious());
		assertEquals(3L, (long) it.previous());

		assertFalse(it.hasPrevious());

		try {
			it.previous();
			fail("Iterator didn't throw exception at beginning");
		} catch (NoSuchElementException e) {
			// ...
		}
	}
}
