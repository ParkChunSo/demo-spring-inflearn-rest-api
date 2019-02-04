package me.stanly.demospringinflearnrestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.stanly.demospringinflearnrestapi.common.TestDescription;
import me.stanly.demospringinflearnrestapi.event.Event;
import me.stanly.demospringinflearnrestapi.event.EventDto;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void creatEvent() throws Exception {
        EventDto event = EventDto.builder()
//                    .id(100)
                    .name("spring")
                    .description("REST API")
                    .beginEnrollmentDateTime(LocalDateTime.of(2019, 01, 31, 14, 00))
                    .closeEnrollmentDateTime(LocalDateTime.of(2019, 01, 31, 19, 00))
                    .beginEventDateTime(LocalDateTime.of(2019, 01, 29, 14, 00))
                    .endEventDateTime(LocalDateTime.of(2019, 01, 29, 14, 00))
                    .location("강남역")
                    .basePrice(100)
                    .maxPrice(200)
                    .limitOfEnrollment(100)
/*                    .free(true)
                    .offline(false)*/
                    .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
//                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("offline").value(Matchers.not(true)));
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용하는 경우에 에러가 발생하는 테스트")
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

     @Test
     @TestDescription("입력이 비어있는 경우에 에러가 발생하는 테스트")
    public void crestEvent_Bad_Request_Empty_Input() throws Exception {
         EventDto eventDto = EventDto.builder()
                 .name("spring")
                 .description("REST API")
                 .beginEnrollmentDateTime(LocalDateTime.of(2019, 01, 31, 14, 00))
                 .closeEnrollmentDateTime(LocalDateTime.of(2019, 01, 29, 19, 00))
                 .beginEventDateTime(LocalDateTime.of(2019, 01, 28, 14, 00))
                 .endEventDateTime(LocalDateTime.of(2019, 01, 27, 14, 00))
                 .basePrice(10000)
                 .maxPrice(200)
                 .limitOfEnrollment(100)
                 .location("강남역")
                 .build();

         this.mockMvc.perform(post("/api/events")
                             .contentType(MediaType.APPLICATION_JSON_UTF8)
                             .content(this.objectMapper.writeValueAsString(eventDto)))
                 .andExpect(status().isBadRequest());
     }

}
