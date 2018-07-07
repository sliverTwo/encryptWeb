/**
 * 加密（需要先加载lib/aes/aes.min.js文件）
 * @param {String} text 需要加密的数据
 * @param {String} key aes密钥
 */
function AesEncrypt(text, key) {
	var key = CryptoJS.enc.Utf8.parse(key);
	var srcs = CryptoJS.enc.Utf8.parse(text);
	var encrypted = CryptoJS.AES.encrypt(text, key, {
		mode: CryptoJS.mode.ECB,
		padding: CryptoJS.pad.Pkcs7
	});
	return encrypted.toString();
}

/**
 * AES解密 （需要先加载lib/aes/aes.min.js文件）
 * @param {String} text Aes加密的数据
 * @param {String} key aes密钥
 */
function AesDecrypt(text, key) {
	console.log("---------function:" + arguments.callee.name + " excuted");
	var key = CryptoJS.enc.Utf8.parse(key);
	var decrypt = CryptoJS.AES.decrypt(text, key, {
		mode: CryptoJS.mode.ECB,
		padding: CryptoJS.pad.Pkcs7
	});

	console.log("aes解密后的数据" + CryptoJS.enc.Utf8.stringify(decrypt).toString());
	return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}

/**
 * RSA加密
 * @param {String} text 需要加密的数据
 * @param {String} publicKey 公钥
 * @return encryptText
 */
function RsaEncrypt(text, publicKey) {
	var encrypt = new JSEncrypt();
	encrypt.setPublicKey(publicKey);
	return encrypt.encrypt(text)
}

/**
 * RSA解密
 * @param {String} text 需要解密的数据
 * @param {String} privateKey 私钥
 */
function RsaDncrypt(text, privateKey) {
	var encrypt = new JSEncrypt();
	encrypt.setPrivateKey(privateKey);
	return encrypt.decrypt(text)
}

/**
 * 获取md5
 * @param {String} text
 */
function MD5(text) {
	return CryptoJS.MD5(text).toString(CryptoJS.enc.Hex).toUpperCase()
}

/**
 * Base64加密
 * @param {Object} text
 */
function BASE64Encrypt(text) {
	var wordArray = CryptoJS.enc.Utf8.parse(text);
	return CryptoJS.enc.Base64.stringify(wordArray);
}

/**
 * Base64解密
 * @param {Object} text
 */
function BASE64Decrypt(text) {
	var parsedWordArray = CryptoJS.enc.Base64.parse(text);
	return parsedWordArray.toString(CryptoJS.enc.Utf8);
}

Date.prototype.format = function(fmt) { //author: meizz 
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"H+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if(/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}