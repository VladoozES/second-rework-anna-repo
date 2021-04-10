package net.codejava;

public class ReplanConfig {
	private Long cardId;
	private int newBoxNum;
	
	protected ReplanConfig() {
		
	}
	
	protected ReplanConfig(Long cardId) {
		this.cardId = cardId;
	}
	
	protected ReplanConfig(Long cardId, int newBoxNum) {
		this.cardId = cardId;
		this.newBoxNum = newBoxNum;
	}
	
	public int getNewBoxNum() {
		return newBoxNum;
	}
	public void setNewBoxNum(int newBoxNum) {
		this.newBoxNum = newBoxNum;
	}
	public Long getCardId() {
		return cardId;
	}
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
}
