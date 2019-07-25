package demo.tool;

import java.util.HashMap;
import java.util.List;

public class Layuis  extends HashMap<String, Object> {

    public static Layuis data(Integer count,List<?> data){
        Layuis r = new Layuis();
        r.put("code", 0);
        r.put("msg", "");
        r.put("count", count);
        r.put("data", data);
        return r;
    }
}
