package br.com.pagbanks.projeto_onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjetoOnboardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoOnboardingApplication.class, args);
    }
}
