package cz.simona.harry.service;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;

import cz.simona.harry.exception.*;
import cz.simona.harry.dao.*;
import cz.simona.harry.domain.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @MockBean
    private QuizRepository memoryRepository;


    @Test
    void givenSavedQuestions_whenSelectQuestionByLevel_QuestionsReturned() {
        List<Question> expectedQuestions = getMockQuestions();
        Mockito.when(memoryRepository.selectQuestionsByLevel(1)).thenReturn(expectedQuestions);
        List<Question> actualQuestions = quizService.selectQuestion(1);
        assertEquals(actualQuestions, expectedQuestions);
    }

    @Test
    void givenResult_whenEvaluationQues_pointsReturned() {
        Integer expectedPoints = 3;
        List<String> results = getMockResults();
        List<Question> questions = getMockQuestions();
        for (Question question: questions){
            Mockito.when(memoryRepository.getQuestionByQuestionId(question.getQuesId())).thenReturn(question);
        }
        Integer actualPoints = quizService.evaluationQues(results);
        assertEquals(actualPoints, expectedPoints);
    }

    @Test
    void givenLevel_whenSetLevelForSessionIdAndPointsIsHigherThanThree_thenNextLevelReturned() throws SessionNotFoundException{
        Integer points = 4;
        Integer sessionId = 23;
        Integer level = 1;
        Integer expectedLevel = level + 1;
        Mockito.when(memoryRepository.checkSessionId(sessionId)).thenReturn(true);
        Mockito.when(memoryRepository.getLevelBySessionId(sessionId)).thenReturn(level);
        Integer actualLevel = quizService.setLevelForSession(points, sessionId);
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void givenLevel_whenSetLevelForSessionIdAndPointIs2_levelReturned() throws SessionNotFoundException{
        Integer expectLevel = 1;
        Integer points = 2;
        Integer sessionId = 23;
        Integer level = 1;
        Mockito.when(memoryRepository.checkSessionId(sessionId)).thenReturn(true);
        Mockito.when(memoryRepository.getLevelBySessionId(sessionId)).thenReturn(level);
        Mockito.when(memoryRepository.setNextLevel(level, sessionId)).thenReturn(level);
        Integer actualLevel = quizService.setLevelForSession(points, sessionId);
        assertEquals(expectLevel, actualLevel);
    }

    @Test
    void givenSectionId_whenCreateSessionId_sessionIdReturned() {
        Integer expectedSessionId = 23;
        Mockito.when(memoryRepository.createSessionId()).thenReturn(expectedSessionId);
        Integer actualSessionId = quizService.createSessionId();
        assertEquals(actualSessionId, expectedSessionId);
    }

    @Test
    void givenLevel_whenGetLevelBySessionId_levelReturned() throws SessionNotFoundException {
        Integer sessionId = 23;
        Integer expectedLevel = 2;
        Mockito.when(memoryRepository.checkSessionId(sessionId)).thenReturn(true);
        Mockito.when(memoryRepository.getLevelBySessionId(sessionId)).thenReturn(expectedLevel);
        Integer actualLevel = quizService.getLevelBySessionId(23);
        assertEquals(actualLevel, expectedLevel);
    }

    @Test
    void givenScore_whenSaveScoreBySessionId_scoreReturned() throws SessionNotFoundException{
        Integer expectScore = 4;
        Integer sessionId = 23;
        Mockito.when(memoryRepository.checkSessionId(sessionId)).thenReturn(true);
        Mockito.when(memoryRepository.saveScoreBySessionId(sessionId, expectScore)).thenReturn(expectScore);
        Integer actualScore = quizService.saveScoreById(expectScore, sessionId);
        assertEquals(actualScore, expectScore);
    }

    @Test
    void givenScore_whenGetScoreById_scoreReturned() throws SessionNotFoundException{
        Integer expectScore = 4;
        Integer sessionId = 23;
        Mockito.when(memoryRepository.checkSessionId(sessionId)).thenReturn(true);
        Mockito.when(memoryRepository.getScoreBySessionId(sessionId)).thenReturn(expectScore);
        Integer actualScore = quizService.getScoreById(sessionId);
        assertEquals(actualScore, expectScore);
    }

    @Test
    void givenResultStatus_whenShowResultStatus_StringReturned(){
        Integer level = 2;
        Integer points = 4;
        String expectedStatus = "Harry Potter mírně pokročilý, určitě si přečti Harryho nebo se alespoň ještě jednou podívej na všechny filmy";
        String actualStatus = quizService.showResultStatus(level, points);
        assertEquals(actualStatus, expectedStatus);
    }

    @Test
    void givenNoSessions_whenGetLevelBySessionId_ExceptionThrown(){
        Integer sessionId = memoryRepository.selectLastSessionId() + 10;
        String expectedMessage = "Session with id: " + sessionId + "not found.";
        Mockito.when(memoryRepository.checkSessionId(sessionId)).thenReturn(false);
        Exception exception = assertThrows(SessionNotFoundException.class, ()-> quizService.getLevelBySessionId(sessionId));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

    }


    private List<Question> getMockQuestions(){
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "Co mel harry potter na cele?", "Blesk", 1, "Kometu", "Blesk", "Kruh"));
        questions.add(new Question(2, "Do jake koleje chodi harry potter?", "Nebelvir", 1, "Mrzimor", "Havraspar", "Nebelvir"));
        questions.add(new Question(3, "Do jake koleje chodi Draco Malfoy?", "Zmijozel", 1, "Zmijozel", "Havraspar", "Nebelvir"));
        questions.add(new Question(4, "Jak se jmenuji rodice Harryho Pottera?", "James a Lily", 1, "James a Lily", "Jack a Emily", "Petunie a Herold"));
        questions.add(new Question(5, "Jak se jmenuje reditel Bradavic?", "Brumbal", 1, "Brumbal", "Snape", "Hagrid"));
        return questions;
    }
    
    private List<String> getMockResults(){
        List<String> results = new ArrayList<>();
        results.add("Blesk&1");
        results.add("Mrzinor&2");
        results.add("Zmijozel&3");
        results.add("James a Lily&4");
        results.add("Snape&5");
        return results;
    }

}