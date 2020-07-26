package cz.simona.harry.dao;

import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import cz.simona.harry.domain.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class QuizRepositoryImplTest {
    
    @Autowired
    private QuizRepository memoryRepository;

    @Test
    void givenQuestions_whenSelectQuestionsByLevel_listQuestionReturned(){
       Question expectedQuestion1 = memoryRepository.createNewQuestion("Jak se jmenuje otec HP?", "James", 11, "James", "Peter", "Mark");
       Question expectedQuestion2 = memoryRepository.createNewQuestion("Jak se jmenuje matka HP?", "Lily", 11, "Jane", "Lily", "Lisa");
       Question expectedQuestion3 = memoryRepository.createNewQuestion("Jak se jmenuje nejleší kamarád HP?", "Ron", 11, "Sam", "Draco", "Ron");
       List<Question> expectedQuestion = new ArrayList<>();
       expectedQuestion.add(expectedQuestion1);
       expectedQuestion.add(expectedQuestion2);
       expectedQuestion.add(expectedQuestion3);
       List<Question> actualQuestion = memoryRepository.selectQuestionsByLevel(11);
       assertEquals(actualQuestion, expectedQuestion);
    }

    @Test
    void givenQuestion_whenCreateNewQuestion_newQuestionReturned(){
        Question expectedQuestion = memoryRepository.createNewQuestion("Jak se jmenuje nejlepší kamarádka HP?", "Hermiona", 12, "Hermiona", "Jane", "Lily");
        Integer lastQuestionId = memoryRepository.getLastQuestion();
        Question actualQuestion = memoryRepository.getQuestionByQuestionId(lastQuestionId);
        assertEquals(actualQuestion, expectedQuestion);
    }

    @Test
    void givenSectionId_whenCreateSessionId_sessionIdReturned() {
        Integer actualSessionId = memoryRepository.createSessionId();
        Integer expectedSessionId = memoryRepository.selectLastSessionId();
        assertEquals(actualSessionId, expectedSessionId);
    }

    @Test
    void givenLeven_whenGetLevelBySessionId_levelReturned() {
        Integer sessionId = memoryRepository.createSessionId();
        Integer expectedLevel = 1;
        Integer actualLevel = memoryRepository.getLevelBySessionId(sessionId);
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void givenLevel_whenSetNextLevel_anotherLevelReturned(){
        Integer expectedLevel = 2;
        Integer sessionId = memoryRepository.createSessionId();
        Integer level = memoryRepository.getLevelBySessionId(sessionId);
        Integer actualLevel = memoryRepository.setNextLevel(level+1, sessionId);
        assertEquals(actualLevel, expectedLevel);
    }

    @Test
    void givenScore_whenSaveScoreById_scoreReturned() {
        Integer score = 4;
        Integer sessionId = memoryRepository.createSessionId();
        Integer actualScore = memoryRepository.saveScoreBySessionId(sessionId, score);
        Integer expectedScore = 4;
        assertEquals(expectedScore, actualScore);
    }

    @Test
    void givenScore_whenGetScoreBySessionId_scoreReturned(){
        Integer expectedScore = 4;
        Integer sessionId = memoryRepository.createSessionId();
        memoryRepository.saveScoreBySessionId(sessionId, 4);
        Integer actualScore = memoryRepository.getScoreBySessionId(sessionId);
        assertEquals(actualScore, expectedScore);
    }

    @Test
    void givenQuestion_whenGetQuestionById_QuestionReturned(){
        Question expectedQuestion = memoryRepository.createNewQuestion("Co mel harry potter na cele?","Blesk", 10, "Kometu", "Blesk", "Kruh");
        Integer lastQuestionId = memoryRepository.getLastQuestion();
        Question actualQuestion = memoryRepository.getQuestionByQuestionId(lastQuestionId);
        assertEquals(actualQuestion, expectedQuestion);
    }

    @Test
     void givenSessionIdWhichNotExists_whenCheckSessionId_booleanReturned(){
        Integer lastSessionId = memoryRepository.selectLastSessionId();
        Integer sessionId = lastSessionId + 50;
        Boolean expectBoolean = memoryRepository.checkSessionId(sessionId);
        assertFalse(expectBoolean);
     }

    @Test
    void givenSessionIdWhichExists_whenCheckSessionId_booleanReturned(){
        Integer sessionId= 5;
        Boolean expectBoolean = memoryRepository.checkSessionId(sessionId);
        assertTrue(expectBoolean);
    }


}