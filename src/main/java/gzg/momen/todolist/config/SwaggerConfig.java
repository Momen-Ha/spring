//package gzg.momen.todolist.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import org.springdoc.core.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("ToDo List API")
//                        .version("1.0.0")
//                        .description("API documentation for the ToDo List application")
//                        .contact(new Contact()
//                                .name("Your Name")
//                                .email("your-email@example.com")
//                                .url("https://your-website.com"))
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
//    }
//
//    @Bean
//    public GroupedOpenApi todosApi() {
//        return GroupedOpenApi.builder()
//                .group("todos")
//                .pathsToMatch("/api/v1/todos/**") // Matches endpoints for todos
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi usersApi() {
//        return GroupedOpenApi.builder()
//                .group("users")
//                .pathsToMatch("/api/v1/users/**") // Matches endpoints for users
//                .build();
//    }
//}
