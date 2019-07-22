package org.ohdsi.dataqualitydashboard.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String getResourceAsString(String resourcePath) {

        try (InputStream is = Utils.class.getResourceAsStream(resourcePath)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
