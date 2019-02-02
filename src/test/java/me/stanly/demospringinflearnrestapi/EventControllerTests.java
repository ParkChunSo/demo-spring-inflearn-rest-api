package me.stanly.demospringinflearnrestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.stanly.demospringinflearnrestapi.event.Event;
import me.stanly.demospringinflearnrestapi.event.EventRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void creatEvent() throws Exception {
        Event event = Event.builder()
                    .id(100)
                    .name("spring")
                    .description("REST API")
                    .beginEnrollmentDateTime(LocalDateTime.of(2019, 01, 31, 14, 00))
                    .closeEnrollmentDateTime(LocalDateTime.of(2019, 01, 31, 19, 00))
                    .beginEventDateTime(LocalDateTime.of(2019, 01, 29, 14, 00))
                    .endEventDateTime(LocalDateTime.of(2019, 01, 29, 14, 00))
                    .basePrice(100)
                    .maxPrice(200)
                    .limitOfEnrollment(100)
                    .location("강남역")
                    .free(true)
                    .offline(false)
                    .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("offline").value(Matchers.not(true)));
    }

    @Test
    public void creatEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 01, 31, 14, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 01, 31, 19, 00))
                .beginEventDateTime(LocalDateTime.of(2019, 01, 29, 14, 00))
                .endEventDateTime(LocalDateTime.of(2019, 01, 29, 14, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
