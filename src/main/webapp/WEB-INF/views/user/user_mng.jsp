<%--
/**
	Class Name: user_mng.jsp
	Description:
	Modification information
	
	수정일     수정자      수정내용
    -----   -----  -------------------------------------------
    2022. 6. 20.        최초작성 
    
    author eclass 개발팀
    since 2020.11.23
    Copyright (C) by KandJang All right reserved.
*/
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="CP" value="${pageContext.request.contextPath }"></c:set>
<c:set var="resources" value="/resources"></c:set>
<c:set var="CP_RES" value="${CP}${resources}"></c:set>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>boot_list</title>
		<!-- favicon -->
		<link rel="shortcut icon" type="image/x-icon"
			href="${CP_RES}/favicon.ico">
		<!-- 부트스트랩 -->
		<link href="${CP_RES}/css/bootstrap.min.css" rel="stylesheet">
		<!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요합니다) -->
		<script src="${CP_RES}/js/jquery-1.12.4.js"></script>
		<!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
		<script src="${CP_RES}/js/bootstrap.min.js"></script>
		
		<!-- 사용자 정의 function, ISEmpty -->
        <script src="${CP_RES}/js/eUtil.js"></script>
        <!-- 사용자 정의  function, callAjax -->
		<script src="${CP_RES}/js/eclass.js"></script>
		
		<script type="text/javascript">
		   //-- $(document).ready
		   $(document).ready(function() {
				console.log("document.ready!");
				
				// progress hide
				$("#loading").hide();
				
				$("#idCheck").on("click", function(){
					console.log("#idCheck Btn Clicked!");
					
					if(eUtil.ISEmpty($("#uId").val())){
						   alert("아이디를 입력하세요.");
						   $("#uId").focus();
						   return;
					}
					
					let url = "${CP}/user/idCheck.do";
                    let parameters = {
                            "uId": $("#uId").val()
                    };
                    let method = "GET";
                    let async;
                    
                    EClass.callAjax(url, parameters, method, async, function(data) {
                    	console.log('data: '+data);
                    	
                    	if(data.msgId == 0){
                    	// id 사용 가능
                    	    alert(data.msgContents);
                    	    $("#idCheckYN").val("1");
                    	}
                    	else{
                    	// id 사용 불가능
                    		alert(data.msgContents);                    		
                    		$("#idCheckYN").val("0");
                    	}
                    })
				});
				
				// 자체적인 속성을 넣은 경우
                /* $("input:text[numberOnly]").on("keydown", function(e){ */
                // 클래스에 numberOnly
                $(".numberOnly").on("keydown", function(e){
                	console.log("$(this).val(): "+$(this).val());
                	
                	$(this).val($(this).val().replace(/[^0-9]/g, ""));
                });
				
				// 검색어 Enter
				$("#searchWord").on("keypress", function(e){
				    console.log("searchWord" + e.which);
				    
				    if(e.which == 13){
				    	e.preventDefault();
				    	doRetrieve(1);
				    }
				    	
				});
				
				// 등록
				$("#add").on("click", function(){
					console.log("add");
					
					if(eUtil.ISEmpty($("#uId").val())){
                        alert("id값을 입력하세요.");
                        $("#uId").focus
                    }
                    
                    if(eUtil.ISEmpty($("#passwd").val())){
                        alert("passwd값을 입력하세요.");
                        $("#passwd").focus();
                        return;
                    }
                    
                    if(eUtil.ISEmpty($("#login").val())){
                        alert("login값을 입력하세요.");
                        $("#login").focus();
                        return;
                    }
                    
                    if(eUtil.ISEmpty($("#recommend").val())){
                        alert("recommend값을 입력하세요.");
                        $("#recommend").focus();
                        return;
                    }
                    
                    if(eUtil.ISEmpty($("#email").val())){
                        alert("email값을 입력하세요.");
                        $("#email").focus();
                        return;
                    }
					
					if(confirm("등록 하시겠습니까?") == false) return;
					
					let url = "${CP}/user/add.do";
                    let parameters = {
                            "uId": $("#uId").val(),
                            "name": $('#name').val(),
                            "passwd": $('#passwd').val(),
                            "intLevel": $('#intLevel').val(),
                            "login": $('#login').val(),
                            "recommend": $('#recommend').val(),
                            "email": $('#email').val()
                    };
                    let method = "POST";
                    let async;
                    
                    EClass.callAjax(url, parameters, method, async, function(data) {
                    	console.log('data.msgId: '+data.msgId);
                        console.log('data.msgContents: '+data.msgContents);
                        
                        if(data.msgId == "1"){
                            // 수정이 성공했을 경우 alert로 수정됐다는 메시지 출력 이후 목록 조회 및 하단 초기화
                            alert(data.msgContents);
                            // 목록 조회
                            // trigger를 이용한 호출
                            //$("#doRetrieve").trigger("click");
                            doRetrieve(1);
                            // 하단 초기화
                            init();
                        }else alert(data.msgContents);
                    })
				});
				
				// 목록 검색 함수
				function doRetrieve(page){
				    console.log("doRetrieve");
                    
                    let url        = "${CP}/user/doRetrieve.do";
                    let method     = "GET";
                    let async      = true;
                    let parameters = {
                            searchDiv: $("#searchDiv").val(),
                            searchWord: $("#searchWord").val(),
                            pageSize: $("#pageSize").val(),
                            pageNum: page
                    };
                    
                    console.log("url: "+url);
                    console.log("method: "+method);
                    console.log("async: "+async);
                    console.log("parameters: "+parameters);
                    
                    let paramArray = Object.keys(parameters);
                    if(paramArray.length > 0){
                        for(let i = 0; i < paramArray.length; i++){
                            console.log(paramArray[i] + ": "+parameters[paramArray[i]]);
                        }
                    }

                    EClass.callAjax(url, parameters, method, async, function(data){
                        // 1. 기존 table 데이터 삭제
                        // 2. 새로운 데이터 table에 입력
                        console.log("Eclass.callAjax data: "+data);
                        
                        let parsedData = data;
                        // [1] 기존 table 데이터 삭제
                        $("#user_table > tbody").empty();
                        
                        let htmlData = "";
                        
                        // 데이터가 있는 경우
                        if(parsedData != null && parsedData.length > 0){
                            $.each(parsedData, function(index, value){
                                console.log(index + ", " + value.num + ", " + value.name);
                                
                                htmlData += "<tr>                                                                     ";
                                htmlData += "     <td class='text-center col-sm-1 col-md-1 col-lg-1'>"+value.num+"</td>   ";
                                htmlData += "     <td class='text-left   col-sm-2 col-md-2 col-lg-2'>"+value.uId+"</td>   ";
                                htmlData += "     <td class='text-left   col-sm-2 col-md-2 col-lg-2'>"+value.name+"</td>  ";
                                htmlData += "     <td class='text-center col-sm-2 col-md-2 col-lg-2'>"+value.level+"</td> ";
                                htmlData += "     <td class='text-left   col-sm-3 col-md-3 col-lg-3'>"+value.email+"</td> ";
                                htmlData += "     <td class='text-center col-sm-2 col-md-2 col-lg-2'>"+value.regDt+"</td> ";
                                htmlData += "</tr>                                                                    ";
                            }); 
                        }else{
                        // 데이터가 없는 경우   
                            htmlData += "<tr><td colspan='99' class='text-center'>no data found</td></tr>";
                        }
                        
                        // [2] 새로운 데이터 table에 입력
                        $("#user_table > tbody").append(htmlData);
                        
                        //     [2-1] 관리 데이터 초기화
                        init();
                        })
				};
				
				// 수정
				$("#doUpdate").on("click", function(){
					console.log('#doUpdate button Clicked!');
					
					console.log("eUtil.ISEmpty($('#uId').val()): "+eUtil.ISEmpty($("#uId").val()));
					
					if(eUtil.ISEmpty($("#uId").val())){
						alert("id값을 입력하세요.");
						$("#uId").focus
					}
					
					if(eUtil.ISEmpty($("#passwd").val())){
                        alert("passwd값을 입력하세요.");
                        $("#passwd").focus();
                        return;
                    }
					
					if(eUtil.ISEmpty($("#login").val())){
                        alert("login값을 입력하세요.");
                        $("#login").focus();
                        return;
                    }
					
					if(eUtil.ISEmpty($("#recommend").val())){
                        alert("recommend값을 입력하세요.");
                        $("#recommend").focus();
                        return;
                    }
					
					if(eUtil.ISEmpty($("#email").val())){
                        alert("email값을 입력하세요.");
                        $("#email").focus();
                        return;
                    }
					
					if(confirm("수정 하시겠습니까?") == false) return;
					
					let url = "${CP}/user/doUpdate.do";
                    let parameters = {
                            "uId": $("#uId").val(),
                            "name": $('#name').val(),
		                    "passwd": $('#passwd').val(),
		                    "intLevel": $('#intLevel').val(),
		                    "login": $('#login').val(),
		                    "recommend": $('#recommend').val(),
		                    "email": $('#email').val()
                    };
                    let method = "POST";
                    let async = true;
                    
                    EClass.callAjax(url, parameters, method, async, function(data) {
                    	console.log('data.msgId: '+data.msgId);
                    	console.log('data.msgContents: '+data.msgContents);
                    	
                    	if(data.msgId == "1"){
                    		// 수정이 성공했을 경우 alert로 수정됐다는 메시지 출력 이후 목록 조회 및 하단 초기화
                    		alert(data.msgContents);
                    		// 목록 조회
                    		// trigger를 이용한 호출
                    		//$("#doRetrieve").trigger("click");
                    		doRetrieve(1);
                    		// 하단 초기화
                    		init();
                    	}else alert(data.msgContents);
                    	
                    });
				});
				
				// 삭제
				$("#doDelete").on("click", function(){
					console.log("#doDelete button Clicked!");
					
					if(eUtil.ISEmpty($("#uId").val())){
                        alert("id값을 입력하세요.");
                        $("#uId").focus();
                        return;
                    }
                    
					let url = "${CP}/user/doDelete.do";
					let parameters = {
							"uId": $("#uId").val()
					};
					let method = "GET";
					let async = true;
					
					if(confirm("삭제 하시겠습니까?") == false) return;
					
					EClass.callAjax(url, parameters, method, async, function(data) {
						console.log('data: '+data);
						
						if(data.msgId == "1"){
							alert(data.msgContents);
							
							doRetrieve(1);
							
							init();
						}else alert(data.msgContents);
					});
				});
				
				// 관리 초기화
				function init(){
				    const initValue = "";
                    
                    $('#uId').val(initValue);
                    $('#name').val(initValue);
                    $('#passwd').val(initValue);
                    $('#intLevel').val(1);
                    $('#login').val(initValue);
                    $('#recommend').val(initValue);
                    $('#email').val(initValue);
                    $('#regDt').val(initValue);
				}
				
				// initBtn click event----------------------
				$("#initBtn").on("click", function(){
					console.log('$initBtn Clicked!');
				    
					init();
					// uId의 disabled 속성 비활성화
					$('#uId').prop("disabled", false);
				// initBtn click event end-------------------
				});
				
				// table click event
				$("#user_table > tbody").on("click", "tr", function(e){
					console.log('#user_table > tbody > tr clicked!');
					let tds = $(this).children();
					let uId = tds.eq(1).text();
					console.log('uId:' + uId);
					
					let url        = "${CP}/user/doSelectOne.do";
					let method     = "GET";
					let async      = true;
					let parameters = {"uId": uId}
					
					EClass.callAjax(url, parameters, method, async, function(data){
						console.log('data.name: '+data.name);
						console.log('data.intLevel: '+data.intLevel);
						$('#uId').val(data.uId);
						$('#name').val(data.name);
						$('#passwd').val(data.passwd);
						$('#intLevel').val(data.intLevel);
						$('#login').val(data.login);
						$('#recommend').val(data.recommend);
						$('#email').val(data.email);
						$('#regDt').val(data.regDt);
						
						// 사용하지 못함으로 처리
						$('#uId').prop("disabled", "disabled");
					});
				});
				
				// doRetrieve
				$("#doRetrieve").on("click", function(e){
				    console.log("doRetrieve");
				    
				    let url        = "${CP}/user/doRetrieve.do";
				    let method     = "GET";
				    let async      = true;
				    let parameters = {
				    		searchDiv: $("#searchDiv").val(),
				    		searchWord: $("#searchWord").val(),
				    		pageSize: $("#pageSize").val(),
				    		pageNum: 1
				    };
				    
				    console.log("url: "+url);
				    console.log("method: "+method);
				    console.log("async: "+async);
				    console.log("parameters: "+parameters);
				    
				    let paramArray = Object.keys(parameters);
				    if(paramArray.length > 0){
				    	for(let i = 0; i < paramArray.length; i++){
				    		console.log(paramArray[i] + ": "+parameters[paramArray[i]]);
				    	}
				    }

				    EClass.callAjax(url, parameters, method, async, function(data){
                        // 1. 기존 table 데이터 삭제
                        // 2. 새로운 데이터 table에 입력
				    	console.log("Eclass.callAjax data: "+data);
				    	
				    	let parsedData = data;
				    	// [1] 기존 table 데이터 삭제
				    	$("#user_table > tbody").empty();
				    	
				    	let htmlData = "";
				    	
				    	// 데이터가 있는 경우
                        if(parsedData != null && parsedData.length > 0){
                            $.each(parsedData, function(index, value){
                                console.log(index + ", " + value.num + ", " + value.name);
                                
                                htmlData += "<tr>                                                                     ";
                                htmlData += "     <td class='text-center col-sm-1 col-md-1 col-lg-1'>"+value.num+"</td>   ";
                                htmlData += "     <td class='text-left   col-sm-2 col-md-2 col-lg-2'>"+value.uId+"</td>   ";
                                htmlData += "     <td class='text-left   col-sm-2 col-md-2 col-lg-2'>"+value.name+"</td>  ";
                                htmlData += "     <td class='text-center col-sm-2 col-md-2 col-lg-2'>"+value.level+"</td> ";
                                htmlData += "     <td class='text-left   col-sm-3 col-md-3 col-lg-3'>"+value.email+"</td> ";
                                htmlData += "     <td class='text-center col-sm-2 col-md-2 col-lg-2'>"+value.regDt+"</td> ";
                                htmlData += "</tr>                                                                    ";
                            }); 
                        }else{
                        // 데이터가 없는 경우   
                            htmlData += "<tr><td colspan='99' class='text-center'>no data found</td></tr>";
                        }
				    	
                        // [2] 새로운 데이터 table에 입력
                        $("#user_table > tbody").append(htmlData);
                        
                        //     [2-1] 관리 데이터 초기화
                        init();
				    });
				    
				//-- doRetrieve
				});
				
				//-- $(document).ready
		   });
		   
		</script>
	</head>
	<body>
		<!-- div container ----------------------------------------------------->
		<div class="container">
			<!-- 제목 ------------------------------------------------------------->
			<div class="page-header">
				<h2>회원 관리</h2>
			</div>
			<!--// 제목 ----------------------------------------------------------->
	
			<!-- 검색영역 -->
			<div class="row">
				<form action="#"
					class="form-inline col-sm-12 col-md-12 col-lg-12 text-right">
					<div class="form-group">
						<select class="form-control input-sm" name="searchDiv" id="searchDiv">
							<option value="">전체</option>
							<option value="10">아이디</option>
							<option value="20">이름</option>
							<option value="30">이메일</option>
						</select> 
						<input type="text" class="form-control input-sm" placeholder="검색어" name="searchWord" id="searchWord" /> 
						<select class="form-control input-sm" name="pageSize" id="pageSize">
							<option value="10">10</option>
							<option value="20">20</option>
							<option value="30">30</option>
							<option value="50">50</option>
							<option value="100">100</option>
						</select> 
						<input id="doRetrieve" type="button" class="btn btn-primary btn-sm" value="목록" />
					</div>
				</form>
	
			</div>
			<!--// 검색영역 ----------------------------------------------------------->
	
	
			<!-- table -->
			<div class="table-responsive">
				<table id="user_table" class="table table-striped table-bordered table-hover table-condensed">
					<!-- 문자: 왼쪽, 숫자: 오른쪽, 같은면: 가운데 -->
					<thead class="bg-primary">
						<tr>
							<th class="text-center col-sm-1 col-md-1 col-lg-1">번호</th>
							<th class="text-center col-sm-2 col-md-2 col-lg-2">아이디</th>
							<th class="text-center col-sm-2 col-md-2 col-lg-2">이름</th>
							<th class="text-center col-sm-2 col-md-2 col-lg-2">등급</th>
							<th class="text-center col-sm-3 col-md-3 col-lg-3">이메일</th>
							<th class="text-center col-sm-2 col-md-2 col-lg-2">등록일</th>
						</tr>
					</thead>
					<tbody>
					   <c:choose>
					       <%-- data가 있는 경우 --%>
					       <c:when test="${list.size()>0}">
					           <c:forEach var="vo" items="${list }">
						           <tr>
	                                    <td class="text-center col-sm-1 col-md-1 col-lg-1">${vo.num }</td>
	                                    <td class="text-left   col-sm-2 col-md-2 col-lg-2">${vo.uId }</td>
	                                    <td class="text-left   col-sm-2 col-md-2 col-lg-2">${vo.name }</td>
	                                    <td class="text-center col-sm-2 col-md-2 col-lg-2">${vo.level }</td>
	                                    <td class="text-left   col-sm-3 col-md-3 col-lg-3">${vo.email }</td>
	                                    <td class="text-center col-sm-2 col-md-2 col-lg-2">${vo.regDt }</td>
	                               </tr>
                               </c:forEach>
					       </c:when>
					       <%-- data가 없는 경우 --%>
					       <c:otherwise>
					           <tr><td colspan="99" class="text-center">no data found</td></tr>
					       </c:otherwise>
					   </c:choose>
						
					</tbody>
				</table>
			</div>
			<!--// tabble ----------------------------------------------------------->
		</div>
		<!--// div container ------------------------------------------------------->
	
		<!-- div container --------------------------------------------------------->
		<div class="container">
			<!-- 버튼 ------------------------------------------------------------->
			<div class="row text-right">
				<label class="col-sm-3 col-md-2 col-lg-2"></label>
				<div class="col-sm-9 col-md-10 col-lg-10">
					<input type="reset" class="btn btn-primary btn-sm" value="초기화" id="initBtn" /> <input
						type="button" class="btn btn-primary btn-sm" value="수정" id="doUpdate" /> <input
						type="button" class="btn btn-primary btn-sm" value="삭제" id="doDelete" /> <input
						type="button" class="btn btn-primary btn-sm" value="등록" id="add" />
				</div>
			</div>
			<!--// 버튼 ------------------------------------------------------------->
	
			<!-- form -------------------------------------------------------------->
			<!-- <form action="" class="form-horizontal"> -->
			<form action="" class="">
			    <input type="hidden" id="idCheckYN" class="idCheckYN"/>
				<div class="form-group">
					<label for="uId" class="col-sm-2 col-md-1 col-lg-1 control-label">아이디</label>
					<div class="col-sm-9 col-md-10 col-lg-10">
						<input type="text" maxlength="20" name="uId" id="uId" placeholder="아이디" class="form-control">
					</div>
					   <input id="idCheck" class="col-sm-1 col-md-1 col-lg-1 btn btn-primary btn-sm" type="button" value="중복 확인">
				</div>
				<div class="form-group">
                    <label for="name" class="col-sm-3 col-md-2 col-lg-2 control-label">이름</label>
                    <div class="col-sm-9 col-md-10 col-lg-10">
                        <input type="text" maxlength="20" name="name" id="name" placeholder="이름" class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label for="passwd" class="col-sm-3 col-md-2 col-lg-2 control-label">비밀번호</label>
                    <div class="col-sm-9 col-md-10 col-lg-10">
                        <input type="password" maxlength="20" name="passwd" id="passwd" placeholder="비밀번호" class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label for="intLevel" class="col-sm-3 col-md-2 col-lg-2 control-label">등급</label>
                    <div class="col-sm-9 col-md-10 col-lg-10">
                        <select name="intLevel" id="intLevel" class="form-control">
                            <option value="1">BASIC</option>
                            <option value="2">SILVER</option>
                            <option value="3">GOLD</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="login" class="col-sm-3 col-md-2 col-lg-2 control-label">로그인</label>
                    <div class="col-sm-9 col-md-10 col-lg-10">
                        <input type="text" maxlength="8" name="login" id="login" placeholder="로그인" class="form-control numberOnly">
                    </div>
                </div>
                <div class="form-group">
                    <label for="recommend" class="col-sm-3 col-md-2 col-lg-2 control-label">추천</label>
                    <div class="col-sm-9 col-md-10 col-lg-10">
                        <input type="text" maxlength="8" name="recommend" id="recommend" placeholder="추천" class="form-control numberOnly">
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-sm-3 col-md-2 col-lg-2 control-label">이메일</label>
                    <div class="col-sm-9 col-md-10 col-lg-10">
                        <input type="text" maxlength="320" name="email" id="email" placeholder="이메일" class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label for="regDt" class="col-sm-3 col-md-2 col-lg-2 control-label">등록일</label>
                    <div class="col-sm-9 col-md-10 col-lg-10">
                        <input type="text" name="regDt" id="regDt" placeholder="등록일" class="form-control" readonly="readonly">
                    </div>
                </div>
			</form>
		</div>
	</body>
</html>