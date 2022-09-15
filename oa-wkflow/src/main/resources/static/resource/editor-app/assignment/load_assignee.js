// 双击源的 input框,之后赋值的时候使用
var selectInput = null,selectType=1,myWin = null, dbCK = false;

function load_assignee_show(input,type) {
	//设置源input
	dbCK = true;
	selectInput = input;
	selectType = type;
	// 弹出人员配置窗口
	myWin = window.open('/oa-wkflow/resource/editor-app/assignment/assignmentset.html?type='+selectType, "人员配置", "height=600, width=800, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no")
}

function loadAssignee(input,type){
	if(dbCK){
		return ;
	}
	var appElement = document.querySelector('[ng-controller=KisBpmAssignmentPopupCtrl]');
    var scope = angular.element(appElement).scope();
	if(selectType==1){
		scope.assignment.assignee=scope.assignment.assignee_zh;
	}else{
		scope.assignment.candidateUsers = scope.assignment.candidateUsers_zh;
	}
}

function setVal(ids,names){
	console.log(ids,names)
	var appElement = document.querySelector('[ng-controller=KisBpmAssignmentPopupCtrl]');
    var scope = angular.element(appElement).scope();
    var choseIds = ids.split(',');
    var num = choseIds.length;
    console.log(scope)
	if(selectType==1){
		scope.assignment.assignee=ids;
		scope.assignment.assignee_zh=names;
	}else{
		var users = [];
		for(var i=0; i<num; i++){
			users.push({
				value: choseIds[i]
			});
		}
		scope.assignment.candidateUsers=users;
		scope.assignment.candidateUsers_zh=names;
	}
    scope.$apply();
    
	myWin.close();
}
