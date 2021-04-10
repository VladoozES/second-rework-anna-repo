package net.codejava;

import java.util.ArrayList;
import java.util.List;

public class AnswerForm {
	private List<Answer> answers;
	
	protected AnswerForm() {
		answers = new ArrayList<Answer>();
	}
	
	public void addAnswer(Answer answer){
		answers.add(answer);
	}
	
	public List<Answer> getAnswers(){
		return answers;
	}
	
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
}
