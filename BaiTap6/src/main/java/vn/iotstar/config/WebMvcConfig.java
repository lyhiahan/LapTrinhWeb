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
        File uploadFolder = new File("upload").getAbsoluteFile();
        String uploadBasePath = uploadFolder.getAbsolutePath().replace("\\", "/");
        if (!uploadBasePath.endsWith("/")) {
            uploadBasePath += "/";
        }

        registry.addResourceHandler("/upload/**")
                .addResourceLocations(
                        "file:" + uploadBasePath,
                        "file:" + uploadBasePath + "category/",
                        "file:" + uploadBasePath + "product/",
                        "file:upload/",
                        "file:upload/category/",
                        "file:upload/product/",
                        "classpath:/static/upload/",
                        "classpath:/static/upload/category/",
                        "classpath:/static/upload/product/"
                );

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
