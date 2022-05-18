package com.quiz.game.controllers;

import com.quiz.game.models.Questions;
import com.quiz.game.models.User;
import com.quiz.game.repo.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;

@Controller
public class GameController {
    public static final int NUM_QUESTIONS = 12;
    private Questions[] questionsList;
    private User[] users;
    private Integer temp = 0;
    private String tempString = temp.toString();

    @Autowired
    private QuestionsRepository questionsRepository;


    @GetMapping("/game")
    public String gameMain(Model model) {
        Iterable<Questions> questions = questionsRepository.findAll();
        model.addAttribute("questions", questions);
        return "game-main";
    }

    @GetMapping("/game/add")
    public String questionAdd(Model model) {
        return "game-add";
    }

    @PostMapping("/game/add")
    public String questionPostAdd(@RequestParam String question, @RequestParam String answer1, @RequestParam String answer2, @RequestParam String answer3, @RequestParam String answer4, @RequestParam int rightAnswer, Model model) {
        Questions questions = new Questions(question, answer1, answer2, answer3, answer4, rightAnswer);
        questionsRepository.save(questions);
        return "redirect:/game";
    }

    @GetMapping("/game/newGame")
    public String startNewGame(Model model) {
        return "start-new-game";
    }


    @PostMapping("/game/newGame")
    public String startGame(@RequestParam int numUsers, @RequestParam String user1, @RequestParam String user2, Model model) {
        String[] names = new String[]{user1, user2};
        User[] users = new User[2];
        for (int i = 0; i < numUsers; i++) {
            User newUser = new User();
            newUser.setName(names[i]);
            newUser.setScore(0);
            users[i] = newUser;
        }
        this.users = users;
        createQuestionList();
        return "redirect:/game/newGame/1";
    }

    @GetMapping("/game/newGame/1")
    public String question( Model model) {
        Questions question = questionsList[0];
        model.addAttribute("questions1", question);
        return "question1";
    }

    @PostMapping("/game/newGame/1")
    public String setAnswer(int rightAnswer, Model model) {
        if(rightAnswer == questionsList[0].getRightAnswer()) {
            users[0].setScore(users[0].getScore() + 1);
        }
        return "redirect:/game/newGame/2";
    }

    @GetMapping("/game/newGame/2")
    public String question2(Model model) {
        Questions questions2 = questionsList[1];
        model.addAttribute("questions2", questions2);
        return "question2";
    }

    @PostMapping("/game/newGame/2")
    public String setAnswer2(@RequestParam int rightAnswer, Model model) {
        if(rightAnswer == questionsList[1].getRightAnswer()) {
            users[0].setScore(users[0].getScore() + 1);
        }
        return "redirect:/game/newGame/3";
    }
    public int[] generateNumList(int maxQuestions) {
        int[] numList = new int[NUM_QUESTIONS];
        ArrayList numbers = new ArrayList<>();
        for(int i = 0; i < maxQuestions; i++) {
            numbers.add(i+1);
        }
        Collections.shuffle(numbers);
        for(int j =0; j < NUM_QUESTIONS; j++) {
            numList[j] = (int) numbers.get(j);
        }
        return numList;
    }

    private void createQuestionList() {
        int i = 0;
        int[] numList = generateNumList((int) questionsRepository.count());
        Questions[] questionsList = new Questions[NUM_QUESTIONS];
        for (int num:numList) {
            Questions newQuestion = new Questions();
            newQuestion.setQuestion(questionsRepository.findById(num).get().getQuestion());
            newQuestion.setAnswer1(questionsRepository.findById(num).get().getAnswer1());
            newQuestion.setAnswer2(questionsRepository.findById(num).get().getAnswer2());
            newQuestion.setAnswer3(questionsRepository.findById(num).get().getAnswer3());
            newQuestion.setAnswer4(questionsRepository.findById(num).get().getAnswer4());
            newQuestion.setRightAnswer(questionsRepository.findById(num).get().getRightAnswer());
            questionsList[i] = newQuestion;
            i++;
        }
        this.questionsList = questionsList;
    }
}

