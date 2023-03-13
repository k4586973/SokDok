<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Join page</title>

<script type="text/javascript" src ="js/index.js"></script>
<link rel="stylesheet" href="Join.css" />

<script type="text/javascript">
	function loadFile(input){
		if(input.files && input.files[0]){
			var fr = new FileReader();
			
			fr.onload=function(event){
				document.getElementById('preview').src=event.target.result;
			};
			fr.readAsDataURL(input.files[0]);
			
		}
		else{
			document.getElementById('preview').src="";
		}
	}
</script>
</head>

<body>
 
<form class="box" method="post">


	<h1>회원가입</h1>
	<h3>프로필</h3>


	<div>
	 <img id="preview" src = "./person.svg" class="radius_img_1" 
	 style="width: 200px; height: 200px">
	</div>
	
	<div align= "center">
	<label for="btnforfileupload" class="btn btn-primary">프로필 변경</label>
	 
	<input type="file" accept="image/jpeg,image/gif,image/png"

	id="btnforfileupload"  onchange="loadFile(this)"  style="display: none;">

	</div>
			


		<input id = "id" type="text" placeholder="아이디를 입력하세요" autocomplete="off" />

		<input id = "pw" type="password" placeholder="비밀번호를 입력하세요" autocomplete="off" />

		<input id = "r_pw" type="password" placeholder="비밀번호를 다시 입력하세요" autocomplete="off" />

	
	
	<div class = "options">
		<input type="button" value="회원가입"  onclick="create_id();" />
		&emsp; &emsp; &emsp;&emsp; &emsp; 
		<input type="button" value="취소"  onclick="back();" />
	</div>


	

</form>

</body>
</html>


