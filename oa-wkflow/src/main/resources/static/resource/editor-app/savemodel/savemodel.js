function checkModelKey(field, modelId){
	var host = document.domain;
	if(host.indexOf('localhost')>=0){
		host = 'http://localhost:17210/oa-wkflow';
	}else{
		host = '/oa-wkflow';
	}
	var msg = '';
	jQuery.ajax({
		url:host + '/check_define_key',
		data:{modelId:modelId, key: field },
		async:false,
		dataType:'json',
		success:function(data){
			msg = data.result;
		}
	})
	return msg;
}