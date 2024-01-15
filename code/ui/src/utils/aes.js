import CryptoJS from "crypto-js";

//首先声明两个变量，加密的时候要用到，要和后台沟通，保持一致
const AES_KEY = '0D7FB71E8EC31E97';

/**
 * 加密
 * @param word
 * @returns {*}
 */
export function encrypt(word){
  let key = CryptoJS.enc.Utf8.parse(AES_KEY);
  let srcs = CryptoJS.enc.Utf8.parse(word);
  let encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
  return encrypted.toString();
}

/**
 * 解密
 * @param word
 * @returns {*}
 */
export function decrypt(word){
  let key = CryptoJS.enc.Utf8.parse(AES_KEY);
  let decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
  return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}
