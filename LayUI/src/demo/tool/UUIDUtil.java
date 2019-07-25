package demo.tool;

import java.util.UUID;
/**
 * UUID生成-工具类
 * @author BLAME
 *
 */
public class UUIDUtil {
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
