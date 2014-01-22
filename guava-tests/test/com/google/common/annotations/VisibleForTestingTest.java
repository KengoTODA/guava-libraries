package com.google.common.annotations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import static com.google.common.annotations.VisibleForTesting.Visibility.*;

public class VisibleForTestingTest extends TestCase {
    public void testPrivate() throws SecurityException, NoSuchMethodException {
        assertTrue(PRIVATE.allowsAccess(Object.class, Object.class));

        Method closeMethod = Writer.class.getMethod("close");
        assertNotNull(closeMethod);
        assertTrue(PRIVATE.allowsAccess(VisibleForTestingTest.class, ExtendedWriter.class));
        assertTrue(PRIVATE.allowsAccess(ExtendedWriter.class, VisibleForTestingTest.class));
        assertTrue(PRIVATE.allowsAccess(Writer.class, closeMethod));
        assertFalse(PRIVATE.allowsAccess(Reader.class, closeMethod));
        assertFalse(PRIVATE.allowsAccess(Object.class, closeMethod));
        assertFalse(PRIVATE.allowsAccess(ExtendedWriter.class, closeMethod));

        Method extendedCloseMethod = ExtendedWriter.class.getMethod("close");
        assertNotNull(extendedCloseMethod);
        assertTrue(PRIVATE.allowsAccess(ExtendedWriter.class, extendedCloseMethod));
        assertFalse(PRIVATE.allowsAccess(Writer.class, extendedCloseMethod));
        assertFalse(PRIVATE.allowsAccess(Object.class, extendedCloseMethod));
        assertFalse(PRIVATE.allowsAccess(Reader.class, extendedCloseMethod));
    }

    public void testPackage() throws SecurityException, NoSuchMethodException {
        assertTrue(PACKAGE.allowsAccess(Object.class, Object.class));

        Method closeMethod = Writer.class.getMethod("close");
        assertNotNull(closeMethod);
        assertTrue(PACKAGE.allowsAccess(VisibleForTestingTest.class, ExtendedWriter.class));
        assertTrue(PACKAGE.allowsAccess(ExtendedWriter.class, VisibleForTestingTest.class));
        assertTrue(PACKAGE.allowsAccess(Writer.class, closeMethod));
        assertTrue(PACKAGE.allowsAccess(Reader.class, closeMethod));
        assertFalse(PACKAGE.allowsAccess(Object.class, closeMethod));
        assertFalse(PACKAGE.allowsAccess(ExtendedWriter.class, closeMethod));

        Method extendedCloseMethod = ExtendedWriter.class.getMethod("close");
        assertNotNull(extendedCloseMethod);
        assertTrue(PACKAGE.allowsAccess(ExtendedWriter.class, extendedCloseMethod));
        assertFalse(PACKAGE.allowsAccess(Writer.class, extendedCloseMethod));
        assertFalse(PACKAGE.allowsAccess(Object.class, extendedCloseMethod));
        assertFalse(PACKAGE.allowsAccess(Reader.class, extendedCloseMethod));
    }

    public void testProtected() throws SecurityException, NoSuchMethodException {
        assertTrue(PROTECTED.allowsAccess(Object.class, Object.class));

        Method closeMethod = Writer.class.getMethod("close");
        assertNotNull(closeMethod);
        assertTrue(PROTECTED.allowsAccess(VisibleForTestingTest.class, ExtendedWriter.class));
        assertTrue(PROTECTED.allowsAccess(ExtendedWriter.class, VisibleForTestingTest.class));
        assertTrue(PROTECTED.allowsAccess(Writer.class, closeMethod));
        assertTrue(PROTECTED.allowsAccess(Reader.class, closeMethod));
        assertFalse(PROTECTED.allowsAccess(Object.class, closeMethod));
        assertTrue(PROTECTED.allowsAccess(ExtendedWriter.class, closeMethod));

        Method extendedCloseMethod = ExtendedWriter.class.getMethod("close");
        assertNotNull(extendedCloseMethod);
        assertTrue(PROTECTED.allowsAccess(ExtendedWriter.class, extendedCloseMethod));
        assertFalse(PROTECTED.allowsAccess(Writer.class, extendedCloseMethod));
        assertFalse(PROTECTED.allowsAccess(Object.class, extendedCloseMethod));
        assertFalse(PROTECTED.allowsAccess(Reader.class, extendedCloseMethod));
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
