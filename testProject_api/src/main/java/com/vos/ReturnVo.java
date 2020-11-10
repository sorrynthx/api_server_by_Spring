package com.vos;

public class ReturnVo {
	private String result;
    private String messages;
    private Object data;

    public ReturnVo() {	}
    
    public ReturnVo(String result, String messages, Object data) {
    	this.result = result;
    	this.messages = messages;
    	this.data = data;
    }
    public ReturnVo(String result, int messages, Object data) {
    	this.result = result;
    	this.messages = messages+"";
    	this.data = data;
    }
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessages() {
		return messages;
	}
	public void setMessages(String messages) {
		this.messages = messages;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		StringBuilder SB = new StringBuilder("{");
		SB.append("'result':'").append(result).append("',");
		SB.append("'messages':'").append(messages).append("',");
		SB.append("'data':'").append(data.toString()).append("'");
		
		return SB.append("}").toString();
	}
}
