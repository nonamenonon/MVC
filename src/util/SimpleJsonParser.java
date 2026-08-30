package util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleJsonParser {
    private final String s;
    private int pos;

    private SimpleJsonParser(String s) {
        this.s = s;
        this.pos = 0;
    }

    public static Object parse(String json) {
        SimpleJsonParser parser = new SimpleJsonParser(json);
        parser.skipWhitespace();
        return parser.parseValue();
    }

    private void skipWhitespace() {
        while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
            pos++;
        }
    }

    private Object parseValue() {
        skipWhitespace();
        char c = s.charAt(pos);
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return parseString();
        if (c == 't' || c == 'f') return parseBoolean();
        if (c == 'n') {
            pos += 4;
            return null;
        }
        return parseNumber();
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        pos++;
        skipWhitespace();
        if (s.charAt(pos) == '}') {
            pos++;
            return map;
        }
        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            pos++;
            Object value = parseValue();
            map.put(key, value);
            skipWhitespace();
            char c = s.charAt(pos);
            pos++;
            if (c == '}') break;
        }
        return map;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        pos++;
        skipWhitespace();
        if (s.charAt(pos) == ']') {
            pos++;
            return list;
        }
        while (true) {
            Object value = parseValue();
            list.add(value);
            skipWhitespace();
            char c = s.charAt(pos);
            pos++;
            if (c == ']') break;
        }
        return list;
    }

    private String parseString() {
        pos++;
        StringBuilder sb = new StringBuilder();
        while (s.charAt(pos) != '"') {
            char c = s.charAt(pos);
            if (c == '\\') {
                pos++;
                char esc = s.charAt(pos);
                switch (esc) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'u':
                        String hex = s.substring(pos + 1, pos + 5);
                        sb.append((char) Integer.parseInt(hex, 16));
                        pos += 4;
                        break;
                    default: sb.append(esc);
                }
            } else {
                sb.append(c);
            }
            pos++;
        }
        pos++;
        return sb.toString();
    }

    private Boolean parseBoolean() {
        if (s.startsWith("true", pos)) {
            pos += 4;
            return Boolean.TRUE;
        }
        pos += 5;
        return Boolean.FALSE;
    }

    private Double parseNumber() {
        int start = pos;
        while (pos < s.length()) {
            char c = s.charAt(pos);
            if (Character.isDigit(c) || c == '-' || c == '+' || c == '.' || c == 'e' || c == 'E') {
                pos++;
            } else {
                break;
            }
        }
        return Double.parseDouble(s.substring(start, pos));
    }
}
