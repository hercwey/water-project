/*
 * 选择上传图片时，页面显示图像<img> id设置为"portrait"
 */
function showPreview(source, portraitId) {
	var file = source.files[0];
	if (window.FileReader) {
		var fr = new FileReader();
		fr.onloadend = function(e) {
			document.getElementById(portraitId).innerHTML = "<img src='" + e.target.result + "' height='100px;'>";
		};
		fr.readAsDataURL(file);
	}
}
/*
 * 显示多个预览图片
 */
function showMutiPreview(source, portraitId) {

	document.getElementById(portraitId).innerHTML = "";

	function readAndPreview(file) {
		// Make sure `file.name` matches our extensions criteria
		if (/\.(jpe?g|png|gif)$/i.test(file.name)) {
			var reader = new FileReader();
			reader.addEventListener("load", function() {
				var image = new Image();
				image.height = 100;
				image.title = file.name;
				image.src = this.result;
				document.getElementById(portraitId).appendChild(image);
				document.getElementById(portraitId).innerHTML += "&nbsp;&nbsp;";
			}, false);
			reader.readAsDataURL(file);
		}
	}

	if (source.files) {
		[].forEach.call(source.files, readAndPreview);
	}

}

// 上传图片时，form提交前验证文件大小
var fileValidator = function() {
	// 表单提交前做表单验证
	var file = document.getElementById("file").files[0];
	var fileSize = 0;
	// 大小 字节
	if (file == null) {
		// alert("请选择上传文件");
		// return;
	} else {
		fileSize = file.size;
		fileSize = Math.round(fileSize / 1024 * 100) / 100; // 单位为KB
		if (fileSize >= 10240) {
			alert("照片最大尺寸为10MB，请重新上传!");
			return false;
		}
	}

};
/*
 * 选择上传文件时验证文件大小
 * value:文件
 * size:上传文件大小   单位为KB
 * prompt:提示
 */
function isAllowUploadFile(value, size, prompt){
	var allowUploadFile = false;
	var fileSize = value.files[0].size;
	fileSize = Math.round(fileSize / 1024 * 100) / 100; // 单位为KB
	if (fileSize >= size) {
		allowUploadFile = false;
		util.message(prompt);
	} else {
		allowUploadFile = true;
	}
	return allowUploadFile;
}

/*
 * 选择上传文件时验证文件大小（多个文件）
 * value:文件
 * size:上传文件大小   单位为KB
 * prompt:提示
 */
function verifyFilesSize(value, size, prompt){
	var flag = true;
	var files = value.files;
	for(var i=0; i<files.length; i++){
		var fileSize = value.files[i].size;
		fileSize = Math.round(fileSize / 1024 * 100) / 100; // 单位为KB
		if (fileSize >= size) {
			flag = false;
			util.message("第 "+i+" 张"+prompt);
			break;
		}
	}
	return flag;
}

/*
 * 得到富文件编辑器的内容
 * 		editorId:富文本编辑器的ID
 */
function getContent(editorId) {
    return UE.getEditor(editorId).getContent();
}
/*
 * 设置富文本编辑器的内容
 * 		editorId:富文本编辑器的ID
 * 		content:设置富文本编辑器内显示的内容
 */
function setContent(editorId, content) {
	var isAppendTo = false;
    UE.getEditor(editorId).setContent(content, isAppendTo);
	/*UE.getEditor(editorId).addListener("ready", function () {
		var isAppendTo = false;
	    UE.getEditor(editorId).setContent(content, isAppendTo);
	});*/
}