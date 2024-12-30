package mcnc.talkwave.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springTalkwaveOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("쓰레기 같은 앱! Talkwave")
                        .description("보안 따윈 개나줘버린 최고의 폐기물 채팅앱")
                        .version("v1.0"));
    }
}
