<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>



</head>
<body>
<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	/* 받는 문자 보내는 문자 UTF-8로 인코딩하라 */

	HashMap<String,String> member = new HashMap<String,String>();
	member.put("test", "1234");				// key value로 묶는다.
	//test 아이디에 1234 비번을 저장
	/* 데이터 베이스가 없으니 임시로 화원정보를 가지고 있는다. */
	
	
	String log_id = request.getParameter("id");
	String log_pw = request.getParameter("pw");

	if(member.get(log_id)!= null){
			//get(key) = value
			//즉, [get(입력_id)==>(id의 비번)]이 null이 아니면
	
			if(member.get(log_id).equals(log_pw)){//아이디와 비번 일치 확인
				//(id의 비번).equals(입력 비번)
				response.sendRedirect("chatMain.jsp");
				/* 아이디와 비번이 맞다면 chat 사이트로 */
			}else{
				
				 response.sendRedirect("login_fail.html");
				/* 아니라면 실패 사이트로 */
				
			}
	}else{	
		response.sendRedirect("login_fail.html");
			/* response.sendRedirect("login_fail.html"); */
			/* 아니라면 실패 사이트로 */
	}
	

	
%>
</body>
</html>