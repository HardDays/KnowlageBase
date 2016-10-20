package ru.knowledgebase.analyticsmodule;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by vova on 20.10.16.
 */
public class RequestParserTest {
    @Test
    public void test1() throws Exception{
        List<String> res = RequestParser.getInstance().getRequestKeywords("купить банк карта card credit master");
        assertTrue(Collections.frequency(res, "купить") == 1);
        assertTrue(Collections.frequency(res, "банк") == 1);
        assertTrue(Collections.frequency(res, "карта") == 1);
        assertTrue(Collections.frequency(res, "card") == 1);
        assertTrue(Collections.frequency(res, "credit") == 1);
        assertTrue(Collections.frequency(res, "master") == 1);
    }

    @Test
    public void test2() throws Exception{
        List<String> res = RequestParser.getInstance().getRequestKeywords("ваыавыыва банк");
        assertTrue(Collections.frequency(res, "ваыавыыва") == 0);
        assertTrue(Collections.frequency(res, "банк") == 1);
    }
}
