package vn.iotstar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.iotstar.util.Constant;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = new File(Constant.DIR).getAbsolutePath().replace("\\", "/");
        if (!uploadDir.endsWith("/")) {
            uploadDir += "/";
        }
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadDir, "file:upload/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "/views/");
    }
}
