package io.moyada.dynamicmvc.controller;

import io.moyada.dynamicmvc.handler.HandlerMapperRegister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.pattern.PathPattern;
import other.controller.TestController;

import java.lang.reflect.Method;

/**
 * @author xueyikang
 * @since 1.0
 **/
@RestController
public class AppController {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    private HandlerMapperRegister handlerMapperRegister;

    @GetMapping("test")
    public String test() {
        return "OK";
    }

    private TestController getTestController() {
        TestController controller;
        try {
            controller = beanFactory.getBean(TestController.class);
        } catch (BeansException e) {
            controller = new TestController();
            beanFactory.registerSingleton("TestController", controller);
        }
        return controller;
    }

    @GetMapping("add")
    public String add(@RequestParam("url") String url) throws NoSuchMethodException {
        TestController controller = getTestController();
        Method method = TestController.class.getDeclaredMethod("test");
        handlerMapperRegister.addMapper(url, controller, method, RequestMethod.GET, MediaType.APPLICATION_JSON);
        return "success";
    }

    @GetMapping("addParam")
    public String addParam(@RequestParam("url") String url) throws NoSuchMethodException {
        TestController controller = getTestController();
        Method method = TestController.class.getDeclaredMethod("cal", Integer.class, Integer.class);
        handlerMapperRegister.addMapper(url, controller, method, RequestMethod.GET, MediaType.APPLICATION_JSON);
        return "success";
    }

    @GetMapping("addPath")
    public String addPath(@RequestParam("url") String url) throws NoSuchMethodException {
        TestController controller = getTestController();
        Method method = TestController.class.getDeclaredMethod("getPath", String.class, String.class);
        handlerMapperRegister.addMapper(url, controller, method, RequestMethod.GET, MediaType.APPLICATION_JSON);
        return "success";
    }
}
