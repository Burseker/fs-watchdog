package com.burseker.hiphub.fswatchdog.common.nonproject.collections;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class IterableTest {

    @Test
    void iteratorTest(){
        Iterable<String> iterable = Collections.singletonList("One");

        //iterable.iterator() returns iterator to first element
        assertTrue(iterable.iterator().hasNext());
        assertEquals(iterable.iterator().next(), "One");
        assertTrue(iterable.iterator().hasNext());
        assertEquals(iterable.iterator().next(), "One");

        Iterator<String> it = iterable.iterator();
        assertTrue(it.hasNext());
        assertEquals(it.next(), "One");
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
}
