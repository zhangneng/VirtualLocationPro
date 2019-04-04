package cn.com.cmplatform.utils;

import java.io.File;

import org.zeroturnaround.zip.ZipUtil;

public class UtilsZip {
	private static UtilsZip zip_instance;

	public synchronized static UtilsZip getInstance() {
		if (null == zip_instance) {
			zip_instance = new UtilsZip();
		}
		return zip_instance;
	}

	UtilsZip() {

	}

	void UtilsCompress(String InFile, String OutFile) {
		ZipUtil.pack(new File(InFile), new File(OutFile));
	}

	void UtilsUnCompress(String InFile, String OutFile) {
		ZipUtil.unpack(new File(InFile), new File(OutFile));
	}

	byte[] UtilsUnCompressToByte(String InFile, String OutFile) {
		return ZipUtil.unpackEntry(new File(InFile), "foo.txt");
	}
}
