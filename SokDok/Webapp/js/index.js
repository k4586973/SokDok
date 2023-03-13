		function back() {
			location.href = 'index.html';
	}
	
		function create_id() {
		var id = document.querySelector('#id');
		var pw = document.querySelector('#pw');
		var r_pw = document.querySelector('#r_pw');
		
		if(id.value == "" || pw.value == "" || r_pw.value == ""){
			alert("회원가입을 할 수 없습니다. 다시 입력해주세요.")
		}else{
	
			if(pw.value !== r_pw.value){
				alert("비밀번호가 일치 하지 않습니다. 다시 입력해주세요.")
			}else{
				alert("회원가입이 완료되었습니다.");
				
				location.href = 'index.html';
			}
		}
	}