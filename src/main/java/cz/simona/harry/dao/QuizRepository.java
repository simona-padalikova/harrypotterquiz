package cz.simona.harry.dao;

import java.util.*;
import cz.simona.harry.domain.*;

public interface QuizRepository {

    List<Question> selectQuestionsByLevel(Integer level);

    Question createNewQuestion(String question, String right_answer, Integer level, String option_a, String option_b, String option_c);

    Integer getLastQuestion();

    Integer selectLastSessionId();

    Integer createSessionId();

    Integer getLevelBySessionId(Integer sessionId);

    Integer setNextLevel(Integer level, Integer sessionId);

    Integer saveScoreBySessionId(Integer sessionId, Integer score);

    Integer getScoreBySessionId(Integer sessionId);

    Question getQuestionByQuestionId(Integer questionId);

    Boolean checkSessionId(Integer sessionId);
    }
