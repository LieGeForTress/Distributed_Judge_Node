package oj;
//判题结果对象【还有判题任务JudgeTask。。。注意区别】
public class JudgeResult {
	private static final long serialVersionUID = 1L;
	//状态码   消耗的时间和内存
	private int status;
	private int time;
	private int memory;
	private String message;
	public JudgeResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public JudgeResult(int status, int time, int memory, String message) {
		super();
		this.status = status;
		this.time = time;
		this.memory = memory;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
