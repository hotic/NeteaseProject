import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.asgard.neteaseproject.bean.result.Result;
import me.asgard.neteaseproject.domain.Order;

import java.io.IOException;
import java.util.List;

public class NeteaseProjectTest {
    public static void main(String[] args) throws IOException {
        Result<List<Order>> result = new ObjectMapper().readValue("{}", new TypeReference<Result<List<Order>>>() {
        });
        System.out.println(result);
    }
}
