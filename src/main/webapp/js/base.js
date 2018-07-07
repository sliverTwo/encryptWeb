/**
 *获取全局参数 
 */
function getGlobal(){
	var global = localStorage.getItem("global") || "{}";
	return JSON.parse(global);
}
function getToken() {
		var global = localStorage.getItem("global") || "{}";
		global = JSON.parse(global);
		var token = global.token || "";
		if(token.length > 1){
			try{
				console.log("aesKey :" + global.aesKey);
				console.log("token :" + global.token);
				var tk = JSON.parse(AesDecrypt(token,global.aesKey));
				if(tk.expirationTime == undefined || tk.expirationTime < Date.parse(new Date)){ //token已过期
					token = "";
					owner.setToken(token);
				}
			}catch(err){ //解密失败，将token置为空
				console.log("token解密失败");
				token = "";
				global.token = "";
				localStorage.setItem("global",JSON.stringify(global));
			}
		}
		return token;
	}
if(window.global == undefined) {
	window.global = getGlobal();
	if(window.global.aesKey == undefined || window.global.serverPublickey == undefined) {
		getbasicInfo();
		console.log("global:" + JSON.stringify(global));
	}
}
/** 错误编码 */
window.ERR_CODE = {
	/** aes密钥错误 */
	AES_ERR: "443",
	/** rsa密钥错误 */
	RSA_ERR: "444",
	/** 登录失效 */
	INVALID_LOGIN: "300"
}
/**
 * 获取全局基础信息
 */
function getbasicInfo() {
	console.log("function:" + arguments.callee.name + " excuted");
	console.log("global:" + JSON.stringify(global));
	global = {};
	global.publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI8o6c9SgJhaO/KJry4iSuq3LQmttX09KdU7rd2Qy/aJ5TAlSRYDC6BFyEVlcgqIL62ELIMxJxm1zwjYidrzaacCAwEAAQ==";
	global.privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAjyjpz1KAmFo78omvLiJK6rctCa21fT0p1Tut3ZDL9onlMCVJFgMLoEXIRWVyCogvrYQsgzEnGbXPCNiJ2vNppwIDAQABAkAr8Z4borKQkZo3L+ZTHfmrJMExaF3+bIjryF7tVSYWrfXfaUDOtactd4P04hT71OtVS3GQSv9xSsDN+aux/YY5AiEAwdkORo3SAct/mdqm/9iY0q6aJ+4C8KOTtUvrocFSom0CIQC9D2e877iHYUSnvMJ4E0sJO8ICs+SSxNnbOIXO8i0P4wIgFvJo3DVBnPDIBCB8EGFFhfshSITGWFIk1BtOo3FNJOkCIQCEMrt1L6K2d1tMdv78e3EimbiFr0iLID69vX5DQLAlxQIgf8EOisCkd2/6P5PwDgKKAnhQT/lxNE744elOrfx/n9o=";
	global.baseUrl = "http://192.168.0.18/m";
	localStorage.setItem("global", JSON.stringify(global));
	if(global.serverPublickey == undefined) {
		getRSAPublicKey();
	}
	//			plus.storage.setItem("global",JSON.stringify(global));

}

/**
 * 获取远程服务端的公钥
 */
function getRSAPublicKey() {
	console.log("function:" + arguments.callee.name + " excuted");
	mui.getJSON(global.baseUrl + "/basic/getRSAPublicKey", function(result) {
		if(result.code = "200") {
			console.log(JSON.stringify(result));
			var data = result.obj.data;
			var md5 = result.obj.md5;
			// 验证通过
			if(MD5(data) == md5) {
				var d = JSON.parse(data);
				console.log(data);
				console.log(d.publicKey);
				global.serverPublickey = d.publicKey;
				localStorage.setItem("global", JSON.stringify(global));
				getAesKey();
			}
		}
	});
}

/**
 * 获取aes密钥
 */
