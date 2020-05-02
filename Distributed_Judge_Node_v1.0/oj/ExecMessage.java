package oj;

//运行 shell 命令，所返回的 信息对象的封装
public class ExecMessage {
	private static final long serialVersionUID = 1L;
	private String error;
	private String stdout;
	public ExecMessage(String error, String stdout) {
		super();
		this.error = error;
		this.stdout = stdout;
	}
	public ExecMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getStdout() {
		return stdout;
	}
	public void setStdout(String stdout) {
		this.stdout = stdout;
	}
	
	
}