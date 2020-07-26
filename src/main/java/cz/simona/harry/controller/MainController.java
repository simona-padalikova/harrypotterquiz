package cz.simona.harry.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import cz.simona.harry.exception.*;
import cz.simona.harry.domain.*;
import cz.simona.harry.form.*;
import cz.simona.harry.service.*;

@Controller
public class MainController {


    @Autowired
    public MainController(QuizService service) {
        this.service = service;
    }

    private QuizService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView viewQuiz() {
        ModelAndView modelAndView = new ModelAndView("index");
        try {
            Integer sessionId = service.createSessionId();
            modelAndView.addObject("sessionId", sessionId);
            List<Question> questions = service.selectQuestion(service.getLevelBySessionId(sessionId));
            modelAndView.addObject("question", questions);
            return modelAndView;
        } catch (SessionNotFoundException ex) {
            return new ModelAndView("redirect:/");
        }

    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.POST)
    public ModelAndView evaluationQuiz(ResultForm resultForm, @PathVariable("sessionId") Integer sessionId) {
        try {
            Integer points = service.evaluationQues(resultForm.getResult());
            service.saveScoreById(points, sessionId);
            service.setLevelForSession(points, sessionId);
            return new ModelAndView("redirect:/result/{sessionId}");
        }  catch (SessionNotFoundException ex) {
            return new ModelAndView("redirect:/");
        }

    }


    @RequestMapping(value = "/result/{sessionId}", method = RequestMethod.GET)
    public ModelAndView showResults(@PathVariable("sessionId") Integer sessionId){
        ModelAndView modelAndView = new ModelAndView("levelResult");
        try {
            Integer points = service.getScoreById(sessionId);
            Integer level = service.getLevelBySessionId(sessionId);
            modelAndView.addObject("level", level);
            modelAndView.addObject("points", points);
            modelAndView.addObject("sessionId", sessionId);
            return modelAndView;
        } catch (SessionNotFoundException ex){
            return new ModelAndView("redirect:/");
        }
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
    public ModelAndView showAnotherLevelQues(@PathVariable("sessionId") Integer sessionId) {
        try {
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("session", sessionId);
            Integer level = service.getLevelBySessionId(sessionId);
            List<Question> questions = service.selectQuestion(level);
            modelAndView.addObject("question", questions);
            return modelAndView;
        } catch (SessionNotFoundException ex) {
            return new ModelAndView("redirect:/");
        }
    }

    @RequestMapping(value = "/finalResult/{sessionId}", method = RequestMethod.GET)
    public ModelAndView showFinalResult(@PathVariable("sessionId") Integer sessionId){
        ModelAndView modelAndView = new ModelAndView("finalResult");
        try {
            modelAndView.addObject("sessionId", sessionId);
            Integer points = service.getScoreById(sessionId);
            Integer level = service.getLevelBySessionId(sessionId);
            String finalResultScore = service.showResultStatus(level, points);
            modelAndView.addObject("finalResultScore", finalResultScore);
            return modelAndView;
        } catch (SessionNotFoundException ex) {
            return new ModelAndView("redirect:/");
        }
    }

}


