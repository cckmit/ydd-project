
function downloadPdf(title){
    // var element = $('#pdfDom');    // 这个dom元素是要导出pdf的div容器
    // var w = element.width();    // 获得该容器的宽
    // var h = element.height();    // 获得该容器的高
    // var offsetTop = element.offset().top;    // 获得该容器到文档顶部的距离
    // var offsetLeft = element.offset().left;    // 获得该容器到文档最左的距离
    // var canvas = document.createElement("canvas");
    // var abs = 0;
    // var win_i = $(window).width();    // 获得当前可视窗口的宽度（不包含滚动条）
    // var win_o = window.innerWidth;    // 获得当前窗口的宽度（包含滚动条）
    // if(win_o>win_i){
    //     abs = (win_o - win_i)/2;    // 获得滚动条长度的一半
    // }
    // canvas.width = w * 2;    // 将画布宽&&高放大两倍
    // canvas.height = h * 2;
    // var context = canvas.getContext("2d");
    // context.scale(2, 2);
    // context.translate(-offsetLeft-abs,-offsetTop);
    // 这里默认横向没有滚动条的情况，因为offset.left(),有无滚动条的时候存在差值，因此
    // translate的时候，要把这个差值去掉
    html2canvas(document.querySelector('#pdfDom'), {
        allowTaint: true,
        useCORS: true,
        scale:1.5,
        //canvas : canvas,
        background:"#ffffff",//设置背景色，避免打印iframe出现黑底
        //width: width,
        //height: height,
    }).then(function (canvas) {
        //获取canvas画布的宽高
        var contentWidth = canvas.width;
        var contentHeight = canvas.height;
        //一页pdf显示html页面生成的canvas高度;
        var pageHeight = contentWidth /841.89  * 592.28;
        //未生成pdf的html页面高度
        var leftHeight = contentHeight;
        //页面偏移
        var position = 841.89/5;
        //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
        //var imgWidth = 841.89;
        var imgWidth = 800;
        var imgHeight = 841.89/contentWidth * contentHeight;
        var pageData = canvas.toDataURL();
        var pdf = new jsPDF('l', 'pt', 'a4');
        //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
        //当内容未超过pdf一页显示的范围，无需分页
        if (leftHeight < pageHeight) {
            pdf.addImage(pageData, 'JPEG', 21,position, imgWidth, imgHeight );
        }else {
            while(leftHeight > 0) {
                pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                leftHeight -= pageHeight;
                position -= 592.28;
                //避免添加空白页
                if(leftHeight > 0) {
                    pdf.addPage();
                }
            }
        }
        pdf.save(title+".pdf");


    })
}

