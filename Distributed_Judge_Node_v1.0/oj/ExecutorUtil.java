package oj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//运行传进来的 shell 命令
public class ExecutorUtil  {
	
	//运行 shell 命令，返回 ExecMessage 对象
	public static ExecMessage exec(String cmd) {
		Runtime runtime = Runtime.getRuntime();
		Process exec = null;
		try {
			String[] cmdxx = new String[]{"sh","-c",cmd};
			exec = runtime.exec(cmdxx);
			
		}catch(IOException e) {
			e.printStackTrace();
			//有错误，返回一个 错误为: e.getMessage() shell 输出为 null 的 ExecMessage 对象
			 return new ExecMessage(e.getMessage(), null);
		}
		
		//命令运行成功
		ExecMessage res = new ExecMessage();
		//将shell的错误流和输入流转换为 string ，赋值给 shell 命令运行的消息对象
		res.setError(message(exec.getErrorStream()));
		res.setStdout(message(exec.getInputStream()));
		return res;
		
	}
		//运行 shell 命令，返回 ExecMessage 对象
		public static ExecMessage execmd(String cmd) {
			Runtime runtime = Runtime.getRuntime();
			Process exec = null;
			try {
				exec = runtime.exec(cmd);
				
			}catch(IOException e) {
				e.printStackTrace();
				//有错误，返回一个 错误为: e.getMessage() shell 输出为 null 的 ExecMessage 对象
				 return new ExecMessage(e.getMessage(), null);
			}
			//命令运行成功
			ExecMessage res = new ExecMessage();
			//将shell的错误流和输入流转换为 string ，赋值给 shell 命令运行的消息对象
			res.setError(message(exec.getErrorStream()));
			res.setStdout(message(exec.getInputStream()));
			return res;
			
		}
	
	
	//通过 进程的 错误流 输入流，为创建的error stdout对象赋值
	private static String message(InputStream inputStream) {
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
			StringBuilder message = new StringBuilder();
			String str;
			while((str = reader.readLine()) != null) {
				message.append(str);
				
			}
			String result = message.toString();
			if(result.equals("")) {
				return null;
			}
			return result;
		}catch(IOException e) {
			return e.getMessage();
		}finally {
			try {
				inputStream.close();
				reader.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
