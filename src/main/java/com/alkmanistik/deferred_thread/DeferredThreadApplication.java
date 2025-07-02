package com.alkmanistik.deferred_thread;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeferredThreadApplication {

    public static void main(String[] args) {

        Dotenv dotenv;
        try {
            dotenv = Dotenv.configure()
                    .load();
        } catch (DotenvException e) {
            System.err.println("Файл .env не загружен или не найден");
            System.exit(1);
            return;
        }

        String[] requiredVars = {"DB_URL", "DB_NAME", "DB_USERNAME", "DB_PASSWORD", "GRAFANA_USER", "GRAFANA_PASSWORD"};
        for (String var : requiredVars) {
            if (dotenv.get(var) == null) {
                System.err.println("ОШИБКА: В файле .env отсутствует переменная: " + var);
                System.exit(1);
            }
        }

        SpringApplication.run(DeferredThreadApplication.class, args);
    }

}
