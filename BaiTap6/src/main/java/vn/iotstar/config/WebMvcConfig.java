package vn.iotstar.config;

import java.io.File;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.iotstar.util.Constant;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = new File(Constant.UPLOAD_DIRECTORY).getAbsolutePath().replace("\\", "/");
        if (!uploadDir.endsWith("/")) {
            uploadDir += "/";
        }

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadDir, "file:upload/category/", "file:upload/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
