package cz.simona.harry.service;

import java.util.*;
import org.springframework.stereotype.*;
import cz.simona.harry.exception.*;
import cz.simona.harry.dao.*;
import cz.simona.harry.domain.*;

@Service
public class QuizService {

    private QuizRepository memoryRepository;
    private Random random = new Random();

    public QuizService(QuizRepository memoryRepository) {
       this.memoryRepository = memoryRepository;
    }

    public List<Question> selectQuestion(Integer level) {
        List<Question> allQuestions = memoryRepository.selectQuestionsByLevel(level);
        int size = allQuestions.size();
        for (int i = 0; i < 50; i++) {
            int rand = random.nextInt(size);
            Question randQues = allQuestions.get(rand);
            allQuestions.remove(rand);
            allQuestions.add(size - 1, randQues);
        }
        List<Question> selectedQuestion = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            selectedQuestion.add(i, allQuestions.get(i));
        }

        return selectedQuestion;
    }

    public Integer evaluationQues(List<String> results){
     Integer points = 0;
     for(String result: results){
         String[] resultI = result.split("&");
         String enteredAnswer = resultI[0];
         String resultIdString = resultI[1];
         Integer resultId = Integer.parseInt(resultIdString);
         Question question = memoryRepository.getQuestionByQuestionId(resultId);
         String rightAnswer = question.getRightAnswer();
         if (enteredAnswer.equals(rightAnswer)) {
             points++;
         }
     }
        return points;
    }

    public Integer createSessionId() {
        return memoryRepository.createSessionId();
    }

    public Integer getLevelBySessionId(Integer id) throws SessionNotFoundException{
        if(!memoryRepository.checkSessionId(id)){
            throw new SessionNotFoundException("Session with id: " + id + "not found.");
        }
        return memoryRepository.getLevelBySessionId(id);
    }

    public Integer setLevelForSession(Integer points, Integer session_id) throws SessionNotFoundException {
        if (!memoryRepository.checkSessionId(session_id)) {
            throw new SessionNotFoundException("Session with id: " + session_id + "not found.");
        }
        Integer level = memoryRepository.getLevelBySessionId(session_id);
        if (points >= 3){
            level++;
            memoryRepository.setNextLevel(level, session_id);
        }
        return level;
    }

    public Integer saveScoreById(Integer score, Integer id) throws SessionNotFoundException{
        if(!memoryRepository.checkSessionId(id)){
            throw new SessionNotFoundException("Session with id: " + id + "not found");
        }
        return memoryRepository.saveScoreBySessionId(id, score);
    }

    public Integer getScoreById(Integer id) throws SessionNotFoundException {
        if(!memoryRepository.checkSessionId(id)){
            throw new SessionNotFoundException("Session with id: " + id + "not found");
        }
        return memoryRepository.getScoreBySessionId(id);
    }

    public String showResultStatus(Integer level, Integer points) {
        if (level == 1){
            return "Harry Potter začátečník, určitě si přečti Harryho nebo se alespoň ještě jednou podívej na všechny filmy.";
        } else if (level == 2){
            return "Harry Potter mírně pokročilý, určitě si přečti Harryho nebo se alespoň ještě jednou podívej na všechny filmy";
        } else if (level == 3) {
            return "Harry Potter znalec, vyznáš se, ale některé detaily ještě nemáš v malíčku.";
        } else if (points > 3 && level == 4) {
            return "Harry Potter ultra znalec, znáš každičký detail.";
        } else {
            return "Chyba, zkus to znovu";
        }
    }

}
