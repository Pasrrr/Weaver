package Huatu;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class VtSdkDemo {
		public interface libVtExtAPI extends Library {
		libVtExtAPI INSTANTCE = (libVtExtAPI) Native.loadLibrary("C:\\Windows\\System32\\VtExtAPI64.dll", libVtExtAPI.class);
		int VtExtInitWithServer(String ip, int port, String appid, String appkey, int logFlag);
		int VtExtIsCrypt(String path, IntByReference isEnc);
		int VtExtEncryptFile(String srcPath, String destPath, int cryptMode, int domain);
		int VtExtDecryptFile(String src, String dest);
		int VtExtIsCryptHeader(byte[] bytes, IntByReference nHeadLength, LongByReference ulHeader);
		int VtExtGetEncryptHeader(int crypt, int domain, byte[] headerData, int length, LongByReference header);
		int VtExtEncryptBuffer(long header, long offset, byte[] buffer, int length);
		int VtExtDecryptBuffer(long header, long offset, byte[] buffer, int length);
		int VtExtDeleteHeader(long header);
		int VtExtOutsidePack(byte[] config, int len);
	}
	
	/*通过连接服务器校验注册*/
	public static int VtExtInitWithServer(String ip, int port, String appid, String appkey, int logFlag){
		int sdkReslut = libVtExtAPI.INSTANTCE.VtExtInitWithServer(ip, port, appid, appkey, logFlag);
		if (sdkReslut == 0) {
			System.out.println("激活成功");
		} else {
		    System.out.println("VtExtInitWithServer Error: "+ sdkReslut);
		}
		return sdkReslut;
	}
	
	/*根据文件路径判断是否是加密文件*/
	public static int VtExtIsCrypt(String path, IntByReference isEnc){
		int sdkReslut = libVtExtAPI.INSTANTCE.VtExtIsCrypt(path, isEnc);
		if (sdkReslut == 0) {
			if (isEnc.getValue() == 0) {
				System.out.println("当前文件不是加密文件");
			}
			else{
				System.out.println("当前文件是加密文件");
			}	
		} else {
		    System.out.println("VtExtIsCrypt Error: "+ sdkReslut);
		}
		return sdkReslut;
	}

	/*加密文件,需要设置加密算法版本和文件安全域*/
	public static int VtExtEncryptFile(String srcPath, String destPath, int cryptMode, int domain)	{
		int sdkReslut = libVtExtAPI.INSTANTCE.VtExtEncryptFile(srcPath, destPath, cryptMode, domain);
		if (sdkReslut == 0) {
			System.out.println("文件加密成功");
		} else {
		    System.out.println("VtExtEncryptFile Error: "+ sdkReslut);
		}
		return sdkReslut;
	}
	
	/*解密文件*/
	public static int VtExtDecryptFile(String srcPath, String destPath)	{
		int sdkReslut = libVtExtAPI.INSTANTCE.VtExtDecryptFile(srcPath, destPath);
		if (sdkReslut == 0) {
			System.out.println("文件解密成功");
		} else {
		    System.out.println("VtExtDecryptFile Error: "+ sdkReslut);
		}
		return sdkReslut;
	}
	
	/**
	* 调用加密头判断接口,判断文件流是否是加密头
	*
	* @param bytes
	* @param ulHeader
	* @return
	*/
	public static int VtExtIsCryptHeader(byte[] bytes, IntByReference nHeadLength, LongByReference ulHeader) {
		int result = -1;
		try {
		    System.out.println("开始调用isCryptHeader判断文件加密头");
		    result = libVtExtAPI.INSTANTCE.VtExtIsCryptHeader(bytes, nHeadLength, ulHeader);
		    if (result == 0) {
		        System.out.println("结束调用VtExtIsCryptHeader判断文件加密头");
				if (ulHeader.getValue() == 0)
				{
					 System.out.println("不是加密文件");
				}
				else
				{
					System.out.println("是加密文件");
				}
		    }
		    else {
		    	System.out.println("VtExtIsCryptHeader Error: "+ result);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	/**
	* 生成加密头接口
	* @param nEncryptType 加密算法
	* @param nFileLevel   安全域
	* @param nDataLength  长度
	* @param resultLength 记录根据加密算法和安全域生成的header
	* @return
	*/
	public static int VtExtGetEncryptHeader(int nEncryptType, int nFileLevel, byte[] fBuff, int nDataLength, LongByReference resultLength) {

		System.out.println("开始调用VtExtGetEncryptHeader");

		int result = 0;
		try {
			result = libVtExtAPI.INSTANTCE.VtExtGetEncryptHeader(nEncryptType, nFileLevel, fBuff, nDataLength, resultLength);
			if (result == 0) {
				System.out.println("VtExtGetEncryptHeader调用成功decryReslut:" + result);
				System.out.println("nDataLength:" + nDataLength);
				System.out.println("ulHeader:" + resultLength.getValue());
			} else {
				System.out.println("VtExtGetEncryptHeader Error: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	* 加密数据流接口
	*
	* @param ulHeader 加密头
	* @param offset   偏移量(不包含文件头)
	* @param buffer   所有要加密的数据
	* @param length   文件总长度
	* @return
	*/
	public static int VtExtEncryptBuffer(long ulHeader, long offset, byte[] buffer, int length) {
		int result = -1;
		try {
			result = libVtExtAPI.INSTANTCE.VtExtEncryptBuffer(ulHeader, offset, buffer, length);
			if (result == 0) {
				System.out.println("加密流成功");
			}
			else {
				System.out.println("VtExtEncryptBuffer Error: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	* 解密数据流接口
	*
	* @param ulHeader 加密头
	* @param offset   偏移量 从0开始
	* @param buffer   所有要加密的数据
	* @param length   每一次待加密的数据
	* @return
	*/
	public static int VtExtDecryptBuffer(long ulHeader, long offset, byte[] buffer, int length) {
		int result = -1;
		try {
			//待解密数据距离起始内容(加密头长度后的位置)的偏移
			result = libVtExtAPI.INSTANTCE.VtExtDecryptBuffer(ulHeader, offset, buffer, length);
			if (result == 0) {
				System.out.println("解密流成功");
			}
			else {
				System.out.println("VtExtDecryptBuffer Error: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	* 释放加密获取的header;
	*
	* @param ulHeader
	*/
	public static int VtExtDeleteHeader(long ulHeader) {
		int reslut = -1;
		try {
			reslut = libVtExtAPI.INSTANTCE.VtExtDeleteHeader(ulHeader);
			if (reslut == 0) {
				System.out.println("释放加密获取的header成功");
			}
			else{
				System.out.println("VtExtDeleteHeader Error: "+ reslut);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslut;
	}
	
	/*制作外发文件*/
	public static int VtExtOutsidePack(byte[] config, int len) 	{
		int sdkReslut = libVtExtAPI.INSTANTCE.VtExtOutsidePack(config, len);
		if (sdkReslut == 0) {
			System.out.println("外发文件制作成功");
		} else {
		    System.out.println("VtExtOutsidePack Error: "+ sdkReslut);
		}
		return sdkReslut;
	}
	
	public static void InitWithServer(){
        int result = VtExtInitWithServer("10.80.11.61", 9445, "F0C1918E769C202DC64E150EAAEA4AC2", "2A34A2F9-9AD5-4DDF-AF87-0B76A216EC33", 1);
        if(0 == result)
            ExecutionSteps();
    }
    
    public static void IsCryptFile(){
    	System.setProperty("jna.encoding","UTF-8");
    	System.out.println("请输入文件地址：");
    	Scanner pScan = new Scanner(System.in);
		String strRead = pScan.nextLine();
		IntByReference isEnc =  new IntByReference();
		int result = VtExtIsCrypt(strRead, isEnc);
		if(0 == result)
            ExecutionSteps();
    }
    
    public static void EncryptFile()    {
    	System.setProperty("jna.encoding","UTF-8");
        System.out.println("请输入明文文件地址：");
        Scanner pScan = new Scanner(System.in);
		String strRead = pScan.nextLine();
        String strFilePath = strRead;

        System.out.println("请输入生成文件地址：");
        strRead = pScan.nextLine();
        String strFilePathEn = strRead;

        int result = VtExtEncryptFile(strFilePath, strFilePathEn, 3, 0);
        if(0 == result)
            ExecutionSteps();
    }
    
    public static void DecryptFile()	{
    	System.setProperty("jna.encoding","UTF-8");
        System.out.println("请输入密文文件地址：");
        Scanner pScan = new Scanner(System.in);
		String strRead = pScan.nextLine();
        String strFilePath = strRead;

        System.out.println("请输入生成文件地址：");
        strRead = pScan.nextLine();
        String strFilePathDe = strRead;

        int result = VtExtDecryptFile(strFilePath, strFilePathDe);
        if(0 == result)
            ExecutionSteps();
    }
    
    public static void EncryptBuffer(){
    	System.out.println("请输入明文文件地址：");
        Scanner pScan = new Scanner(System.in);
		String strRead = pScan.nextLine();
        String strFilePath = strRead;

        System.out.println("请输入生成文件地址：");
        strRead = pScan.nextLine();
        String strFilePathEn = strRead;
        
        byte[] headBuff = new byte[1024];
        byte[] buffRes = new byte[1024];
        try{
        	FileInputStream fileStream = new FileInputStream(strFilePath);
			fileStream.read(buffRes, 0, 1024);
			fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntByReference nHeadLength = new IntByReference();
        nHeadLength.setValue(buffRes.length);
        LongByReference ulHeader = new LongByReference();
        //读取文件前面1024字节判断, 默认读取文件1024字节进行判断即可
		int result = VtExtIsCryptHeader(buffRes, nHeadLength, ulHeader);
		System.out.println(buffRes.length);
		if(0 != result || 0 != ulHeader.getValue())
			return;
		System.out.println("加密生成加密头开始");
		result = VtExtGetEncryptHeader(3, 0, headBuff, 1024, ulHeader);
		if( 0 != result)
			return;
		
		try{
			FileInputStream fs = new FileInputStream(strFilePath);
			FileOutputStream fse = new FileOutputStream(strFilePathEn);
			System.out.println("fs:"+fs);
			fse.write(headBuff, 0, 1024);
			byte[] data = new byte[4096];
			int offset = 0;
			
			int length = 0;
			while((length = fs.read(data, 0, 4096)) != -1){
				result = VtExtEncryptBuffer(ulHeader.getValue(), offset, data, length);
				if( 0 != result)
					return;
				fse.write(data, 0, length);
				offset += length;
			}
			System.out.println("length:"+length);
			VtExtDeleteHeader(ulHeader.getValue());
			fs.close();
			fse.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
        if( 0 == result)
        	ExecutionSteps();
    }
    
    public static void DecryptBuffer(){
		System.out.println("请输入密文文件地址：");
		Scanner pScan = new Scanner(System.in);
		String strRead = pScan.nextLine();
		String strFilePath = strRead;

		System.out.println("请输入生成文件地址：");
		strRead = pScan.nextLine();
		String strFilePathEn = strRead;
		
		byte[] headBuff = new byte[1024];
		byte[] buffRes = new byte[1024];
		try{
			FileInputStream fileStream = new FileInputStream(strFilePath);
			fileStream.read(buffRes, 0, 1024);
			fileStream.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		System.out.println(buffRes);
		System.out.println(buffRes.length);
		IntByReference nHeadLength = new IntByReference();
		nHeadLength.setValue(buffRes.length);
		LongByReference ulHeader = new LongByReference();
		System.out.println("nHeadLength:"+nHeadLength+";ulHeader:"+ulHeader);
		//读取文件前面1024字节判断, 默认读取文件1024字节进行判断即可
		int result = VtExtIsCryptHeader(buffRes, nHeadLength, ulHeader);
		if(0 != result)
			return;
		if(0 == ulHeader.getValue())
			return;
		try{
			FileInputStream fs = new FileInputStream(strFilePath);
			FileOutputStream fse = new FileOutputStream(strFilePathEn);
			//使得fs偏移1024，过滤加密头
			fs.read(headBuff, 0, 1024);
			byte[] data = new byte[20971520];
			int offset = 0;		
			int length = 0;
			while((length = fs.read(data, 0, 20971520)) != -1){
				result = VtExtDecryptBuffer(ulHeader.getValue(), offset, data, length);
				if( 0 != result)
					return;
				fse.write(data, 0, length);
				offset += length;
			}
			VtExtDeleteHeader(ulHeader.getValue());
			fs.close();
			fse.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		if( 0 == result)
			ExecutionSteps();
    }
    
    public static void OutsidePack(){
    	int result = 0;
    	try{
			FileInputStream fileStream = new FileInputStream("OutsidePack.txt");
			int len = fileStream.available();
			byte[] by = new byte[len];
			fileStream.read(by, 0, len);
			result = VtExtOutsidePack(by, len);
			fileStream.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
        if(0 == result)
        	ExecutionSteps();
    }
    
	public static void ExecutionSteps () {
		System.out.println("请输入执行操作步骤:1.获取授权码,2.密文判断,3.加密,4.解密,5.流加密,6.流解密,7.制作外发包,0.结束......");
		Scanner pScan = new Scanner(System.in);
		String step = pScan.nextLine();//字符类型的输入方式
		switch (step)
		{
			case "0":
				break;
			case "1":
				InitWithServer();
				break;
			case "2":
                IsCryptFile();
                break;
            case "3":
                EncryptFile();
                break;
            case "4":
                DecryptFile();
                break;
            case "5":
            	EncryptBuffer();
            	break;
            case "6":
            	DecryptBuffer();
            	break;
            case "7":
            	OutsidePack();
            	break;
			default:
				System.out.println("无此步驟!");
				ExecutionSteps();
				break;
		}
		
	}

	public static void main(String[] args) {
		ExecutionSteps();
	}
}
