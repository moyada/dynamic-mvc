package other.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xueyikang
 * @since 1.0
 **/
@RestController
public class TestController {

    public String test() {
        return "ok";
    }

    public int cal(@RequestParam("a") Integer a, @RequestParam("b") Integer b) {
        return a * b;
    }

    public String getPath(@PathVariable("path") String path, String ver) {
        return path + ver;
    }
}
