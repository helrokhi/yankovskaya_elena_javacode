package ru.pro.task1.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;
import ru.pro.task1.config.JacksonConfig;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
@Import(JacksonConfig.class)
class CustomTimeDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private static final String REGEX_TIMESTAMP = "\\d{4}:\\d{2}:\\d{2}##:\\d{2}:\\d{2}:\\d{2}:\\d{3}";

    @Test
    void serialize_ShouldUseCustomFormat() throws Exception {
        CustomTimeDto dto = new CustomTimeDto(LocalDateTime.now());

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).containsPattern("\"timestamp\":\"" + REGEX_TIMESTAMP + "\"");
    }

    @Test
    void deserialize_ShouldParseCustomFormat() throws Exception {
        String json = "{\"timestamp\":\"2025:08:07##:12:34:56:789\"}";

        CustomTimeDto dto = objectMapper.readValue(json, CustomTimeDto.class);

        assertThat(dto.timestamp()).isEqualTo(
                LocalDateTime.of(2025, 8, 7, 12, 34, 56, 789_000_000)
        );
    }
}