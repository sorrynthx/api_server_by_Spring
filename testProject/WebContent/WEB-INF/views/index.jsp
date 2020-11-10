<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>test Project</title>
<body>
	test Project 프로젝트 <br><br><br>
	
	${testList} 		<br><br><br>
	${testObject}		<br><br><br>
	
	<h2>testList</h2>
	<div id="testList"></div>	<br><br>
	
	<h2>testObject</h2>	
	<div id="testObject"></div>
</body>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
var obj = JSON.parse('${testObject}');
var list = JSON.parse('${testList}');

for (var i=0; i<list.length; i++) {
	$('#testList').text(list[i].test + ' / ' + list[i].value + ' / ' + list[i].message);
}

$('#testObject').text(obj.value + ' / ' + obj.value + ' / ' + obj.message);

</script>
</html>