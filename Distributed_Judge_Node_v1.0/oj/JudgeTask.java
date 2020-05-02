package oj;
//判题任务对象的封装
public class JudgeTask {
	private static final long serialVersionUID = 1L;
	private int solution_id;
	private int problem_id;
	private int contest_id;
	private int time_limit;
	private int memory_limit;
	private int language;
	private String code;
	public JudgeTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public JudgeTask(int solution_id, int problem_id, int contest_id, int time_limit, int memory_limit, int language,
			String code) {
		super();
		this.solution_id = solution_id;
		this.problem_id = problem_id;
		this.contest_id = contest_id;
		this.time_limit = time_limit;
		this.memory_limit = memory_limit;
		this.language = language;
		this.code = code;
	}



	public int getSolution_id() {
		return solution_id;
	}
	public void setSolution_id(int solution_id) {
		this.solution_id = solution_id;
	}
	public int getProblem_id() {
		return problem_id;
	}
	public void setProblem_id(int problem_id) {
		this.problem_id = problem_id;
	}
	public int getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(int time_limit) {
		this.time_limit = time_limit;
	}
	public int getMemory_limit() {
		return memory_limit;
	}
	public void setMemory_limit(int memory_limit) {
		this.memory_limit = memory_limit;
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public int getContest_id() {
		return contest_id;
	}



	public void setContest_id(int contest_id) {
		this.contest_id = contest_id;
	}
	
	
}
