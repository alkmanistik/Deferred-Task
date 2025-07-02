package com.alkmanistik.deferred_thread.env;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class EnvTest {
    @Test
    public void checkEnvFile() {

        Dotenv dotenv;
        try {
            dotenv = Dotenv.configure()
                    .load();
        } catch (DotenvException e) {
            fail("Файл .env не загружен или не найден");
            return;
        }

        String[] requiredVars = {"DB_URL", "DB_NAME", "DB_USERNAME", "DB_PASSWORD", "GRAFANA_USER", "GRAFANA_PASSWORD"};
        for (String var : requiredVars) {
            assertNotNull(dotenv.get(var), var + " не указан в .env!");
        }
    }
}
