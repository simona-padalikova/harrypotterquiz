package cz.simona.harry.dao;

import java.sql.*;
import java.util.*;
import org.mariadb.jdbc.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.lookup.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import cz.simona.harry.domain.*;

@Repository
public class QuizRepositoryImpl implements QuizRepository{

    private JdbcTemplate jdbcTemplate;
    private RowMapper<Question> rowMapper;


    public QuizRepositoryImpl() {
        try {
            MariaDbDataSource mariaDbDataSource = new MariaDbDataSource();
            mariaDbDataSource.setUserName("student");
            mariaDbDataSource.setPassword("password");
            mariaDbDataSource.setUrl("jdbc:mysql://localhost:3306/harrypotterquiz");

            jdbcTemplate = new JdbcTemplate(mariaDbDataSource);
            rowMapper = BeanPropertyRowMapper.newInstance(Question.class);

        } catch (SQLException e) {
            throw new DataSourceLookupFailureException("Failed to create DataSource", e);
        }
    }

    @Override
    public List<Question> selectQuestionsByLevel(Integer level) {
       List<Question> questions = jdbcTemplate.query("select * from questions, options where ques_id = option_id and level = ?", rowMapper, level);
        return questions;
    }

    @Override
    public Question createNewQuestion(String question, String right_answer, Integer level, String option_a, String option_b, String option_c){
        jdbcTemplate.update("insert into questions (question, right_answer, level) values (?,?,?)", question, right_answer, level);
        Integer lastQuestionId = jdbcTemplate.queryForObject("select max(ques_id) from questions", Integer.class);
        jdbcTemplate.update("insert into options (option_id, option_a, option_b, option_c) values (?,?,?,?)", lastQuestionId, option_a, option_b, option_c);
        Question selectedQuestion = jdbcTemplate.queryForObject("select * from questions, options where ques_id = option_id and ques_id = ?", rowMapper, lastQuestionId);
        return selectedQuestion;
    }

    @Override
    public Integer getLastQuestion(){
        Integer lastQuestion = jdbcTemplate.queryForObject("select max(ques_id) from questions", Integer.class);
        return  lastQuestion;
    }

    @Override
    public Integer selectLastSessionId(){
        return  jdbcTemplate.queryForObject("select max(sesion_id) from playersesion", Integer.class);
    }

    @Override
    public Integer createSessionId() {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement("insert into playersesion (level, score) values (1, 0)", Statement.RETURN_GENERATED_KEYS);
                return statement;
            }
        }, holder);

        Integer primaryKey = holder.getKey().intValue();
        return primaryKey;
    }

    @Override
    public Integer getLevelBySessionId(Integer sessionId){
        return jdbcTemplate.queryForObject("select level from playersesion where sesion_id = ?", Integer.class, sessionId);
    }

    @Override
    public Integer setNextLevel(Integer level, Integer sessionId) {
        jdbcTemplate.update("update playersesion set level = ? where sesion_id = ?", level, sessionId);
        return jdbcTemplate.queryForObject("select level from playersesion where sesion_id = ?", Integer.class, sessionId);
    }

    @Override
    public Integer saveScoreBySessionId(Integer sessionId, Integer score){
        jdbcTemplate.update("update playersesion set score = 0 where sesion_id = ?", sessionId);
       jdbcTemplate.update("update playersesion set score = ? where sesion_id = ?", score, sessionId);
       return getScoreBySessionId(sessionId);
    }

    @Override
    public Integer getScoreBySessionId(Integer sessionId){
       return jdbcTemplate.queryForObject("select score from playersesion where sesion_id = ?", Integer.class, sessionId);
    }

    @Override
    public Question getQuestionByQuestionId(Integer questionId) {
       return jdbcTemplate.queryForObject("select * from questions, options where ques_id = option_id and ques_id=?", rowMapper, questionId);
    }

    @Override
    public Boolean checkSessionId(Integer sessionId){
      Integer actualSessionId = (int) jdbcTemplate.queryForObject("select exists (select * from playersesion where sesion_id = ?)", Integer.class, sessionId);
        return actualSessionId == 1;
    }

}
