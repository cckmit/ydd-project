//注入高级代码js,补充画表单不成功问题
function rejectAdvanceJs(json,json_eval){
	if(!json_eval){
		return json;
	}
	console.log(json_eval);
	json_eval = JSON.parse(json_eval);
	console.log(json_eval);
	if(!json_eval){
		return json;
	}
	if(json_eval.column){
		combineColumn(json.column,json_eval.column);
	}
	console.log(json);
	return json;
}
function combineColumn(columnA,columnB){
	if(!columnA){
		columnA = {};
	}
	for(var i=0;i<columnB.length;i++){
		var objB = columnB[i];
		var propB = objB.prop;
		if(!propB){continue;}
		for(j=0;j<columnA.length;j++){
			var objA = columnA[j];
			var propA = objA.prop;
			if(!propA){continue;}
			if(propA==propB){
				for(field in objB){
					objA[field] = objB[field];
					if(objA[field].startsWith("function")){
						//处理函数转换
						objA[field] = eval("objA."+field+"="+objA[field]);
					}
				}
			}
		}//# for j
	}//# for i
	return columnA;
}
