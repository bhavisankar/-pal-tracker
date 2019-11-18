package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private String port;
    private String mem_limit;
    private String cf_instance_index;
    private String  cf_instance_addr;

    public EnvController(@Value("${port:NOT SET}") String port,
                         @Value("${memory.limit:NOT SET}") String mem_limit,
                         @Value("${cf.instance.index:NOT SET}") String cfInstanceIndex,
                         @Value("${cf.instance.addr:NOT SET}") String Cf_instance_addr) {
        this.port=port;
        this.mem_limit= mem_limit;
        this.cf_instance_index = cfInstanceIndex;
        this.cf_instance_addr =Cf_instance_addr;

    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        Map<String, String> env = new HashMap<>();

        env.put("PORT", port);
        env.put("MEMORY_LIMIT", mem_limit);
        env.put("CF_INSTANCE_INDEX", cf_instance_index);
        env.put("CF_INSTANCE_ADDR", cf_instance_addr);

        return env;
    }

}
