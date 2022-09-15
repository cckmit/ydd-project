function createPdf(htmlId) {
	//export2Excel(this.columns, this.fileData, '123');
	// exportToExcel($("#content").html(), "整改详情");
	if (htmlId == null) {
		htmlId = 'app';
	}
	html2canvas(document.getElementById(htmlId), {
		dpi: 300,
		// allowTaint: true,  //允许 canvas 污染， allowTaint参数要去掉，否则是无法通过toDataURL导出canvas数据的
		useCORS: true  //允许canvas画布内 可以跨域请求外部链接图片, 允许跨域请求。
	}).then((canvas) => {
		console.log(canvas)
		var contentWidth = canvas.width;
		var contentHeight = canvas.height;
		//一页pdf显示html页面生成的canvas高度;
		var pageHeight = contentWidth / 595.28 * 841.89;
		//未生成pdf的html页面高度
		var leftHeight = contentHeight;
		//页面偏移
		var position = 0;
		//a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
		var imgWidth = 595.28;
		var imgHeight = 595.28 / contentWidth * contentHeight;
		var pageData = canvas.toDataURL('image/jpeg', 1.0);
		var pdf = new jsPDF('', 'pt', 'a4');
		//有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
		//当内容未超过pdf一页显示的范围，无需分页
		console.log('处理完画布高度：' + contentHeight)
		console.log('每页高度：' + pageHeight)
		if (leftHeight < pageHeight) {
			//在pdf.addImage(pageData, 'JPEG', 左，上，宽度，高度)设置在pdf中显示；
			pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight);
			// pdf.addImage(pageData, 'JPEG', 20, 40, imgWidth, imgHeight);
		} else {    // 分页
			while (leftHeight > 10) {
				console.log(leftHeight)
				pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight);
				leftHeight -= pageHeight;
				position -= 841.89;
				//避免添加空白页
				if (leftHeight > 10) {
					pdf.addPage();
				}
			}
		}
		//可动态生成
		var fileData = btoa(pdf.output());
		$.ajax({
			url: "/oa-wkflow/file/file/base64",  //后台接收地址
			data: {
				fileData: fileData,
			},
			type: 'post',
			async: false,
			success: function(res) {
				console.log(res)
			}
		});
	})
}