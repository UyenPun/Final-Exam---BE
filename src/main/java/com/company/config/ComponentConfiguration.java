package com.company.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ComponentConfiguration {

  // ModelMapper
  @Bean
  public ModelMapper initModelMapper() {
    return new ModelMapper();
  }

  // Swagger
  /**
   * UI: http://localhost:8080/swagger-ui/index.html
   */
  @Bean
  public OpenAPI myOpenAPI() {
    Contact contact = new Contact()
        .email("info@duyngocit.com.vn")
        .name("DuyNgocIT")
        .url("https://www.facebook.com/duyngocit");

    License license = new License()
        .name("Apache 2.0")
        .url("http://www.apache.org/licenses/LICENSE-2.0.html");

    Info info = new Info()
        .title("Demo Application API")
        .version("1.0")
        .contact(contact)
        .description("This is API description for Application")
        .termsOfService("Terms of service URL")
        .license(license);

    return new OpenAPI().info(info);
  }

  // Index start 1
  @Bean
  public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
    return pageableResolver -> {
      pageableResolver.setOneIndexedParameters(true);
    };
  }

  // Origin
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");
      }
    };
  }

}
