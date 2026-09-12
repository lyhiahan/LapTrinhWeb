package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshConfig {

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                // Decorator cho trang quản trị Admin
                builder.addDecoratorPath("/admin/*", "/views/decorator/admin-layout.jsp");

                // Decorator cho các trang còn lại (Client)
                builder.addDecoratorPath("/*", "/views/decorator/main-layout.jsp");

                // Loại trừ các đường dẫn xác thực và tĩnh
                builder.addExcludedPath("/login*")
                       .addExcludedPath("/register*")
                       .addExcludedPath("/forgot-password*")
                       .addExcludedPath("/reset-password*")
                       .addExcludedPath("/verify-otp*")
                       .addExcludedPath("/image*")
                       .addExcludedPath("/upload/**")
                       .addExcludedPath("/views/decorator/*")
                       .addExcludedPath("/views/error.jsp")
                       .addExcludedPath("/views/style.css")
                       .addExcludedPath("/static/**")
                       .addExcludedPath("/assets/**");
            }
        });
        filter.addUrlPatterns("/*");
        filter.setOrder(1);
        return filter;
    }
}
