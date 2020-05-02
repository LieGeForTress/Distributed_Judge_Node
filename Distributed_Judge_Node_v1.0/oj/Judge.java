package oj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import dj.dao.errDao;
import dj.dao_impl.errDaoImpl;

import net.sf.json.JSONObject;

/*
 * OnlineJudge Core
 * 结果状态码:
 * 0 Accepted
 * 1 Presentation Error
 * 2 Time Limit Exceeded
 * 3 Memory Limit Exceeded
 * 4 Wrong Answer
 * 5 Runtime Error
 * 6 Output Limit Exceeded
 * 7 Compile Error
 * 8 System Error
 * */
public class Judge {
	/*
     * 判题任务分配器
	 * param:task对象
	 * return:result对象
	 * */
	public JudgeResult judge(JudgeTask task) {
		JudgeResult result = new JudgeResult();
		//根据运行id 创建一个目录 /sub/xxx
		String path="/oj/sub/"+task.getSolution_id();
		ExecutorUtil.exec("chmod -R 755 "+path);
		File file = new File(path);
		file.mkdirs();
		try {
			createFile(path,task.getCode(),task.getLanguage());
		}catch(Exception e) {
			result.setStatus(8); //系统错误，无法创建源文件
			//ExecutorUtil.exec("rm -rf "+ path);
			return result;
		}
		//写入源程序正确,开始编译
		String message =  compile(path,task.getLanguage());
		if (message != null) {
			//编译报错
			result.setStatus(7);
			result.setMessage(message);
			ExecutorUtil.exec("rm -rf "+path);
			return result;
		}
		//编译成功
		//修改文件权限
		ExecutorUtil.exec("chmod -R 755 "+path);
		//调用lorun
		String output = run(path,task.getLanguage(),task.getTime_limit(),task.getMemory_limit(),task.getProblem_id(),task.getContest_id());
		//解析返回的JSON格式字符串
		JSONObject object = JSONObject.fromObject(output);
		result.setMessage(output);
		result.setStatus(object.getInt("status"));
		result.setTime(object.getInt("max_time"));
		result.setMemory(object.getInt("max_memory"));
		//删除编译的程序
		ExecutorUtil.exec("rm -rf "+path);
		return result;
	}
	
	
	/*
	 * 写入源代码
	 * param:文件路径 源代码 程序语言
	 * */
	public static void createFile(String path,String source,int lang) throws Exception {
		// 1 C
		// 2 C++
		// 3 Java
		// 4 Python
		String filename;
		if(lang == 1) {
			filename = "main.c";
		}else if(lang == 2) {
			filename = "main.cpp";
		}else if(lang == 3) {
			filename = "Main.java";
		}else {
			filename = "main.py";
		}
		File file = new File(path);
		file.mkdirs();
		File files = new File(path + "/" + filename);
		files.createNewFile();//没有path目录，直接抛出异常
		OutputStream output = new FileOutputStream(files);
		PrintWriter writer = new PrintWriter(output);
		writer.print(source);
		writer.close();
		output.close();
	}
	/*
	 * 编译
	 * param:文件路径 程序语言
	 * */
	public String compile(String path,int lang) {
		String cmd;
		if(lang == 1) {
			cmd = "gcc "+"/"+path+"/main.c -o "+"/"+path+"/main";
		}else if(lang == 2) {
			cmd = "g++ -std=c++11 -Wall -o "+path+"/main "+path+"/main.cpp";
		}else if(lang == 3) {
			cmd = "javac -encoding UTF-8 "+path+"/Main.java";
		}else {
			cmd = "python3 -m py_compile "+path+"/main.py";
		}
		//执行运行shell命令，返回error信息，，，编译的信息放在stdout中
		return ExecutorUtil.exec(cmd).getError();
	}
	/*
	 * 调用lorun
	 * */
	public String run(String path,int lang,int time,int memory,int p_id,int c_id) {
		// Java 3 和 Python 4 运行时间和内存加倍
		String cmd;
		time = time * 1000;
		String pid;
		//判断是否是比赛，如果是，则pid【代表测试路径】就应该是 /oj/con/c_id/p_id
		if(c_id != 0) {
			pid= "/oj/con/"+c_id+"/"+p_id;
		}else {
			pid = "/oj/pro/"+p_id;
		}
		if(lang == 1) {
			cmd = "python3 /root/judge.py "+path+"/./main "+pid+" "+path+" "+time+" "+memory;
		}else if(lang == 2){
			cmd = "python3 /root/judge.py "+path+"/./main "+pid+" "+path+" "+time+" "+memory;
		}else if(lang == 3) {
			cmd="python3 /root/judge.py java@-classpath@"+path+"@Main "+pid+" "+path+" "+time*2+" "+memory * 2;
		}else {
			cmd="python3 /root/judge.py python3@"+path+"/__pycache__/main.cpython-38.pyc "+pid+" "+path+" "+time*2+" "+memory * 2;
		}
		
		//运行时不会报错，都将以JSON格式返回评测结果
		ExecMessage em = ExecutorUtil.execmd(cmd);
		String re = em.getStdout();
		//err.inserErr("end lorun" + em.getError() + " "+em.getStdout());
		return re;
	}
}