function getAesKey() {
	console.log("function:" + arguments.callee.name + " excuted");
	console.log(global);
	if(global.serverPublickey == undefined) {
		getRSAPublicKey();
	}
	var data = {};
	var encryPublicKey = RsaEncrypt(global.publicKey, global.serverPublickey);
	data.publicKey = encryPublicKey;
	var param = getParam(data, false);

//	console.log("serverPublickey：" + global.serverPublickey);
//	console.log("serverPublickey长度：" + global.serverPublickey.length);
//	console.log("publicKey：" + global.publicKey);
//	console.log("publicKey长度：" + global.publicKey.length);
//	console.log("加密后的key:" + encryPublicKey);

	mui.getJSON(global.baseUrl + "/basic/getAESKey", param, function(result) {
		resultHandler(result, function(data) {
			//				var data = res.obj.data;
			var d = data;
//			console.log("data:" + JSON.stringify(data));
//			console.log("privateKey:" + global.privateKey);
			var aesKey = RsaDncrypt(data.aesKey, global.privateKey);
			console.log("aesKey:" + data.aesKey);
			if(aesKey) {
				global.aesKey = aesKey;
				global.token = "";
				localStorage.setItem("global", JSON.stringify(global));
			} else {
				// 获取失败，重新获取或者做其它处理...
			}
		});
	});
	console.log(JSON.stringify(global));
}

/**
 * ajax结果处理函数，将一些异常状态统一处理，并将验证数据一致性，返回纯粹的数据（经过加密的）
 * @param {Object} result 返回的结果
 * @param {Function} successCallback 正常情况的回调
 */
function resultHandler(result, successCallback, errorCallback) {
	if(result.code == "200") { // ok
		if(result.obj != undefined) { // 有数据返回，进行MD5校验
			var data = result.obj.data;
			var md5 = result.obj.md5;
			if(MD5(data) == md5) { // 验证通过
				console.log("返回的数据:" + data);
				if(typeof(successCallback) == "function") {
					successCallback(JSON.parse(data));
				}
			} else { // 数据被篡改，验证失败
				console.log("数据被篡改");
				// 后续处理...
			}
		} else { // 直接回调
			if(typeof(successCallback) == "function") {
				successCallback(result);
			}
		}
	} else if(result.code == ERR_CODE.RSA_ERR) { // AES密钥错误
		// 重新获取密钥
		getRSAPublicKey();
	} else if(result.code = ERR_CODE.AES_ERR) { // RSA密钥错误
		// 给出提示
		//			plus.nativeUI.alert( result.msg, function(e){
		//				plus.runtime.restart();
		//			}, "提示", "重启");
		
		// 判断本地是否有密钥以及判断token是否存在
		var global = getGlobal();
		if(global.aesKey != undefined && getToken().length > 0){
			var data = {};
			data.aesKey = global.aesKey||"";
			var param = getParam(data,false);
			// 将数据发送至后台
			mui.post(global.baseUrl + '/basic/putAes',param,function(res){
					resultHandler(res);
				},'json'
			);
		}else{
			console.log("无效的aes");
			// 重新获取密钥
			getAesKey();
		}
		if(typeof(errorCallback) === "function") {
				errorCallback();
			} else {
				plus.nativeUI.toast(result.msg);
			}
	} else if(result.code = ERR_CODE.INVALID_LOGIN) {
		// 跳转到登陆页面
		plus.webview.getLaunchWebview().show("pop-in");
		// 给出提示
		plus.nativeUI.toast("登陆已过期,请重新登陆");
		// 清空token
		var global = getGlobal();
		global.token = "";
		localStorage.setItem("global", global);
	} else {
		console.log("其它异常");
		console.log(JSON.stringify(result));
		// 后续处理...
	}
}
/**
 * 
 * @param {Object} data 传输的数据
 * @param {Boolean} encryptFlag 是否需要加密，默认需要加密
 */
function getParam(data, encryptFlag) {
	data = data || {};
	var token = getToken();
	if(token.length > 0){
		data.token = token;
	}
	var param = {};
	if(encryptFlag == undefined) {
		encryptFlag = true;
	}
	if(encryptFlag == true) {
		param.isEncrypt = 1;
		param.data = AesEncrypt(JSON.stringify(data), window.global.aesKey);
	} else {
		param.isEncrypt = 0;
		param.data = JSON.stringify(data);
	}
	param.md5 = MD5(param.data);
	console.log("参数：" + JSON.stringify(param));
	return param
}

/**
 * 
 * @param {String} url 请求的地址
 * @param {Object} data 参数
 * @param {Boolean} isEncrypt 是否需要加密
 * @param {Function} success 回调函数
 */
function zwGetJson(url,data,isEncrypt,success){
	var param = getParam(data,isEncrypt);
	var g = getGlobal();
	g.baseUrl = g.baseUrl || "http://192.168.0.18/m";
	var url = g.baseUrl + url;
	mui.getJSON(url,param,function(result){
			resultHandler(result,success);
		}
	);
}
