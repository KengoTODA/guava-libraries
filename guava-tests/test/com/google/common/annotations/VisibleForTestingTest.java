package com.google.common.annotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.google.common.annotations.VisibleForTesting.Visibility;

public class VisibleForTestingTest extends TestCase {
    public void testPrivate() throws SecurityException, NoSuchMethodException {
        Visibility visibility = Visibility.PRIVATE;

        assertTrue(visibility.allowsAccess(Object.class, Object.class));

        Method closeMethod = Writer.class.getMethod("close");
        assertThat(closeMethod, is(notNullValue()));
        assertTrue(visibility.allowsAccess(VisibleForTestingTest.class, ExtendedWriter.class));
        assertTrue(visibility.allowsAccess(ExtendedWriter.class, VisibleForTestingTest.class));
        assertTrue(visibility.allowsAccess(Writer.class, closeMethod));
        assertFalse(visibility.allowsAccess(Reader.class, closeMethod));
        assertFalse(visibility.allowsAccess(Object.class, closeMethod));
    }

    public void testPackage() throws SecurityException, NoSuchMethodException {
        Visibility visibility = Visibility.PACKAGE;

        assertTrue(visibility.allowsAccess(Object.class, Object.class));

        Method closeMethod = Writer.class.getMethod("close");
        assertThat(closeMethod, is(notNullValue()));
        assertTrue(visibility.allowsAccess(Writer.class, closeMethod));
        assertTrue(visibility.allowsAccess(Reader.class, closeMethod));
        assertFalse(visibility.allowsAccess(Object.class, closeMethod));
    }

    public void testProtected() throws SecurityException, NoSuchMethodException {
        Visibility visibility = Visibility.PROTECTED;

        assertTrue(visibility.allowsAccess(Object.class, Object.class));

        Method closeMethod = Writer.class.getMethod("close");
        Method extendedCloseMethod = ExtendedWriter.class.getMethod("close");
        assertThat(extendedCloseMethod, is(notNullValue()));
        assertFalse(visibility.allowsAccess(Writer.class, extendedCloseMethod));
        assertFalse(visibility.allowsAccess(Reader.class, extendedCloseMethod));
        assertTrue(visibility.allowsAccess(ExtendedWriter.class, extendedCloseMethod));
        assertTrue(visibility.allowsAccess(ExtendedWriter.class, closeMethod));
        assertFalse(visibility.allowsAccess(Object.class, extendedCloseMethod));
    }

    private static final class ExtendedWriter extends BufferedWriter {
        private ExtendedWriter(Writer writer) {
            super(writer);
        }

        @Override
        public void close() throws IOException {
            super.close();
        }
    }
}
