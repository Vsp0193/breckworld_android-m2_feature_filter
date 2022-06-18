package com.breckworld.view.omnivirt;

import android.net.Uri;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class MailTo {
    public static final String MAILTO_SCHEME = "mailto:";
    private HashMap<String, String> mHeaders = new HashMap();
    private static final String TO = "to";
    private static final String BODY = "body";
    private static final String CC = "cc";
    private static final String BCC = "bcc";
    private static final String SUBJECT = "subject";
    private static final String[] VALID_HEADERS = new String[]{"to", "body", "cc", "bcc", "subject"};

    public static boolean isMailTo(String url) {
        return url != null && url.startsWith("mailto:");
    }

    public static MailTo parse(String url) throws ParseException {
        if (url == null) {
            throw new NullPointerException();
        } else if (!isMailTo(url)) {
            throw new ParseException("Not a mailto scheme", 0);
        } else {
            String noScheme = "foo:///" + url.substring("mailto:".length());
            Uri email = Uri.parse(noScheme);
            MailTo m = new MailTo();
            String previousHeader = null;
            String query = email.getEncodedQuery();
            if (query != null) {
                List<String> validHeaderList = Arrays.asList(VALID_HEADERS);
                String[] queries = query.split("&");
                String[] var8 = queries;
                int var9 = queries.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    String q = var8[var10];
                    String[] nameval = q.split("=");
                    if (nameval.length == 0 && previousHeader != null) {
                        appendValue(m, previousHeader, "&" + q);
                    } else {
                        String header = Uri.decode(nameval[0].toLowerCase());
                        String value = nameval.length > 1 ? Uri.decode(nameval[1]) : null;
                        if (validHeaderList.contains(header)) {
                            m.mHeaders.put(header, value);
                            previousHeader = header;
                        } else if (previousHeader != null) {
                            appendValue(m, previousHeader, "&" + q);
                        }
                    }
                }
            }

            String address = email.getPath();
            if (address != null) {
                if (address.startsWith("/")) {
                    address = address.substring(1);
                }

                String addr = m.getTo();
                if (addr != null) {
                    address = address + ", " + addr;
                }

                m.mHeaders.put("to", address);
            }

            return m;
        }
    }

    private static void appendValue(MailTo m, String header, String newValue) {
        String oldValue = (String)m.mHeaders.get(header);
        if (oldValue == null) {
            m.mHeaders.put(header, newValue);
        } else {
            m.mHeaders.put(header, oldValue + newValue);
        }

    }

    public String getTo() {
        return (String)this.mHeaders.get("to");
    }

    public String getCc() {
        return (String)this.mHeaders.get("cc");
    }

    public String getBcc() {
        return (String)this.mHeaders.get("bcc");
    }

    public String getSubject() {
        return (String)this.mHeaders.get("subject");
    }

    public String getBody() {
        return (String)this.mHeaders.get("body");
    }

    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("mailto:");
        sb.append('?');
        Iterator var2 = this.mHeaders.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<String, String> header = (Entry)var2.next();
            sb.append(Uri.encode((String)header.getKey()));
            sb.append('=');
            sb.append(Uri.encode((String)header.getValue()));
            sb.append('&');
        }

        return sb.toString();
    }

    private MailTo() {
    }
}
