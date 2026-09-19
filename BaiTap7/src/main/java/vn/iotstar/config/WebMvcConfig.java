package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = storageProperties.getLocation();
        if (location == null || location.isEmpty()) {
            location = "uploads";
        }
        Path uploadDir = Paths.get(location).toAbsolutePath().normalize();
        String uploadPath = uploadDir.toUri().toString();

        if (!uploadPath.endsWith("/")) {
            uploadPath = uploadPath + "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);

        registry.addResourceHandler("/admin/categories/images/**")
                .addResourceLocations(uploadPath);

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
