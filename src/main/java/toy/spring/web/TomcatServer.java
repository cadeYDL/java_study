package toy.spring.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.bridge.SLF4JBridgeHandler;
import toy.spring.Autowired;
import toy.spring.BeanPostProcessor;
import toy.spring.Component;
import toy.spring.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

// 用户自定义路由和执行逻辑的映射关系
// 将的映射关系注册到dispatchHandler中
// handler负责处理
// 启动tomcat

@Component
public class TomcatServer {

    @Autowired
    private DispatchServlet dispatchServlet;



    @PostConstruct
    public void start() throws Exception {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final int port = 8080;
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        tomcat.addServlet(contextPath,"dispatchServer",dispatchServlet);

        context.addServletMappingDecoded("/*","dispatchServer");
        tomcat.start();
        System.out.println("Embedded Tomcat started port:"+port);
    }
}
