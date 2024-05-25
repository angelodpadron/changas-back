package com.changas.repository;

import com.changas.model.Changa;
import com.changas.model.QuestionsAndAnswers.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
}
