package vn.hcmute.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshConfig_24133016 {

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                builder.setDecoratorPrefix("/WEB-INF/decorators/");

                builder.addDecoratorPath("/admin/*", "admin-layout.jsp");

                // Decorator cho các trang còn lại (Client/User)
                builder.addDecoratorPath("/*", "main-layout.jsp");

                // Loại trừ các đường dẫn xác thực và tĩnh
                builder.addExcludedPath("/login*")
                       .addExcludedPath("/register*")
                       .addExcludedPath("/verify-otp*")
                       .addExcludedPath("/image*")
                       .addExcludedPath("/upload/**")
                       .addExcludedPath("/views/decorator/*")
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
