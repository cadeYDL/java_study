package design_pattern.facade;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import facede.plugin.MyPlugin;

@RestController
public class TimeController {

    private MyPlugin myPlugin;

    private final DateTimeFormatter dataTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/time")
    public String getTime(){
        if (myPlugin!=null){
            myPlugin.beforeGetTime();
        }
        return LocalDateTime.now().format(dataTimeFormatter);
    }

    @GetMapping("/load/plugin/{path}")
    public String loadPlugin(@PathVariable("path") String path){
        File file = new File(path);
        try(URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toPath().toUri().toURL()});
            InputStream pluginPkgFullName = urlClassLoader.getResourceAsStream("fullname.plugin")){
            String fullName = new String(pluginPkgFullName.readAllBytes());
            Class<?> aClass = urlClassLoader.loadClass(fullName);
            myPlugin = (MyPlugin) aClass.getConstructor().newInstance();
            return "ok:"+aClass.getName();
        }catch (Exception e){
            return "error";
        }

    }
}
