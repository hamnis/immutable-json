package net.hamnaberg.json.pointer;


import io.vavr.collection.List;

import java.util.ArrayList;

class JsonPointerParser {
    List<Ref> parse(String s) {
        List<String> list = clean((s.startsWith("/") ? s.substring(1) : s).split("/"));
        return parse(list);
    }

    List<Ref> parse(List<String> parts) {
        ArrayList<Ref> path = new ArrayList<>(parts.size());
        for (String p : parts) {
            if (p.equals("-")) {
                path.add(EndOfArray.INSTANCE);
            }
            else if (ArrayRef.pattern.matcher(p).matches()) {
                path.add(new ArrayRef(Integer.parseInt(p)));
            }
            else {
                path.add(new PropertyRef(p));
            }
        }
        return List.ofAll(path);
    }

    private List<String> clean(String[] split) {
        return List.of(split).map(this::unescape);
    }

    private String unescape(String str) {
        return str.replace("~1", "/").replace("~0", "~");
    }
}
