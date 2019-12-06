package org.apache.manifoldcf.csws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * This Adapter is meant to intercept XML response and replace invalid characters
 */
public class InvalidCharInterceptor extends AbstractPhaseInterceptor<Message> {

    // https://stackoverflow.com/a/4237934/2143734

    // Valid Characters in XML 1.0
    // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
    // now we create a regexp filtering the opposite, i.e. all invalid char intervals
    private static final Pattern XML_1_0_FORBIDDEN_CHARS_PATTERN = Pattern
            .compile("[^" + "\u0009\r\n" + "\u0020-\uD7FF" + "\uE000-\uFFFD" + "\ud800\udc00-\udbff\udfff" + "]");

    private String replacement;

    InvalidCharInterceptor(String replacement) {
        super(Phase.RECEIVE);
        this.replacement = replacement;
    }

    /**
     * Remove all XML 1.0 invalid characters from the String
     *
     * @param s           the string to process
     * @param replacement the replacement string
     * @return the replaced string
     */
    private static String replaceForbiddenChars(String s, String replacement) {
        Matcher m = XML_1_0_FORBIDDEN_CHARS_PATTERN.matcher(s);
        return m.replaceAll(replacement);
    }

    @Override
    public void handleMessage(Message message) {
        try (InputStream is = message.getContent(InputStream.class)) {
            if (is != null) {
                String content = IOUtils.toString(is, StandardCharsets.UTF_8);
                String cleanContent = replaceForbiddenChars(content, replacement);
                message.setContent(InputStream.class,
                        new ByteArrayInputStream(cleanContent.getBytes(StandardCharsets.UTF_8)));
            }
        } catch (IOException e) {
            throw new Fault(e);
        }
    }
}
