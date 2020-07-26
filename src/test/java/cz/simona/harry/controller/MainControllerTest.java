package cz.simona.harry.controller;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.result.*;

import cz.simona.harry.form.*;
import cz.simona.harry.service.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(value = MainController.class)

class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @Test
    void givenQuiz_whenViewQuiz_QuizWithSelectedQuestionReturned() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void givenResultForm_whenEvaluationQuiz_redirectResultSessionIdReturned() throws Exception {
        ResultForm result = new ResultForm(Arrays.asList("Hagrid&3", "Sirius&2", "James a Lily&5","paní Norrisová&7", "Tom Radle&4"));
        this.mockMvc.perform(post("/23", result)
                .param("result", "result")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/result/{sessionId}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void givenResult_whenShowResults_AnotherHtmlWithResultReturned() throws Exception {
        Integer sessionId = 23;
        this.mockMvc.perform(get("/result/{sessionId}", sessionId)
                .param("sessionId", "sessionId"))
                .andExpect(status().isOk())
                .andExpect(view().name("levelResult"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
   void givenResult_whenShowAnotherLevelQues_AnotherQuestionWithRightLevelReturned() throws Exception {
        Integer sessionId = 23;
        this.mockMvc.perform(get("/{sessionId}", sessionId)
                .param("sessionId", "sessionId"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void givenResult_whenShowFinalResult_resultStatusReturned() throws Exception{
        Integer sessionId = 23;
        this.mockMvc.perform(get("/finalResult/{sessionId}", sessionId)
                .param("sessionId", "sessionId"))
                .andExpect(status().isOk())
                .andExpect(view().name("finalResult"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}