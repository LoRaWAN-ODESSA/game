package com.quiz.game.repo;

import com.quiz.game.models.Questions;
import org.springframework.data.repository.CrudRepository;

public interface QuestionsRepository extends CrudRepository<Questions, Integer> {

}
