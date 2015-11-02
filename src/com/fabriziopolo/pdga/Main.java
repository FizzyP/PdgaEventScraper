package com.fabriziopolo.pdga;

import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.ArrayList;

public class Main {

    final static String PDGA_EVENTS_URL = "http://www.pdga.com/tour/events";

    final static String MATCH_WHITESPACE = "\\s*";

    final static String MATCH_OFFICIAL_NAME_TD_START = "<td class=\"views-field views-field-OfficialName\">" + MATCH_WHITESPACE;
    final static String MATCH_LINK_TEXT = "<a [^>]*>([^<]*)</a>" + MATCH_WHITESPACE;
    final static String MATCH_OFFICIAL_NAME_TD_END = "</td>";

    final static String eventMatcherString = MATCH_OFFICIAL_NAME_TD_START + MATCH_LINK_TEXT; // + MATCH_OFFICIAL_NAME_TD_END;
//    final static String eventMatcherString = MATCH_LINK_TEXT ; // + MATCH_OFFICIAL_NAME_TD_END;

    public static void main(String[] args) {

        String pageSource;
        try {
            URL url = new URL(PDGA_EVENTS_URL);
            pageSource = getTextAtUrl(url);
        }
        catch (Exception e) {
            System.exit(1);
            return;
        }

//        System.out.println(page);

        Pattern eventPattern = Pattern.compile(eventMatcherString);
        List<PdgaEventInfo> matches = getAllMatches(eventPattern, pageSource);

        for (PdgaEventInfo info : matches) {
            System.out.println(info.name);
        }
    }

    static List<PdgaEventInfo> getAllMatches(Pattern pattern, String source)
    {
        Matcher m = pattern.matcher(source);
        List<PdgaEventInfo> results = new ArrayList<>();
        while (m.find()) {
            PdgaEventInfo info = new PdgaEventInfo();
            info.name = m.group(1);
            results.add(info);
        }
        return results;
    }

    static String getTextAtUrl(URL url) throws IOException
    {
        InputStream is = url.openStream();
        int ptr = 0;
        StringBuffer buffer = new StringBuffer();
        while ((ptr = is.read()) != -1) {
            buffer.append((char)ptr);
        }
        return buffer.toString();
    }

}
