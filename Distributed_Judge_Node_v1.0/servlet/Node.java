package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import oj.JudgeTask;
import oj.Judge;
import oj.JudgeResult;

import dj.entity.*;
import dj.dao.*;
import dj.dao_impl.*;

/**
 * Servlet implementation class Node
 */
@WebServlet("/Node")
public class Node extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Node() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("UTF-8");
		int sid = Integer.parseInt(request.getParameter("sid"));
		int lang = Integer.parseInt(request.getParameter("lang"));
		int pid = Integer.parseInt(request.getParameter("pid"));
		String cc = request.getParameter("cid");
		int cid = 0;
		if(cc != null) {
			cid = Integer.parseInt(cc);
		}
		//传递获得的参数，从database中获取源代码
		//在此处获取源代码的目的是：通过传递少量的数据，减少主server的带宽占用
		//使高配的database抗高压
		
		judgeNode(sid,pid,cid,lang);		
	}
	/*
	 * JudgeNode
	 * 每个JudgeNode只会访问/修改 三次数据库
	 * 1.获取源程序
	 * 2.修改客户端将会轮询的judge_result表(缩写--jr表)
	 * 3.修改solution表
	 * */
	public static void judgeNode(int sid,int pid,int cid,int lang) {
		int m = 2;//machine 分布式判题机编号
		//get Source Code
		sourceCodeDao sd = new sourceCodeDaoImpl();
		jrDao jrdao = new jrDaoImpl();
		solutionDao sld = new solutionDaoImpl();
		
		SourceCode sc = sd.getCode(sid);
		//create Object
		JudgeTask jt = new JudgeTask();
		JudgeResult jr = new JudgeResult();
		Judge judge = new Judge();
		//根据进度 修改judge_result 表中的状态 status码  +  node 判题机编号
		//0 已提交(默认) 1判题ing 2 完成
		jrdao.change(sid,1,m);
		//修改判题状态为 1 判题ing
		jt.setCode(sc.getSoure());
		jt.setLanguage(lang);
		jt.setTime_limit(sc.getTime());
		jt.setMemory_limit(sc.getMemory());
		jt.setSolution_id(sid);
		jt.setProblem_id(pid);
		jt.setContest_id(cid);
		//传入判题对象，开始判题
		jr = judge.judge(jt);
		//判题结束，修改solution表
		sld.changeS(sid, jr.getTime(), jr.getMemory(), jr.getStatus());
		//修改判题状态为 2 完成
		jrdao.change(sid,2,m);
	}
}
